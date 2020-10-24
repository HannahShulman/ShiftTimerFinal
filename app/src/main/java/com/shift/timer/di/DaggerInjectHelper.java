package com.shift.timer.di;

import com.shift.timer.MainApplication;
import com.shift.timer.di.components.DaggerOrderGuideComponent;
import com.shift.timer.ui.CompletedShiftFragment;
import com.shift.timer.ui.CurrentShiftFragment;
import com.shift.timer.ui.EditShiftFragment;

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
    public static void inject(CompletedShiftFragment fragment) {
        DaggerOrderGuideComponent.builder()
                .netComponent(((MainApplication) fragment.getContext().getApplicationContext()).getNetComponent())
                .build()
                .inject(fragment);
    }
}