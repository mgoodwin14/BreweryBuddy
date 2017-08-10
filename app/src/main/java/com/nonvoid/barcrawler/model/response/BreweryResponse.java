package com.nonvoid.barcrawler.model.response;

import com.google.gson.annotations.SerializedName;
import com.nonvoid.barcrawler.model.Brewery;

import java.util.ArrayList;

/**
 * Created by Matt on 7/1/2017.
 */

public class BreweryResponse {
    @SerializedName("data")
    ArrayList<Brewery> breweries;

    public ArrayList<Brewery> getBreweries() {
        if(breweries == null){
            breweries = new ArrayList<>();
        }
        return breweries;
    }
}
