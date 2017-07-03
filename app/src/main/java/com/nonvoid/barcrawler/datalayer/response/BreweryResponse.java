package com.nonvoid.barcrawler.datalayer.response;

import com.google.gson.annotations.SerializedName;
import com.nonvoid.barcrawler.model.Brewery;

import java.util.ArrayList;

/**
 * Created by Matt on 7/1/2017.
 */

public class BreweryResponse {
    @SerializedName("data")
    ArrayList<Brewery> locations;

    public ArrayList<Brewery> getLocations() {
        return locations;
    }
}