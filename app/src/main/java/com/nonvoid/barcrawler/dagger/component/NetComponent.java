package com.nonvoid.barcrawler.dagger.component;


import com.nonvoid.barcrawler.activity.BeerDetailsActivity;
import com.nonvoid.barcrawler.activity.BreweryDetailsActivity;
import com.nonvoid.barcrawler.activity.HomeActivity;
import com.nonvoid.barcrawler.dagger.module.AppModule;
import com.nonvoid.barcrawler.dagger.module.NetModule;
import com.nonvoid.barcrawler.fragment.BeerListFragment;
import com.nonvoid.barcrawler.fragment.BreweryListFragment;
import com.nonvoid.barcrawler.fragment.BreweryLocationFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {
    void inject(BreweryDetailsActivity activity);
    void inject(BeerDetailsActivity activity);
    void inject(BreweryListFragment fragment);
    void inject(BreweryLocationFragment fragment);
    void inject(BeerListFragment fragment);
    void inject(HomeActivity activity);
}
