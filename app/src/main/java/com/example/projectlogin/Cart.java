package com.example.projectlogin;

import java.util.ArrayList;

public class Cart {
    private ArrayList<Product> CartArrList = new ArrayList<>();
    private int totalCash = 0;
    private int ID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getTotalCash() {
        return totalCash;
    }

    public void setTotalCash(int _totalCash) {
        totalCash = _totalCash;
    }

    public int calTotalCash() {
        int _totalCash = 0;
        for (int i = 0; i < CartArrList.size(); i++) {
            _totalCash += CartArrList.get(i).getPrice() * CartArrList.get(i).getQuantity();
        }
        return _totalCash;
    }

    public ArrayList<Product> getCartArrList() {
        return CartArrList;
    }

    public int getNoOfItem() {
        return CartArrList.size();
    }

    public void setCartArrList(ArrayList<Product> _cartArrList) {
        CartArrList = _cartArrList;
    }

    public void addItem(Product item) {
        CartArrList.add(item);
    }

    public void clearAll() {
        CartArrList.clear();
    }

    Cart() {
    }
}