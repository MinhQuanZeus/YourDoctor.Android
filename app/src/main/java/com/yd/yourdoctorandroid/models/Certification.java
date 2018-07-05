package com.yd.yourdoctorandroid.models;

public class Certification {
    private String _id;
    private String name;
    private String path_image;

    public Certification(String _id, String name, String path_image) {
        this._id = _id;
        this.name = name;
        this.path_image = path_image;
    }

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

    public String getPath_image() {
        return path_image;
    }

    public void setPath_image(String path_image) {
        this.path_image = path_image;
    }
}
