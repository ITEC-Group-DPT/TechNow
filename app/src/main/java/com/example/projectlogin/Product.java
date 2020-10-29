package com.example.projectlogin;

public class Product {
    private String name;
    private String type;
    private String avatarURL, avatarURL1, avatarURL2, avatarURL3, avatarURL4;
    private int price;
    private int quantity = 1;
    private int sold;
    private String desc;
    private String detail;
    private String OrderID;

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public Product() {
    }

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

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public String getAvatarURL1() {
        return avatarURL1;
    }

    public void setAvatarURL1(String avatarURL1) {
        this.avatarURL1 = avatarURL1;
    }

    public String getAvatarURL2() {
        return avatarURL2;
    }

    public void setAvatarURL2(String avatarURL2) {
        this.avatarURL2 = avatarURL2;
    }

    public String getAvatarURL3() {
        return avatarURL3;
    }

    public void setAvatarURL3(String avatarURL3) {
        this.avatarURL3 = avatarURL3;
    }

    public String getAvatarURL4() {
        return avatarURL4;
    }

    public void setAvatarURL4(String avatarURL4) {
        this.avatarURL4 = avatarURL4;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
