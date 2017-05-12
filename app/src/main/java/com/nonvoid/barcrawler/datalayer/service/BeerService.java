package com.nonvoid.barcrawler.datalayer.service;

import com.nonvoid.barcrawler.datalayer.response.LocationResponse;
import com.nonvoid.barcrawler.model.Brewery;


import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Matt on 5/5/2017.
 */

public interface BeerService {

    @GET("locations?key=98d5ee318c335e35af66cc5f952fd412&format=json")
    Observable<LocationResponse> getBrewery(@Query("locality") String city);
}
