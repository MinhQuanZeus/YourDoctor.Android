package com.yd.yourdoctorandroid.networks.models;

public class Specialist {
    String _id;
    String name;

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

    @Override
    public String toString() {
        return "Specialist{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
