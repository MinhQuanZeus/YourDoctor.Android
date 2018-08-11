package com.yd.yourdoctorandroid.networks.getListHistoryChat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoctorIdHistory {
    @SerializedName("id")
    @Expose
    private String _id;
    private String firstName;
    private String middleName;
    private String lastName;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
