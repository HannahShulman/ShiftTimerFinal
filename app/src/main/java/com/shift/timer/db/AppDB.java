package com.shift.timer.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.shift.timer.model.AdditionalHoursSetting;
import com.shift.timer.model.BreakCalculationsSetting;
import com.shift.timer.model.MonthlyStartingCalculationsSetting;
import com.shift.timer.model.Shift;
import com.shift.timer.model.TravelExpensesSetting;
import com.shift.timer.model.WageSetting;
import com.shift.timer.model.Workplace;

/**
 * Created by roy on 5/31/2017.
 */
@Database(entities = {Shift.class,
        Workplace.class,
        WageSetting.class,
        AdditionalHoursSetting.class,
        TravelExpensesSetting.class,
        BreakCalculationsSetting.class,
        MonthlyStartingCalculationsSetting.class
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
