package com.nonvoid.barcrawler.database;

import android.util.Log;

import com.nonvoid.barcrawler.model.response.BeerResponse;
import com.nonvoid.barcrawler.model.response.BreweryResponse;
import com.nonvoid.barcrawler.model.response.LocationResponse;
import com.nonvoid.barcrawler.model.Brewery;
import com.nonvoid.barcrawler.model.BreweryLocation;
import com.nonvoid.barcrawler.model.Beer;

import java.util.List;


import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class BreweryDataBaseClient implements BreweryDataBaseAPI {

    private BreweryDataBaseService service;

    public BreweryDataBaseClient(BreweryDataBaseService service) {
        this.service = service;
    }

    @Override
    public Observable<List<BreweryLocation>> searchCityForBreweries(String city) {
        return service.searchCityForBreweries(city)
                .compose(applySchedulers())
                .map(LocationResponse::getLocations);
    }

    @Override
    public Observable<List<Beer>> getBeersForBrewery(String breweryId) {
        return service.getBeersForBrewery(breweryId)
                .compose(applySchedulers())
                .map(BeerResponse::getBeers);
    }

    @Override
    public Observable<List<Brewery>> searchForBrewery(String query) {
        return service.searchForBrewery(query)
                .compose(applySchedulers())
                .map(BreweryResponse::getBreweries);
    }

    @Override
    public Observable<List<Beer>> searchForBeer(String query) {
        return service.searchForBeer(query)
                .compose(applySchedulers())
                .map(BeerResponse::getBeers);
    }

    @Override
    public Observable<Brewery> getBreweryById(String breweryId) {
        return service.getBreweryById(breweryId)
                .compose(applySchedulers())
                .map(response ->{
                    if(response!=null && response.getBreweries() !=null && !response.getBreweries().isEmpty()){
                        return response.getBreweries().get(0);
                    }
                    return null;
                });
    }

    @Override
    public Observable<List<Brewery>> getBreweriesById(List<String> idList){
        StringBuilder param = new StringBuilder();
        param.append(idList.get(0));
        for(int i=1; i < 10 && i < idList.size(); i++){
            param.append(",").append(idList.get(i));
        }

        return service.getBreweriesById(param.toString())
                .compose(applySchedulers())
                .map(BreweryResponse::getBreweries);
    }

    @Override
    public Observable<List<Beer>> getBeersById(List<String> idList) {
        StringBuilder param = new StringBuilder();
        param.append(idList.get(0));
        for(int i=1; i<10 && i<idList.size(); i++){
            param.append(",").append(idList.get(i));
        }

        return service.getBeersById(param.toString())
                .compose(applySchedulers())
                .map(BeerResponse::getBeers);
    }

    @Override
    public Observable<Beer> getBeerById(String beerId) {
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
        return observable ->
                observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Log.d("MPG", throwable.getMessage(), throwable));
    }
}
