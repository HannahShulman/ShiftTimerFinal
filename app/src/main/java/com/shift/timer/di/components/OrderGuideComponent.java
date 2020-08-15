package com.shift.timer.di.components;


import com.shift.timer.di.OrderGuidePresenterModule;
import com.shift.timer.di.scope.FragmentScoped;
import com.shift.timer.ui.CurrentShiftFragment;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

/**
 * Created by roy on 5/31/2017.
 */
@FragmentScoped
@Component(dependencies = NetComponent.class, modules = OrderGuidePresenterModule.class)
public abstract class OrderGuideComponent {

    public abstract void inject(CurrentShiftFragment fragment);
}
