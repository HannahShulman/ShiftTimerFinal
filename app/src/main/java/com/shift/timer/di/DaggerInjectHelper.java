package com.shift.timer.di;

import com.shift.timer.MainApplication;
import com.shift.timer.di.components.DaggerOrderGuideComponent;
import com.shift.timer.ui.settingfragments.AdditionalHoursSettingFragment;
import com.shift.timer.ui.settingfragments.BreaksSettingFragment;
import com.shift.timer.ui.CompletedShiftFragment;
import com.shift.timer.ui.CurrentShiftFragment;
import com.shift.timer.ui.EditShiftFragment;
import com.shift.timer.ui.settingfragments.HourlyPaymentSettingFragment;
import com.shift.timer.ui.SettingsFragment;
import com.shift.timer.ui.settingfragments.TravelExpensesSettingFragment;

public class DaggerInjectHelper {

    public static void inject(CurrentShiftFragment fragment) {
        DaggerOrderGuideComponent.builder()
                .netComponent(((MainApplication) fragment.getContext().getApplicationContext()).getNetComponent())
                .build()
                .inject(fragment);
    }
    public static void inject(EditShiftFragment fragment) {
        DaggerOrderGuideComponent.builder()
                .netComponent(((MainApplication) fragment.getContext().getApplicationContext()).getNetComponent())
                .build()
                .inject(fragment);
    }
    public static void inject(SettingsFragment fragment) {
        DaggerOrderGuideComponent.builder()
                .netComponent(((MainApplication) fragment.getContext().getApplicationContext()).getNetComponent())
                .build()
                .inject(fragment);
    }
    public static void inject(AdditionalHoursSettingFragment fragment) {
        DaggerOrderGuideComponent.builder()
                .netComponent(((MainApplication) fragment.getContext().getApplicationContext()).getNetComponent())
                .build()
                .inject(fragment);
    }
    public static void inject(HourlyPaymentSettingFragment fragment) {
        DaggerOrderGuideComponent.builder()
                .netComponent(((MainApplication) fragment.getContext().getApplicationContext()).getNetComponent())
                .build()
                .inject(fragment);
    }
    public static void inject(TravelExpensesSettingFragment fragment) {
        DaggerOrderGuideComponent.builder()
                .netComponent(((MainApplication) fragment.getContext().getApplicationContext()).getNetComponent())
                .build()
                .inject(fragment);
    }
    public static void inject(CompletedShiftFragment fragment) {
        DaggerOrderGuideComponent.builder()
                .netComponent(((MainApplication) fragment.getContext().getApplicationContext()).getNetComponent())
                .build()
                .inject(fragment);
    }
    public static void inject(BreaksSettingFragment fragment) {
        DaggerOrderGuideComponent.builder()
                .netComponent(((MainApplication) fragment.getContext().getApplicationContext()).getNetComponent())
                .build()
                .inject(fragment);
    }
}