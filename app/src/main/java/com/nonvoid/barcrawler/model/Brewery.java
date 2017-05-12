package com.nonvoid.barcrawler.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Matt on 5/3/2017.
 */

public class Brewery {

    @SerializedName("id")
    private String id;
    @SerializedName("description")
    private String description;
    @SerializedName("name")
    private String name;

}
