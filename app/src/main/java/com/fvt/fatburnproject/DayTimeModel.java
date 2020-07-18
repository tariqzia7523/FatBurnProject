package com.fvt.fatburnproject;

public class DayTimeModel {
    String day;
    String time;
    String seats;
    String delay;
    boolean fbx;
    boolean wbx;
    String fbxSeats;
    String wbxSeats;

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

    public String getDelay() {
        return delay;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }

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

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public DayTimeModel() {
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
