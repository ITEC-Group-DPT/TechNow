package com.example.projectlogin;

import java.util.ArrayList;

public class Cart {
    private static ArrayList<Product> CartArrList = new ArrayList<>();

    public static int getTotalCash() {
        return totalCash;
    }

    public static void setTotalCash(int _totalCash) {
        Cart.totalCash = _totalCash;
    }

    private static int totalCash = 0;

    public static ArrayList<Product> getCartArrList() {
        return CartArrList;
    }
    public static int getNoOfItem() {
        return CartArrList.size();
    }
    public static void setCartArrList(ArrayList<Product> _cartArrList) {
        CartArrList = _cartArrList;
    }

    public static void addItem(Product item){
        CartArrList.add(item);
    }
    public static void clearAll() {CartArrList.clear();}
    Cart() {};
}