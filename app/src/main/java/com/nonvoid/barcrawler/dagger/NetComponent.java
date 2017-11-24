package com.nonvoid.barcrawler.dagger;


import com.nonvoid.barcrawler.LoginActivity;
import com.nonvoid.barcrawler.beer.BeerDetailsActivity;
import com.nonvoid.barcrawler.brewery.BreweryDetailsActivity;
import com.nonvoid.barcrawler.HomeActivity;
import com.nonvoid.barcrawler.beer.BeerListFragment;
import com.nonvoid.barcrawler.brewery.BreweryListFragment;
import com.nonvoid.barcrawler.brewery.BreweryLocationFragment;
import com.nonvoid.barcrawler.social.ProfileFragment;

import org.jetbrains.annotations.NotNull;

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
    void inject(ProfileFragment fragment);

    void inject(@NotNull LoginActivity loginActivity);

    void inject(@NotNull com.nonvoid.barcrawler.redesign.SearchActivity searchActivity);
}
