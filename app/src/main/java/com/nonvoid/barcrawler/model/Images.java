
package com.nonvoid.barcrawler.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Images implements Parcelable
{

    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("medium")
    @Expose
    private String medium;
    @SerializedName("large")
    @Expose
    private String large;
    @SerializedName("squareMedium")
    @Expose
    private String squareMedium;
    @SerializedName("squareLarge")
    @Expose
    private String squareLarge;
    public final static Parcelable.Creator<Images> CREATOR = new Creator<Images>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Images createFromParcel(Parcel in) {
            Images instance = new Images();
            instance.icon = ((String) in.readValue((String.class.getClassLoader())));
            instance.medium = ((String) in.readValue((String.class.getClassLoader())));
            instance.large = ((String) in.readValue((String.class.getClassLoader())));
            instance.squareMedium = ((String) in.readValue((String.class.getClassLoader())));
            instance.squareLarge = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Images[] newArray(int size) {
            return (new Images[size]);
        }

    }
    ;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getSquareMedium() {
        return squareMedium;
    }

    public void setSquareMedium(String squareMedium) {
        this.squareMedium = squareMedium;
    }

    public String getSquareLarge() {
        return squareLarge;
    }

    public void setSquareLarge(String squareLarge) {
        this.squareLarge = squareLarge;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(icon);
        dest.writeValue(medium);
        dest.writeValue(large);
        dest.writeValue(squareMedium);
        dest.writeValue(squareLarge);
    }

    public int describeContents() {
        return  0;
    }

}
