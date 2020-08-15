package com.shift.timer.di.modules;

import android.preference.PreferenceManager;

import com.shift.timer.MainApplication;
import com.shift.timer.SpContract;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SpContractModule {

    @Provides
    @Singleton
    public SpContract providesSpContract()  {
        return new SpContract(PreferenceManager.getDefaultSharedPreferences(MainApplication.getInstance()));
    }
}
