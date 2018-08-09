package com.yd.yourdoctorandroid.models;

import java.io.Serializable;

public class New implements Serializable{

    public String title;
    public String link;
    public String description;
    public String pubDate;
    public String image;

    public New(String title, String link, String description, String pubDate, String image) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
        this.image = image;
    }

    @Override
    public String toString() {
        return "New{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}

