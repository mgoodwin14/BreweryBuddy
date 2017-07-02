package com.nonvoid.barcrawler.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Matt on 5/3/2017.
 */

public class Brewery implements Parcelable {

    @SerializedName("id")
    private String id;
    @SerializedName("description")
    private String description;
    @SerializedName("name")
    private String name;
    @SerializedName("nameShortDisplay")
    private String nameShortDisplay;
    @SerializedName("brandClassification")
    private String brandClassification;
    @SerializedName("images")
    private Images images;


    public static final Creator<Brewery> CREATOR = new Creator<Brewery>() {
        @Override
        public Brewery createFromParcel(Parcel in) {
            return new Brewery(in);
        }

        @Override
        public Brewery[] newArray(int size) {
            return new Brewery[size];
        }
    };

    protected Brewery(Parcel in) {
        id = in.readString();
        description = in.readString();
        name = in.readString();
        nameShortDisplay = in.readString();
        brandClassification = in.readString();
//        images.icon = in.readString();
//        images.squareMedium = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(description);
        dest.writeString(name);
        dest.writeString(nameShortDisplay);
        dest.writeString(brandClassification);
//        dest.writeString(images.icon);
//        dest.writeString(images.squareMedium);
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getNameShortDisplay() {
        return nameShortDisplay;
    }

    public String getBrandClassification() {
        return brandClassification;
    }

    public String getIcon(){
        return images.icon;
    }

    public String getSquareMediumImage(){
        return images.squareMedium;
    }

    class Images {
        @SerializedName("icon")
        String icon;
        @SerializedName("squareMedium")
        String squareMedium;
    }
}
