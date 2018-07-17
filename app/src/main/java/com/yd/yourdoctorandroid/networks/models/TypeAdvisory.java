package com.yd.yourdoctorandroid.networks.models;

public class TypeAdvisory {
    String _id;
    String name;
    String price;
    String limitNumberRecords;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLimitNumberRecords() {
        return limitNumberRecords;
    }

    public void setLimitNumberRecords(String limitNumberRecords) {
        this.limitNumberRecords = limitNumberRecords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
