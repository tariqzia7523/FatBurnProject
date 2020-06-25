package com.fvt.fatburnproject;

public class AdminOnlineClassModel {
    String id;
    String price;
    String title;


    public AdminOnlineClassModel( String price, String title) {
        this.price = price;
        this.title = title;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AdminOnlineClassModel() {
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String Title) {
        this.title = Title;
    }


}
