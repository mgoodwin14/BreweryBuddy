package com.nonvoid.barcrawler.model.response;

import com.google.gson.annotations.SerializedName;
import com.nonvoid.barcrawler.model.Beer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 7/17/2017.
 */

public class BeerResponse extends BaseResponse{

    @SerializedName("data")
    List<Beer> beers;

    public List<Beer> getBeers() {
        if(beers == null){
            beers = new ArrayList<>();
        }
        return beers;
    }

    public void setBeers(List<Beer> beers) {
        this.beers = beers;
    }
}
