
package com.nonvoid.barcrawler.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Labels implements Parcelable
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
    public final static Parcelable.Creator<Labels> CREATOR = new Creator<Labels>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Labels createFromParcel(Parcel in) {
            Labels instance = new Labels();
            instance.icon = ((String) in.readValue((String.class.getClassLoader())));
            instance.medium = ((String) in.readValue((String.class.getClassLoader())));
            instance.large = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Labels[] newArray(int size) {
            return (new Labels[size]);
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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(icon);
        dest.writeValue(medium);
        dest.writeValue(large);
    }

    public int describeContents() {
        return  0;
    }

}
