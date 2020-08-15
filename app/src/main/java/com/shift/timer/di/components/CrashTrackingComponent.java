package com.shift.timer.di.components;


import com.shift.timer.di.modules.CrashTrackingModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = CrashTrackingModule.class)
public interface CrashTrackingComponent {
//    CrashTracker inject();
}
