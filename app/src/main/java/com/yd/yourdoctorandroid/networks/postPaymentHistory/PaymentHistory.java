package com.yd.yourdoctorandroid.networks.postPaymentHistory;

public class PaymentHistory {
    private String userID;
    private long amount;
    private long remainMoney;
    private String typeAdvisoryID;
    private int status;

    public PaymentHistory(String userID, long amount, long remainMoney, String typeAdvisoryID, int status) {
        this.userID = userID;
        this.amount = amount;
        this.remainMoney = remainMoney;
        this.typeAdvisoryID = typeAdvisoryID;
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

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public double getRemainMoney() {
        return remainMoney;
    }

    public void setRemainMoney(long remainMoney) {
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
