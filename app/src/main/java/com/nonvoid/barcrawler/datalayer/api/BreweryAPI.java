package com.nonvoid.barcrawler.datalayer.api;

import com.nonvoid.barcrawler.model.Brewery;
import com.nonvoid.barcrawler.model.BreweryLocation;
import com.nonvoid.barcrawler.model.Beer;

import java.util.ArrayList;

import io.reactivex.Observable;


/**
 * Created by Matt on 5/5/2017.
 * This interface is used by retro
 */

public interface BreweryAPI {
    Observable<ArrayList<BreweryLocation>> getLocationsInCity(String city);
    Observable<ArrayList<Beer>> getBeersForBrewery(String breweryId);
    Observable<ArrayList<Brewery>> searchForBrewery(String query);
    Observable<ArrayList<Beer>> searchForBeer(String query);
    Observable<Beer> getBeer(String beerId);
}
