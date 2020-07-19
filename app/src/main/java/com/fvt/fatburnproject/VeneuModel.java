package com.fvt.fatburnproject;

import java.io.Serializable;

public class VeneuModel implements Serializable {
    String day;
    String time;
    String wbxTime;
    String venue;
    String personName;
    String phoneNumber;
    String totalSeats;

    String transectionId;
    String paymentId;

    boolean selected;

    boolean fbxEnabled;
    boolean wbxEnabled;

    String fbxSeats;
    String wbxSeats;

    public String getWbxTime() {
        return wbxTime;
    }

    public void setWbxTime(String wbxTime) {
        this.wbxTime = wbxTime;
    }

    public String getFbxSeats() {
        return fbxSeats;
    }

    public void setFbxSeats(String fbxSeats) {
        this.fbxSeats = fbxSeats;
    }

    public String getWbxSeats() {
        return wbxSeats;
    }

    public void setWbxSeats(String wbxSeats) {
        this.wbxSeats = wbxSeats;
    }

    public boolean isFbxEnabled() {
        return fbxEnabled;
    }

    public void setFbxEnabled(boolean fbxEnabled) {
        this.fbxEnabled = fbxEnabled;
    }

    public boolean isWbxEnabled() {
        return wbxEnabled;
    }

    public void setWbxEnabled(boolean wbxEnabled) {
        this.wbxEnabled = wbxEnabled;
    }

    String type;

    String id;



    boolean fbx;
    boolean wbx;

    public boolean isFbx() {
        return fbx;
    }

    public void setFbx(boolean fbx) {
        this.fbx = fbx;
    }

    public boolean isWbx() {
        return wbx;
    }

    public void setWbx(boolean wbx) {
        this.wbx = wbx;
    }

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

    public String getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(String totalSeats) {
        this.totalSeats = totalSeats;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getTransectionId() {
        return transectionId;
    }

    public void setTransectionId(String transectionId) {
        this.transectionId = transectionId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public VeneuModel() {
    }


    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
