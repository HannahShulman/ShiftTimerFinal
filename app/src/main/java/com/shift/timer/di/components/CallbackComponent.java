package com.shift.timer.di.components;


import com.shift.timer.di.scope.FragmentScoped;

import dagger.Component;

/**
 * Created by roy on 5/30/2017.
 */
@FragmentScoped
@Component(dependencies = NetComponent.class)
public interface CallbackComponent {
//    void inject(ResponseConverters converters);
}
