package com.shift.timer.di.components;


import com.shift.timer.di.OrderGuidePresenterModule;
import com.shift.timer.di.scope.FragmentScoped;
import com.shift.timer.ui.settingfragments.AdditionalHoursSettingFragment;
import com.shift.timer.ui.settingfragments.BreaksSettingFragment;
import com.shift.timer.ui.CompletedShiftFragment;
import com.shift.timer.ui.CurrentShiftFragment;
import com.shift.timer.ui.EditShiftFragment;
import com.shift.timer.ui.settingfragments.HourlyPaymentSettingFragment;
import com.shift.timer.ui.SettingsFragment;
import com.shift.timer.ui.settingfragments.MonthlyCalculationCycleFragment;
import com.shift.timer.ui.settingfragments.NotifyEndOfShiftSettingFragment;
import com.shift.timer.ui.settingfragments.RatePerDaySettingFragment;
import com.shift.timer.ui.settingfragments.TravelExpensesSettingFragment;
import com.shift.timer.ui.workplace.NoAdditionalWorkplacesDialog;

import dagger.Component;

/**
 * Created by roy on 5/31/2017.
 */
@FragmentScoped
@Component(dependencies = NetComponent.class, modules = OrderGuidePresenterModule.class)
public abstract class OrderGuideComponent {

    public abstract void inject(CurrentShiftFragment fragment);
    public abstract void inject(EditShiftFragment fragment);
    public abstract void inject(SettingsFragment fragment);
    public abstract void inject(CompletedShiftFragment fragment);
    public abstract void inject(HourlyPaymentSettingFragment fragment);
    public abstract void inject(AdditionalHoursSettingFragment fragment);
    public abstract void inject(TravelExpensesSettingFragment fragment);
    public abstract void inject(BreaksSettingFragment fragment);
    public abstract void inject(MonthlyCalculationCycleFragment fragment);
    public abstract void inject(RatePerDaySettingFragment fragment);
    public abstract void inject(NotifyEndOfShiftSettingFragment fragment);
    public abstract void inject(NoAdditionalWorkplacesDialog dialog);
}
