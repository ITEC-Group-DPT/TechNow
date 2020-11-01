package com.example.projectlogin;

import java.util.ArrayList;

public class OrderHistory {
    private ArrayList<Cart> orderHistory = new ArrayList<>();
    private String ID;

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