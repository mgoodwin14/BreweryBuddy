package com.nonvoid.barcrawler.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

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
    @SerializedName("labels")
    Map<String, String> labels;
    @SerializedName("style")
    Style style;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getShortName() {
        return style.shortName;
    }

    class Style{
        @SerializedName("abvMin")
        String abv;
        @SerializedName("shortName")
        String shortName;
    }
}
