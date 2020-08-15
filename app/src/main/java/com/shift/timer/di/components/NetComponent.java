package com.shift.timer.di.components;

import android.content.SharedPreferences;

import com.shift.timer.db.AppDB;
import com.shift.timer.db.ShiftDao;
import com.shift.timer.di.modules.AppModule;
import com.shift.timer.di.modules.NetModule;
import com.shift.timer.ui.ShiftRepository;
import com.shift.timer.ui.WorkplaceRepository;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by roy on 5/19/2017.
 */

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface NetComponent {

    NetComponent getNetComponent();

    SharedPreferences getSharedPreferences();

    ShiftRepository getRepository();

    WorkplaceRepository getWorkplaceRepository();

    AppDB getDB();

    ShiftDao getShiftDao();

}
