package com.yd.yourdoctorandroid.networks.getRuleIntroAPI;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RuleObject {
    @SerializedName("_id")
    @Expose
    private String id;
    private String type;
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
