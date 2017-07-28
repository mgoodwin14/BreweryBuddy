
package com.nonvoid.barcrawler.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Country implements Parcelable
{

    @SerializedName("isoCode")
    @Expose
    private String isoCode;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("isoThree")
    @Expose
    private String isoThree;
    @SerializedName("numberCode")
    @Expose
    private int numberCode;
    @SerializedName("createDate")
    @Expose
    private String createDate;
    public final static Parcelable.Creator<Country> CREATOR = new Creator<Country>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Country createFromParcel(Parcel in) {
            Country instance = new Country();
            instance.isoCode = ((String) in.readValue((String.class.getClassLoader())));
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            instance.displayName = ((String) in.readValue((String.class.getClassLoader())));
            instance.isoThree = ((String) in.readValue((String.class.getClassLoader())));
            instance.numberCode = ((int) in.readValue((int.class.getClassLoader())));
            instance.createDate = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Country[] newArray(int size) {
            return (new Country[size]);
        }

    }
    ;

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getIsoThree() {
        return isoThree;
    }

    public void setIsoThree(String isoThree) {
        this.isoThree = isoThree;
    }

    public int getNumberCode() {
        return numberCode;
    }

    public void setNumberCode(int numberCode) {
        this.numberCode = numberCode;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(isoCode);
        dest.writeValue(name);
        dest.writeValue(displayName);
        dest.writeValue(isoThree);
        dest.writeValue(numberCode);
        dest.writeValue(createDate);
    }

    public int describeContents() {
        return  0;
    }

}
