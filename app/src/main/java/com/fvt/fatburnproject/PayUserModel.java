package com.fvt.fatburnproject;

import java.io.Serializable;

public class PayUserModel implements Serializable {
    String id;
    String name;
    String phoneNumber;
    String paymentID;
    String transectionid;

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getTransectionid() {
        return transectionid;
    }

    public void setTransectionid(String transectionid) {
        this.transectionid = transectionid;
    }

    public PayUserModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
