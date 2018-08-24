package com.yd.yourdoctorandroid.networks.postPaymentHistory;

public class PaymentHistory {
    private String userID;
    private float amount;
    private float remainMoney;
    private String typeAdvisoryID;
    private String fromUser;
    private int status;

    public PaymentHistory(String userID, float amount, float remainMoney, String typeAdvisoryID, String fromUser ,int status) {
        this.userID = userID;
        this.amount = amount;
        this.remainMoney = remainMoney;
        this.typeAdvisoryID = typeAdvisoryID;
        this.fromUser = fromUser;
        this.status = status;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public double getRemainMoney() {
        return remainMoney;
    }

    public void setRemainMoney(float remainMoney) {
        this.remainMoney = remainMoney;
    }

    public String getTypeAdvisoryID() {
        return typeAdvisoryID;
    }

    public void setTypeAdvisoryID(String typeAdvisoryID) {
        this.typeAdvisoryID = typeAdvisoryID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    @Override
    public String toString() {
        return "PaymentHistory{" +
                "userID='" + userID + '\'' +
                ", amount=" + amount +
                ", remainMoney=" + remainMoney +
                ", typeAdvisoryID='" + typeAdvisoryID + '\'' +
                ", status=" + status +
                '}';
    }
}
