package com.nonvoid.barcrawler.datalayer.response;

import com.google.gson.annotations.SerializedName;
import com.nonvoid.barcrawler.model.BreweryLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 5/5/2017.
 */

public class LocationResponse {
    @SerializedName("currentPage")
    int currentPage;
    @SerializedName("numberOfPages")
    int numberOfPages;
    @SerializedName("totalResults")
    int results;
    @SerializedName("data")
    ArrayList<BreweryLocation> locations;

    public int getCurrentPage() {
        return currentPage;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public int getResults() {
        return results;
    }

    public ArrayList<BreweryLocation> getLocations() {
        if(locations == null){
            locations = new ArrayList<>();
        }
        return locations;
    }
}
