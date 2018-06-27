package com.yd.yourdoctorandroid.networks.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    private String message;
    @SerializedName("jwt_token")
    @Expose
    private String jwtToken;
    @SerializedName("user")
    @Expose
    private Patient patient;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "message='" + message + '\'' +
                ", jwtToken='" + jwtToken + '\'' +
                ", patient=" + patient.toString() +
                '}';
    }

    public AuthResponse(String message, String jwtToken, Patient patient) {

        this.message = message;
        this.jwtToken = jwtToken;
        this.patient = patient;
    }
}
