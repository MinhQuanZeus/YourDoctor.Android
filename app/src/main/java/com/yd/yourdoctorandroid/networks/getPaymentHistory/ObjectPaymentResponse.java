package com.yd.yourdoctorandroid.networks.getPaymentHistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ObjectPaymentResponse {
    @SerializedName("_id")
    @Expose
    private String id;
    private String userID;
    private float amount;
    private float remainMoney;
    private FromUser fromUser;
    private TypeAdvisoryResponse typeAdvisoryID;
    private int status;
    private String updatedAt;
    private String createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getAmount() {
        return (int) Math.round(amount);
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getRemainMoney() {
        return (int) Math.round(remainMoney);
    }

    public void setRemainMoney(float remainMoney) {
        this.remainMoney = remainMoney;
    }

    public FromUser getFromUser() {
        return fromUser;
    }

    public void setFromUser(FromUser fromUser) {
        this.fromUser = fromUser;
    }

    public TypeAdvisoryResponse getTypeAdvisoryID() {
        return typeAdvisoryID;
    }

    public void setTypeAdvisoryID(TypeAdvisoryResponse typeAdvisoryID) {
        this.typeAdvisoryID = typeAdvisoryID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
