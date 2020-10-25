package com.example.projectlogin;

public class Product {
    private String type;
    private String avatarURL;
    private String name;
    private int price;
    private int quantity = 1;
    private int sold;

    public Product() {}

    public Product(String avatarURL, String name, int price) {
        this.avatarURL = avatarURL;
        this.name = name;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public void setQuantity(int _quantity) {
        this.quantity = _quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }
}
