package com.nonvoid.barcrawler.datalayer.response;

import com.google.gson.annotations.SerializedName;
import com.nonvoid.barcrawler.model.Beer;

import java.util.ArrayList;

/**
 * Created by Matt on 5/29/2017.
 */

public class BeerResponse extends BaseResponse {
    @SerializedName("data")
    ArrayList<Beer> beers;

    public ArrayList<Beer> getBeers() {
        return beers;
    }
}
