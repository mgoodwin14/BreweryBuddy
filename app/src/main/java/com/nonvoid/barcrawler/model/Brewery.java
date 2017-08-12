
package com.nonvoid.barcrawler.model;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Brewery implements Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("nameShortDisplay")
    @Expose
    private String nameShortDisplay;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("established")
    @Expose
    private String established;
    @SerializedName("isOrganic")
    @Expose
    private String isOrganic;
    @SerializedName("images")
    @Expose
    private Images images;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("statusDisplay")
    @Expose
    private String statusDisplay;
    @SerializedName("createDate")
    @Expose
    private String createDate;
    @SerializedName("updateDate")
    @Expose
    private String updateDate;
    @SerializedName("isMassOwned")
    @Expose
    private String isMassOwned;
    @SerializedName("brandClassification")
    @Expose
    private String brandClassification;
    @SerializedName("breweryLocations")
    @Expose
    private final List<BreweryLocation> breweryLocations = new ArrayList<BreweryLocation>();
    public final static Parcelable.Creator<Brewery> CREATOR = new Creator<Brewery>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Brewery createFromParcel(Parcel in) {
            Brewery instance = new Brewery();
            instance.id = ((String) in.readValue((String.class.getClassLoader())));
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            instance.nameShortDisplay = ((String) in.readValue((String.class.getClassLoader())));
            instance.description = ((String) in.readValue((String.class.getClassLoader())));
            instance.website = ((String) in.readValue((String.class.getClassLoader())));
            instance.established = ((String) in.readValue((String.class.getClassLoader())));
            instance.isOrganic = ((String) in.readValue((String.class.getClassLoader())));
            instance.images = ((Images) in.readValue((Images.class.getClassLoader())));
            instance.status = ((String) in.readValue((String.class.getClassLoader())));
            instance.statusDisplay = ((String) in.readValue((String.class.getClassLoader())));
            instance.createDate = ((String) in.readValue((String.class.getClassLoader())));
            instance.updateDate = ((String) in.readValue((String.class.getClassLoader())));
            instance.isMassOwned = ((String) in.readValue((String.class.getClassLoader())));
            instance.brandClassification = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.breweryLocations, (BreweryLocation.class.getClassLoader()));
            return instance;
        }

        public Brewery[] newArray(int size) {
            return (new Brewery[size]);
        }

    }
    ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameShortDisplay() {
        return nameShortDisplay;
    }

    public void setNameShortDisplay(String nameShortDisplay) {
        this.nameShortDisplay = nameShortDisplay;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEstablished() {
        return established;
    }

    public void setEstablished(String established) {
        this.established = established;
    }

    public String getIsOrganic() {
        return isOrganic;
    }

    public void setIsOrganic(String isOrganic) {
        this.isOrganic = isOrganic;
    }

    public Images getImages() {
        if(images==null)
            images= new Images();
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDisplay() {
        return statusDisplay;
    }

    public void setStatusDisplay(String statusDisplay) {
        this.statusDisplay = statusDisplay;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getIsMassOwned() {
        return isMassOwned;
    }

    public void setIsMassOwned(String isMassOwned) {
        this.isMassOwned = isMassOwned;
    }

    public String getBrandClassification() {
        return brandClassification;
    }

    public void setBrandClassification(String brandClassification) {
        this.brandClassification = brandClassification;
    }

    public List<BreweryLocation> getBreweryLocations() {
        return breweryLocations;
    }

//    public void setLocations(List<BreweryLocation> breweryLocations) {
//        this.breweryLocations = breweryLocations;
//    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(nameShortDisplay);
        dest.writeValue(description);
        dest.writeValue(website);
        dest.writeValue(established);
        dest.writeValue(isOrganic);
        dest.writeValue(images);
        dest.writeValue(status);
        dest.writeValue(statusDisplay);
        dest.writeValue(createDate);
        dest.writeValue(updateDate);
        dest.writeValue(isMassOwned);
        dest.writeValue(brandClassification);
        dest.writeList(breweryLocations);
    }

    public int describeContents() {
        return  0;
    }

}
