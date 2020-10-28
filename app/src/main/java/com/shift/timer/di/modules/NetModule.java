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
import com.shift.timer.db.AdditionalHoursSettingDao;
import com.shift.timer.db.AppDB;
import com.shift.timer.db.BreakCalculationsDao;
import com.shift.timer.db.MonthlyStartingCalculationsSettingDao;
import com.shift.timer.db.NotifySettingDao;
import com.shift.timer.db.RatePerDaySettingDao;
import com.shift.timer.db.ShiftDao;
import com.shift.timer.db.TravelExpensesDao;
import com.shift.timer.db.WageSettingDao;
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

                db.execSQL("CREATE TABLE IF NOT EXISTS `WageSetting` (`id` INTEGER NOT NULL, `wage` INTEGER NOT NULL, `workplaceId` INTEGER NOT NULL, PRIMARY KEY(`id`))");
                ContentValues wageValue = new ContentValues();
                wageValue.put("id", -1);
                wageValue.put("wage", 3000);
                wageValue.put("workplaceId", -1);
                db.insert("WageSetting", OnConflictStrategy.REPLACE, wageValue);

                db.execSQL("CREATE TABLE IF NOT EXISTS `AdditionalHoursSetting` (`id` INTEGER NOT NULL, `workplaceId` INTEGER NOT NULL,`regularRateMinutes` INTEGER NOT NULL, PRIMARY KEY(`id`))");
                ContentValues regularRateMinutes = new ContentValues();
                regularRateMinutes.put("id", -1);
                regularRateMinutes.put("workplaceId", -1);
                regularRateMinutes.put("regularRateMinutes", 8.5 * 60);
                db.insert("AdditionalHoursSetting", OnConflictStrategy.REPLACE, regularRateMinutes);

                db.execSQL("CREATE TABLE IF NOT EXISTS `TravelExpensesSetting` (`id` INTEGER NOT NULL, `workplaceId` INTEGER NOT NULL,`singleTravelExpense` INTEGER NOT NULL,`shouldCalculate` INTEGER NOT NULL, PRIMARY KEY(`id`))");
                ContentValues travelExpenseSetting = new ContentValues();
                travelExpenseSetting.put("id", -1);
                travelExpenseSetting.put("workplaceId", -1);
                travelExpenseSetting.put("singleTravelExpense", 590);
                travelExpenseSetting.put("shouldCalculate", 0);
                db.insert("TravelExpensesSetting", OnConflictStrategy.REPLACE, travelExpenseSetting);


                db.execSQL("CREATE TABLE IF NOT EXISTS `BreakCalculationsSetting` (`id` INTEGER NOT NULL, `minutesToDeduct` INTEGER NOT NULL, `workplaceId` INTEGER NOT NULL, PRIMARY KEY(`id`))");
                ContentValues breaksSetting = new ContentValues();
                breaksSetting.put("id", -1);
                breaksSetting.put("minutesToDeduct", 0);
                breaksSetting.put("workplaceId", -1);
                db.insert("BreakCalculationsSetting", OnConflictStrategy.REPLACE, breaksSetting);

                db.execSQL("CREATE TABLE IF NOT EXISTS `MonthlyStartingCalculationsSetting` (`id` INTEGER NOT NULL, `dayOfMonth` INTEGER NOT NULL, `workplaceId` INTEGER NOT NULL, PRIMARY KEY(`id`))");
                ContentValues monthlyPeriod = new ContentValues();
                monthlyPeriod.put("id", -1);
                monthlyPeriod.put("dayOfMonth", 1);
                monthlyPeriod.put("workplaceId", -1);
                db.insert("MonthlyStartingCalculationsSetting", OnConflictStrategy.REPLACE, monthlyPeriod);
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
    RatePerDaySettingDao provideRatePerDaySettingDao(AppDB db) {
        return db.ratePerDaySettingDao();
    }
    @Provides
    @Singleton
    NotifySettingDao provideNotifySettingDao(AppDB db) {
        return db.notifySettingDao();
    }

    @Provides
    @Singleton
    BreakCalculationsDao provideBreakCalculationsDao(AppDB db) {
        return db.breakCalculationsDao();
    }

    @Provides
    @Singleton
    AdditionalHoursSettingDao provideAdditionalHoursSettingDao(AppDB db) {
        return db.additionalHoursSettingDao();
    }
    @Provides
    @Singleton
    MonthlyStartingCalculationsSettingDao provideMonthlyStartingCalculationsSettingDao(AppDB db) {
        return db.monthlyStartingCalculationsSettingDao();
    }

    @Provides
    @Singleton
    WorkplaceDao provideWorkplaceDao(AppDB db) {
        return db.workplaceDao();
    }

    @Provides
    @Singleton
    WageSettingDao provideWageSettingDao(AppDB db) {
        return db.wageSettingDao();
    }

    @Provides
    @Singleton
    TravelExpensesDao provideTravelExpensesDao(AppDB db) {
        return db.travelExpensesDao();
    }

    @Provides
    @Singleton
    ShiftRepository provideShiftRepository(SpContract spContract, ShiftDao shiftDao, WageSettingDao wageSettingDao) {
        return new ShiftRepository(spContract, shiftDao, wageSettingDao);
    }

    @Provides
    @Singleton
    WorkplaceRepository provideWorkplaceRepository(SpContract spContract, WorkplaceDao workplaceDao) {
        return new WorkplaceRepository(spContract, workplaceDao);
    }
}
