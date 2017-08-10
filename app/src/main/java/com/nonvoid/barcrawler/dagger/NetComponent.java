package com.nonvoid.barcrawler.dagger;


import com.nonvoid.barcrawler.beer.BeerDetailsActivity;
import com.nonvoid.barcrawler.brewery.BreweryDetailsActivity;
import com.nonvoid.barcrawler.HomeActivity;
import com.nonvoid.barcrawler.beer.BeerListFragment;
import com.nonvoid.barcrawler.brewery.BreweryListFragment;
import com.nonvoid.barcrawler.location.BreweryLocationFragment;

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
