package com.yd.yourdoctorandroid.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Patient {
    private String id;
    @SerializedName("firstName")
    @Expose
    private String fName;
    @SerializedName("middleName")
    @Expose
    private String mName;
    @SerializedName("lastName")
    @Expose
    private String lName;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    private String password;
    private String avatar;
    private int status;
    private int gender;
    private String birthday;
    private String address;
    private int role;
    private long remainMoney;
    private List<String> favoriteDoctors;

    public Patient() {
    }

    public Patient(String id, String fName, String mName, String lName, String phoneNumber, String password, String avatar, int status ,int gender, String birthday, String address, int role, long remainMoney, List<String> favoriteDoctors) {
        this.id = id;
        this.fName = fName;
        this.mName = mName;
        this.lName = lName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.avatar = avatar;
        this.status = status;
        this.gender = gender;
        this.birthday = birthday;
        this.address = address;
        this.role = role;
        this.remainMoney = remainMoney;
        this.favoriteDoctors = favoriteDoctors;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String  getFullName(){
        return fName + " "+ mName + " " + lName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public long getRemainMoney() {
        return remainMoney;
    }

    public void setRemainMoney(long remainMoney) {
        this.remainMoney = remainMoney;
    }

    public List<String> getFavoriteDoctors() {
        return favoriteDoctors;
    }

    public void setFavoriteDoctors(List<String> favoriteDoctors) {
        this.favoriteDoctors = favoriteDoctors;
    }

    public String getFullName() {
        if(!this.mName.isEmpty()) {
            return this.fName + " " + this.mName + " " + this.lName;
        }else {
            return this.fName + " " + this.lName;
        }
    }
}
