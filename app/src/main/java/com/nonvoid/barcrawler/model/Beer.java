package com.nonvoid.barcrawler.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Created by Matt on 5/12/2017.
 */

public class Beer {
    @SerializedName("id")
    String id;
    @SerializedName("name")
    String name;
    @SerializedName("description")
    String description;
    @SerializedName("abv")
    String abv;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAbv() {
        return abv;
    }
}
