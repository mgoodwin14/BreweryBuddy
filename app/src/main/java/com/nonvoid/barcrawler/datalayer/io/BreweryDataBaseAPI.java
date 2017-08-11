package com.nonvoid.barcrawler.datalayer.io;

import com.nonvoid.barcrawler.model.Brewery;
import com.nonvoid.barcrawler.model.BreweryLocation;
import com.nonvoid.barcrawler.model.Beer;

import java.util.List;

import io.reactivex.Observable;


/**
 * Created by Matt on 5/5/2017.
 * This interface is used by retro
 */

public interface BreweryDataBaseAPI {
    Observable<List<BreweryLocation>> searchCityForBreweries(String city);
    Observable<List<Brewery>> searchForBrewery(String query);
    Observable<List<Beer>> searchForBeer(String query);
    Observable<Brewery> getBreweryById(String breweryId);
    Observable<List<Beer>> getBeersForBrewery(String breweryId);
    Observable<Beer> getBeerById(String beerId);
}
