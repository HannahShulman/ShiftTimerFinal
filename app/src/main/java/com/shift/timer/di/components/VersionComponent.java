package com.shift.timer.di.components;


import com.shift.timer.di.modules.VersionModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = VersionModule.class)
public interface VersionComponent {
//    Version inject();
}
