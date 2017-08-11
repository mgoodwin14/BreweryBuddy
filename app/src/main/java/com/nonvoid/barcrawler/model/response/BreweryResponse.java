package com.nonvoid.barcrawler.model.response;

import com.google.gson.annotations.SerializedName;
import com.nonvoid.barcrawler.model.Brewery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 7/1/2017.
 */

public class BreweryResponse {
    @SerializedName("data")
    List<Brewery> breweries;

    public List<Brewery> getBreweries() {
        if(breweries == null){
            breweries = new ArrayList<>();
        }
        return breweries;
    }

    public void setBreweries(List<Brewery> breweries) {
        this.breweries = breweries;
    }
}
