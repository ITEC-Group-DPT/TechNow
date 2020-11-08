package com.example.projectlogin;

import java.util.ArrayList;

public class OrderHistory {
    private ArrayList<Cart> orderHistory = new ArrayList<>();
    private String ID;
    private String cus_name;
    private String cus_phonenum;
    private String cus_address;
    private String datetime;

    public String getCus_name() {
        return cus_name;
    }

    public void setCus_name(String cus_name) {
        this.cus_name = cus_name;
    }

    public String getCus_phonenum() {
        return cus_phonenum;
    }

    public void setCus_phonenum(String cus_phonenum) {
        this.cus_phonenum = cus_phonenum;
    }

    public String getCus_address() {
        return cus_address;
    }

    public void setCus_address(String cus_address) {
        this.cus_address = cus_address;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public ArrayList<Cart> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(ArrayList<Cart> orderHistory) {
        this.orderHistory = orderHistory;
    }

    public void addItem(Cart item){
        orderHistory.add(item);
    }
}