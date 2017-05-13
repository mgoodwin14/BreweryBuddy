package com.nonvoid.barcrawler.datalayer.client;

import com.nonvoid.barcrawler.datalayer.api.BreweryAPI;
import com.nonvoid.barcrawler.datalayer.response.LocationResponse;
import com.nonvoid.barcrawler.datalayer.service.BeerService;
import com.nonvoid.barcrawler.model.BreweryLocation;

import java.util.List;


import io.reactivex.Observable;
import retrofit2.Retrofit;

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;



/**
 * Created by Matt on 5/5/2017.
 */

public class BreweryClient implements BreweryAPI {

    private BeerService service;

    public BreweryClient() {

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://api.brewerydb.com/v2/")
                .build();
        service = retrofit.create(BeerService.class);
    }

    @Override
    public Observable<List<BreweryLocation>> getLocationsInCity(String city) {
        return service.getBrewery(city)
                //.subscribeOn(Schedulers.io())
                //.observeOn(AndroidSchedulers.mainThread())
                //.unsubscribeOn(Schedulers.io())
                .map(LocationResponse::getLocations);
    }
}
