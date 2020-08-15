package com.shift.timer.di.modules;

import android.location.Geocoder;


import com.shift.timer.MainApplication;

import java.util.Locale;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class GeoCoderModule {

    public GeoCoderModule() { }

    @Provides
    @Singleton
    public Geocoder providesUser()  {
        return new Geocoder(MainApplication.getInstance(), Locale.getDefault());
    }
}
