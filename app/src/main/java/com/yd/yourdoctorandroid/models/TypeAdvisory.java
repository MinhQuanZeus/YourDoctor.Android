package com.yd.yourdoctorandroid.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TypeAdvisory {

    @SerializedName("_id")
    @Expose
    String id;
    String name;
    long price;
    int limitNumberRecords;
    String description;

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

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getLimitNumberRecords() {
        return limitNumberRecords;
    }

    public void setLimitNumberRecords(int limitNumberRecords) {
        this.limitNumberRecords = limitNumberRecords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
