package com.nonvoid.barcrawler.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matt on 5/12/2017.
 */

public class Beer implements Parcelable {
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

    protected Beer(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
    }

    public static final Creator<Beer> CREATOR = new Creator<Beer>() {
        @Override
        public Beer createFromParcel(Parcel in) {
            return new Beer(in);
        }

        @Override
        public Beer[] newArray(int size) {
            return new Beer[size];
        }
    };

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
        return style.shortName != null ? style.shortName : "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
    }

    class Style{
        @SerializedName("abvMin")
        String abv;
        @SerializedName("shortName")
        String shortName;
    }
}
