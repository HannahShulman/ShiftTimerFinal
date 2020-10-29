package com.shift.timer.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.shift.timer.dao.AdditionalHoursSettingDao;
import com.shift.timer.dao.BreakCalculationsDao;
import com.shift.timer.dao.MonthlyStartingCalculationsSettingDao;
import com.shift.timer.dao.NotifySettingDao;
import com.shift.timer.dao.RatePerDaySettingDao;
import com.shift.timer.dao.ShiftDao;
import com.shift.timer.dao.TravelExpensesDao;
import com.shift.timer.dao.WageSettingDao;
import com.shift.timer.dao.WorkplaceDao;
import com.shift.timer.model.AdditionalHoursSetting;
import com.shift.timer.model.BreakCalculationsSetting;
import com.shift.timer.model.CurrentWorkplace;
import com.shift.timer.model.MonthlyStartingCalculationsSetting;
import com.shift.timer.model.NotifyAfterShiftSetting;
import com.shift.timer.model.NotifyOnArrivalSetting;
import com.shift.timer.model.NotifyOnLeaveSetting;
import com.shift.timer.model.RatePerDaySetting;
import com.shift.timer.model.Shift;
import com.shift.timer.model.TravelExpensesSetting;
import com.shift.timer.model.WageSetting;
import com.shift.timer.model.Workplace;

@Database(entities = {Shift.class,
        Workplace.class,
        WageSetting.class,
        AdditionalHoursSetting.class,
        TravelExpensesSetting.class,
        BreakCalculationsSetting.class,
        MonthlyStartingCalculationsSetting.class,
        RatePerDaySetting.class,
        NotifyOnArrivalSetting.class,
        NotifyOnLeaveSetting.class,
        NotifyAfterShiftSetting.class,
        CurrentWorkplace.class

}, version = 1)

@TypeConverters({Converters.class})
public abstract class AppDB extends RoomDatabase {

    public abstract ShiftDao shiftDao();

    public abstract WorkplaceDao workplaceDao();

    public abstract WageSettingDao wageSettingDao();

    public abstract AdditionalHoursSettingDao additionalHoursSettingDao();

    public abstract TravelExpensesDao travelExpensesDao();

    public abstract BreakCalculationsDao breakCalculationsDao();

    public abstract MonthlyStartingCalculationsSettingDao monthlyStartingCalculationsSettingDao();

    public abstract RatePerDaySettingDao ratePerDaySettingDao();

    public abstract NotifySettingDao notifySettingDao();
}
