package com.yd.yourdoctorandroid.models;

import java.util.ArrayList;
import java.util.Date;

public class Doctor {
    private String id;
    private String user_name;
    private String password;
    private String last_name;
    private String first_name;
    private Date birth_day;
    private String address;
    private int status;
    private String avatar;
    private long remain_money;
    private float current_rating;
    private ArrayList<Certification> certificates;
    private ArrayList<String> id_specialist;
    private String university_graduate;
    private int year_graduate;
    private String place_working;

    public Doctor(String id, String user_name, String password, String last_name, String first_name, Date birth_day, String address, int status, String avatar, long remain_money, float current_rating, ArrayList<Certification> certificates, ArrayList<String> id_specialist, String university_graduate, int year_graduate, String place_working) {
        this.id = id;
        this.user_name = user_name;
        this.password = password;
        this.last_name = last_name;
        this.first_name = first_name;
        this.birth_day = birth_day;
        this.address = address;
        this.status = status;
        this.avatar = avatar;
        this.remain_money = remain_money;
        this.current_rating = current_rating;
        this.certificates = certificates;
        this.id_specialist = id_specialist;
        this.university_graduate = university_graduate;
        this.year_graduate = year_graduate;
        this.place_working = place_working;
    }

    public Doctor() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public Date getBirth_day() {
        return birth_day;
    }

    public void setBirth_day(Date birth_day) {
        this.birth_day = birth_day;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getRemain_money() {
        return remain_money;
    }

    public void setRemain_money(long remain_money) {
        this.remain_money = remain_money;
    }

    public float getCurrent_rating() {
        return current_rating;
    }

    public void setCurrent_rating(float current_rating) {
        this.current_rating = current_rating;
    }

    public ArrayList<Certification> getCertificates() {
        return certificates;
    }

    public void setCertificates(ArrayList<Certification> certificates) {
        this.certificates = certificates;
    }

    public ArrayList<String> getId_specialist() {
        return id_specialist;
    }

    public void setId_specialist(ArrayList<String> id_specialist) {
        this.id_specialist = id_specialist;
    }

    public String getUniversity_graduate() {
        return university_graduate;
    }

    public void setUniversity_graduate(String university_graduate) {
        this.university_graduate = university_graduate;
    }

    public int getYear_graduate() {
        return year_graduate;
    }

    public void setYear_graduate(int year_graduate) {
        this.year_graduate = year_graduate;
    }

    public String getPlace_working() {
        return place_working;
    }

    public void setPlace_working(String place_working) {
        this.place_working = place_working;
    }
}
