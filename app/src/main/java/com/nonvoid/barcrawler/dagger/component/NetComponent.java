package com.nonvoid.barcrawler.dagger.component;


import com.nonvoid.barcrawler.activity.BreweryDetailsActivity;
import com.nonvoid.barcrawler.activity.MainActivity;
import com.nonvoid.barcrawler.dagger.module.AppModule;
import com.nonvoid.barcrawler.dagger.module.NetModule;
import com.nonvoid.barcrawler.fragment.BeerListFragment;
import com.nonvoid.barcrawler.fragment.BreweryListFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {
    void inject(MainActivity activity);
    void inject(BreweryDetailsActivity activity);
    void inject(BreweryListFragment fragment);
    void inject(BeerListFragment fragment);

}
