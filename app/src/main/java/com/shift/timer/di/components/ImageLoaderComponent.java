package com.shift.timer.di.components;


import com.shift.timer.di.modules.ImageLoaderModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ImageLoaderModule.class)
public interface ImageLoaderComponent {
//    ImageLoader inject();
}
