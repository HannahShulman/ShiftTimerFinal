package com.shift.timer.di.modules;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import com.shift.timer.MainApplication;
import com.shift.timer.SpContract;

import java.util.ArrayList;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class VersionModule {
    public VersionModule() { }


    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(MainApplication.getInstance());
    }

    @Provides
    @Singleton
    SpContract provideSpContract(SharedPreferences sp) {
        return new SpContract(sp);
    }

}
