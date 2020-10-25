package com.example.projectlogin;

import java.util.ArrayList;

public class OrderHistory {
    private static int pos;
    private ArrayList<Cart> orderHistory = new ArrayList<>();

    public static int getPos() {
        return pos;
    }

    public static void setPos(int pos) {
        OrderHistory.pos = pos;
    }


    public ArrayList<Cart> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(ArrayList<Cart> orderHistory) {
        this.orderHistory = orderHistory;
    }

}
