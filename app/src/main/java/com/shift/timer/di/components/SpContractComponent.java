package com.shift.timer.di.components;


import com.shift.timer.SpContract;
import com.shift.timer.di.modules.SpContractModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = SpContractModule.class)
public interface SpContractComponent {
    SpContract inject();
}
