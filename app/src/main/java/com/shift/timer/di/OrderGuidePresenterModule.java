package com.shift.timer.di;

import com.shift.timer.viewmodels.CurrentShiftViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class OrderGuidePresenterModule {


    private final CurrentShiftViewModel shoppingListViewModel;

    public OrderGuidePresenterModule( CurrentShiftViewModel shoppingListViewModel) {
        this.shoppingListViewModel = shoppingListViewModel;
    }

    @Provides
    CurrentShiftViewModel shoppingListViewModel(){
        return shoppingListViewModel;
    }

}