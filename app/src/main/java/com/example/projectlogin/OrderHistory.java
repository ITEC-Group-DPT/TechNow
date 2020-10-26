package com.example.projectlogin;

import java.util.ArrayList;

public class OrderHistory {
    private ArrayList<Cart> orderHistory = new ArrayList<>();



    public ArrayList<Cart> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(ArrayList<Cart> orderHistory) {
        this.orderHistory = orderHistory;
    }

}
