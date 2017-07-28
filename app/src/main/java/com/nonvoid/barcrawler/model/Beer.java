
package com.nonvoid.barcrawler.model;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Beer implements Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("nameDisplay")
    @Expose
    private String nameDisplay;
    @SerializedName("description")
    private String description;
    @SerializedName("abv")
    @Expose
    private String abv;
    @SerializedName("styleId")
    @Expose
    private int styleId;
    @SerializedName("isOrganic")
    @Expose
    private String isOrganic;
    @SerializedName("labels")
    @Expose
    private Labels labels;
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
    @SerializedName("style")
    @Expose
    private Style style;
    @SerializedName("breweries")
    @Expose
    private List<Brewery> breweries = new ArrayList<Brewery>();
    @SerializedName("type")
    @Expose
    private String type;
    public final static Parcelable.Creator<Beer> CREATOR = new Creator<Beer>() {

        @SuppressWarnings({
            "unchecked"
        })
        public Beer createFromParcel(Parcel in) {
            Beer instance = new Beer();
            instance.id = ((String) in.readValue((String.class.getClassLoader())));
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            instance.nameDisplay = ((String) in.readValue((String.class.getClassLoader())));
            instance.abv = ((String) in.readValue((String.class.getClassLoader())));
            instance.styleId = ((int) in.readValue((int.class.getClassLoader())));
            instance.isOrganic = ((String) in.readValue((String.class.getClassLoader())));
            instance.labels = ((Labels) in.readValue((Labels.class.getClassLoader())));
            instance.status = ((String) in.readValue((String.class.getClassLoader())));
            instance.statusDisplay = ((String) in.readValue((String.class.getClassLoader())));
            instance.createDate = ((String) in.readValue((String.class.getClassLoader())));
            instance.updateDate = ((String) in.readValue((String.class.getClassLoader())));
            instance.style = ((Style) in.readValue((Style.class.getClassLoader())));
            in.readList(instance.breweries, (com.nonvoid.barcrawler.model.Brewery.class.getClassLoader()));
            instance.type = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Beer[] newArray(int size) {
            return (new Beer[size]);
        }

    }
    ;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNameDisplay() {
        return nameDisplay;
    }

    public String getDescription() {
        return description;
    }

    public String getAbv() {
        return abv;
    }

    public int getStyleId() {
        return styleId;
    }

    public String getIsOrganic() {
        return isOrganic;
    }

    public Labels getLabels() {
        return labels;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusDisplay() {
        return statusDisplay;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public Style getStyle() {
        return style;
    }

    public List<Brewery> getBreweries() {
        return breweries;
    }

    public String getType() {
        return type;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(nameDisplay);
        dest.writeValue(abv);
        dest.writeValue(styleId);
        dest.writeValue(isOrganic);
        dest.writeValue(labels);
        dest.writeValue(status);
        dest.writeValue(statusDisplay);
        dest.writeValue(createDate);
        dest.writeValue(updateDate);
        dest.writeValue(style);
        dest.writeList(breweries);
        dest.writeValue(type);
    }

    public int describeContents() {
        return  0;
    }

}
