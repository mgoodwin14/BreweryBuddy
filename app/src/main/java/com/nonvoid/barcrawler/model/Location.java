
package com.nonvoid.barcrawler.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location implements Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("streetAddress")
    @Expose
    private String streetAddress;
    @SerializedName("locality")
    @Expose
    private String locality;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("postalCode")
    @Expose
    private String postalCode;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("isPrimary")
    @Expose
    private String isPrimary;
    @SerializedName("inPlanning")
    @Expose
    private String inPlanning;
    @SerializedName("isClosed")
    @Expose
    private String isClosed;
    @SerializedName("openToPublic")
    @Expose
    private String openToPublic;
    @SerializedName("locationType")
    @Expose
    private String locationType;
    @SerializedName("locationTypeDisplay")
    @Expose
    private String locationTypeDisplay;
    @SerializedName("countryIsoCode")
    @Expose
    private String countryIsoCode;
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
    @SerializedName("country")
    @Expose
    private Country country;
    public final static Parcelable.Creator<Location> CREATOR = new Creator<Location>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Location createFromParcel(Parcel in) {
            Location instance = new Location();
            instance.id = ((String) in.readValue((String.class.getClassLoader())));
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            instance.streetAddress = ((String) in.readValue((String.class.getClassLoader())));
            instance.locality = ((String) in.readValue((String.class.getClassLoader())));
            instance.region = ((String) in.readValue((String.class.getClassLoader())));
            instance.postalCode = ((String) in.readValue((String.class.getClassLoader())));
            instance.phone = ((String) in.readValue((String.class.getClassLoader())));
            instance.website = ((String) in.readValue((String.class.getClassLoader())));
            instance.latitude = ((double) in.readValue((double.class.getClassLoader())));
            instance.longitude = ((double) in.readValue((double.class.getClassLoader())));
            instance.isPrimary = ((String) in.readValue((String.class.getClassLoader())));
            instance.inPlanning = ((String) in.readValue((String.class.getClassLoader())));
            instance.isClosed = ((String) in.readValue((String.class.getClassLoader())));
            instance.openToPublic = ((String) in.readValue((String.class.getClassLoader())));
            instance.locationType = ((String) in.readValue((String.class.getClassLoader())));
            instance.locationTypeDisplay = ((String) in.readValue((String.class.getClassLoader())));
            instance.countryIsoCode = ((String) in.readValue((String.class.getClassLoader())));
            instance.status = ((String) in.readValue((String.class.getClassLoader())));
            instance.statusDisplay = ((String) in.readValue((String.class.getClassLoader())));
            instance.createDate = ((String) in.readValue((String.class.getClassLoader())));
            instance.updateDate = ((String) in.readValue((String.class.getClassLoader())));
            instance.country = ((Country) in.readValue((Country.class.getClassLoader())));
            return instance;
        }

        public Location[] newArray(int size) {
            return (new Location[size]);
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

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(String isPrimary) {
        this.isPrimary = isPrimary;
    }

    public String getInPlanning() {
        return inPlanning;
    }

    public void setInPlanning(String inPlanning) {
        this.inPlanning = inPlanning;
    }

    public String getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(String isClosed) {
        this.isClosed = isClosed;
    }

    public String getOpenToPublic() {
        return openToPublic;
    }

    public void setOpenToPublic(String openToPublic) {
        this.openToPublic = openToPublic;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getLocationTypeDisplay() {
        return locationTypeDisplay;
    }

    public void setLocationTypeDisplay(String locationTypeDisplay) {
        this.locationTypeDisplay = locationTypeDisplay;
    }

    public String getCountryIsoCode() {
        return countryIsoCode;
    }

    public void setCountryIsoCode(String countryIsoCode) {
        this.countryIsoCode = countryIsoCode;
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

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(streetAddress);
        dest.writeValue(locality);
        dest.writeValue(region);
        dest.writeValue(postalCode);
        dest.writeValue(phone);
        dest.writeValue(website);
        dest.writeValue(latitude);
        dest.writeValue(longitude);
        dest.writeValue(isPrimary);
        dest.writeValue(inPlanning);
        dest.writeValue(isClosed);
        dest.writeValue(openToPublic);
        dest.writeValue(locationType);
        dest.writeValue(locationTypeDisplay);
        dest.writeValue(countryIsoCode);
        dest.writeValue(status);
        dest.writeValue(statusDisplay);
        dest.writeValue(createDate);
        dest.writeValue(updateDate);
        dest.writeValue(country);
    }

    public int describeContents() {
        return  0;
    }

}
