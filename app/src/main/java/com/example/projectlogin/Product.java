package com.example.projectlogin;

import android.graphics.Bitmap;

public class Product {
    private String avatarURL;
    private String name;
    private int price;
    //private int quantity = 1;

    public Product() {};

    public Product(String avatarURL, String name, int price) {
        this.avatarURL = avatarURL;
        this.name = name;
        this.price = price;
    }
    public String getAvatarURL() {
        return avatarURL;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }


   /* public void setQuantity(int _quantity) {
        this.quantity = _quantity;
    }*/

    /*public int getQuantity() {
        return quantity;
    }*/

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
