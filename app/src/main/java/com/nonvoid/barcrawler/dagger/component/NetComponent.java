package com.nonvoid.barcrawler.dagger.component;


import com.nonvoid.barcrawler.activity.MainActivity;
import com.nonvoid.barcrawler.dagger.module.AppModule;
import com.nonvoid.barcrawler.dagger.module.NetModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {
    void inject(MainActivity activity);
}
