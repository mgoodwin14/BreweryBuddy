package com.nonvoid.barcrawler.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.nonvoid.barcrawler.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matt on 5/5/2017.
 */

public class BreweryLocation implements Parcelable {
    @SerializedName("id")
    String id;
    @SerializedName("breweryId")
    String breweryId;
    @SerializedName("name")
    String name;
    @SerializedName("latitude")
    double lat;
    @SerializedName("longitude")
    double lng;
    @SerializedName("brewery")
    BreweryData breweryData;

    protected BreweryLocation(Parcel in) {
        id = in.readString();
        breweryId = in.readString();
        name = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
    }

    public static final Creator<BreweryLocation> CREATOR = new Creator<BreweryLocation>() {
        @Override
        public BreweryLocation createFromParcel(Parcel in) {
            return new BreweryLocation(in);
        }

        @Override
        public BreweryLocation[] newArray(int size) {
            return new BreweryLocation[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getBreweryId() {
        return breweryId;
    }

    public String getName() {
        if(breweryData!= null){
            if(StringUtils.isNotNullOrEmpty(breweryData.name)){
                return breweryData.name;
            }
        }
        return this.name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(breweryId);
        dest.writeString(name);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
    }

    public class BreweryData{
        @SerializedName("name")
        String name;
    }
}
