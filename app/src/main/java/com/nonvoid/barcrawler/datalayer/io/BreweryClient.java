package com.nonvoid.barcrawler.datalayer.io;

import com.nonvoid.barcrawler.model.response.BeerResponse;
import com.nonvoid.barcrawler.model.response.BreweryResponse;
import com.nonvoid.barcrawler.model.response.LocationResponse;
import com.nonvoid.barcrawler.model.Brewery;
import com.nonvoid.barcrawler.model.BreweryLocation;
import com.nonvoid.barcrawler.model.Beer;

import java.util.ArrayList;


import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;



/**
 * Created by Matt on 5/5/2017.
 */

public class BreweryClient implements BreweryAPI {

    private BeerService service;

    public BreweryClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .baseUrl("http://api.brewerydb.com/v2/")
                .build();
        service = retrofit.create(BeerService.class);
    }

    public BreweryClient(BeerService service) {
        this.service = service;
    }

    @Override
    public Observable<ArrayList<BreweryLocation>> getLocationsInCity(String city) {
        return service.getBrewery(city)
                .compose(applySchedulers())
                .map(LocationResponse::getLocations);
    }

    @Override
    public Observable<ArrayList<Beer>> getBeersForBrewery(String breweryId) {
        return service.getBeersForBrewery(breweryId)
                .compose(applySchedulers())
                .map(BeerResponse::getBeers);
    }

    @Override
    public Observable<ArrayList<Brewery>> searchForBrewery(String query) {
        return service.searchForBrewery(query)
                .compose(applySchedulers())
                .map(BreweryResponse::getBreweries);
    }

    @Override
    public Observable<ArrayList<Beer>> searchForBeer(String query) {
        return service.searchForBeer(query)
                .compose(applySchedulers())
                .map(BeerResponse::getBeers);
    }

    @Override
    public Observable<Beer> getBeer(String beerId) {
        return service.getBeerById(beerId)
                .compose(applySchedulers())
                .map(response -> {
                    if(response!=null && response.getBeers() != null && !response.getBeers().isEmpty()) {
                        return response.getBeers().get(0);
                    }
                    return null;
                });
    }

    private <T> ObservableTransformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
