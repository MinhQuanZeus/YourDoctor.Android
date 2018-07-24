package com.yd.yourdoctorandroid.networks.models;

public class TypeAdvisory {
    String _id;
    String name;
    long price;
    int limitNumberRecords;
    String description;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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
