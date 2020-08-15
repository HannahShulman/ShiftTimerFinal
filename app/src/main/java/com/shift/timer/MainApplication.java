package com.shift.timer;

import android.app.Application;

import androidx.annotation.Nullable;

import com.facebook.stetho.Stetho;
import com.shift.timer.di.components.DaggerNetComponent;
import com.shift.timer.di.components.NetComponent;
import com.shift.timer.di.modules.AppModule;
import com.shift.timer.di.modules.NetModule;


public class MainApplication extends Application {

    private NetComponent netComponent;

    private static MainApplication instance;


    @Nullable
    public static MainApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();

        Stetho.initializeWithDefaults(this);
        initComponents();
    }

    public NetComponent getNetComponent() {
        return netComponent;
    }

    public void initComponents() {
        netComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule("abcd"))
                .build();
    }
}