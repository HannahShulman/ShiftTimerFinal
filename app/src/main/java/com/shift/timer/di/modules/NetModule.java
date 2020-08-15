package com.shift.timer.di.modules;

import android.app.Application;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shift.timer.MainApplication;
import com.shift.timer.SpContract;
import com.shift.timer.db.AppDB;
import com.shift.timer.db.ShiftDao;
import com.shift.timer.db.WorkplaceDao;
import com.shift.timer.ui.ShiftRepository;
import com.shift.timer.ui.WorkplaceRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;

/**
 * Created by roy on 5/19/2017.
 */
@Module
public class NetModule {

    @NonNull
    private String baseUrl;

    public NetModule(@NonNull String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    SpContract provideSpContract(SharedPreferences sp) {
        return new SpContract(sp);
    }

    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB

        return new Cache(application.getCacheDir(), cacheSize);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    protected AppDB provideDB() {
        final RoomDatabase.Builder<AppDB> builder = Room.databaseBuilder(MainApplication.getInstance(), AppDB.class, "app-db");
        return builder.fallbackToDestructiveMigration().addCallback(new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                db.execSQL("CREATE TABLE IF NOT EXISTS `workplace` (`id` INTEGER NOT NULL, `description` TEXT, PRIMARY KEY(`id`))");
                ContentValues values = new ContentValues();
                values.put("id", -1);
                values.put("description", "עבודה 1");
                db.insert("workplace", OnConflictStrategy.REPLACE, values);
            }

            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
                ContentValues values = new ContentValues();
                values.put("id", 3);
                values.put("description", "עבודה 1");
                db.insert("workplace", OnConflictStrategy.REPLACE, values);
            }
        }).build();
    }

    @Provides
    @Singleton
    ShiftDao provideShiftDao(AppDB db) {
        return db.shiftDao();
    }

    @Provides
    @Singleton
    WorkplaceDao provideWorkplaceDao(AppDB db) {
        return db.workplaceDao();
    }

    @Provides
    @Singleton
    ShiftRepository provideShiftRepository(SpContract spContract, ShiftDao shiftDao, WorkplaceDao workplaceDao) {
        return new ShiftRepository(spContract, shiftDao);
    }

    @Provides
    @Singleton
    WorkplaceRepository provideWorkplaceRepository(SpContract spContract, WorkplaceDao workplaceDao) {
        return new WorkplaceRepository(spContract, workplaceDao);
    }
}
