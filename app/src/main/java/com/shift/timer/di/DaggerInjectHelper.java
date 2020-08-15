package com.shift.timer.di;

import com.shift.timer.MainApplication;
import com.shift.timer.di.components.DaggerOrderGuideComponent;
import com.shift.timer.ui.CurrentShiftFragment;

public class DaggerInjectHelper {

    public static void inject(CurrentShiftFragment fragment) {
        DaggerOrderGuideComponent.builder()
                .netComponent(((MainApplication) fragment.getContext().getApplicationContext()).getNetComponent())
                .build()
                .inject(fragment);
    }
}