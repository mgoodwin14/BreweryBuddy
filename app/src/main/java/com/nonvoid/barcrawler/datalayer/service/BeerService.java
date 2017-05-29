package com.nonvoid.barcrawler.datalayer.service;

import com.nonvoid.barcrawler.datalayer.response.BeerResponse;
import com.nonvoid.barcrawler.datalayer.response.LocationResponse;



import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by Matt on 5/5/2017.
 */

public interface BeerService {

    @GET("locations?key=98d5ee318c335e35af66cc5f952fd412&format=json&isPrimary=y&openToPublic=y")
    Observable<LocationResponse> getBrewery(@Query("locality") String city);

    @GET("brewery/{id}/beers?key=98d5ee318c335e35af66cc5f952fd412&format=json")
    Observable<BeerResponse> getBeersForBrewery(@Path("id") String breweryId);
}
