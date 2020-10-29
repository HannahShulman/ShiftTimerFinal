package com.shift.timer.di.components;

import android.content.SharedPreferences;

import com.shift.timer.dao.AdditionalHoursSettingDao;
import com.shift.timer.dao.ShiftDao;
import com.shift.timer.dao.TravelExpensesDao;
import com.shift.timer.db.AppDB;
import com.shift.timer.di.modules.AppModule;
import com.shift.timer.di.modules.NetModule;
import com.shift.timer.repositories.SettingsRepository;
import com.shift.timer.repositories.ShiftRepository;
import com.shift.timer.repositories.WorkplaceRepository;

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

    TravelExpensesDao travelExpensesDao();

    AdditionalHoursSettingDao additionalHoursSettingDao();

    SettingsRepository settingsRepository();

}
