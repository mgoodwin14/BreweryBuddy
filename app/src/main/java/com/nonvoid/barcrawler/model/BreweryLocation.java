package com.nonvoid.barcrawler.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.nonvoid.barcrawler.util.StringUtils;

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
    @SerializedName("locality")
    String locality;
    @SerializedName("region")
    String region;

    protected BreweryLocation(Parcel in) {
        id = in.readString();
        breweryId = in.readString();
        name = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
        locality = in.readString();
        region = in.readString();
        breweryData=new BreweryData();
        breweryData.name = in.readString();
        breweryData.description = in.readString();
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

    public LatLng getLatLng(){
        return new LatLng(lat, lng);
    }

    public String getLocality() {
        return locality;
    }

    public String getRegion() {
        return region;
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
        dest.writeString(locality);
        dest.writeString(region);
        dest.writeString(breweryData.name);
        dest.writeString(breweryData.description);
    }

    public String getDescription() {
        if(breweryData!=null){
            return breweryData.description;
        }
        return "No description";
    }

    public class BreweryData{
        @SerializedName("name")
        String name;
        @SerializedName("description")
        String description;
    }
}
