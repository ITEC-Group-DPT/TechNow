package com.example.projectlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.Comparator;

public class OrderHistoryActivity extends AppCompatActivity {
    private OrderHistoryAdapter adapter;
    private ListView lv_OrderHistory;
    //private OrderHistory orderHistory = new OrderHistory();
    private Cart cart = new Cart();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        initComponent();
    }

    private void initComponent() {
        DatabaseRef.getDatabaseReference().child("Order History").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot _dataSnapshot : dataSnapshot.getChildren()) {
                            Product product = _dataSnapshot.getValue(Product.class);
                            product.setOrderID(dataSnapshot.getKey());
                            cart.addItem(product);
                        }
                        cart.setID(dataSnapshot.getKey());
                        Collections.sort(cart.getCartArrList(), new Comparator<Product>() {
                            public int compare(Product p1, Product p2) {
                                return p2.getOrderID().compareTo(p1.getOrderID());
                            }
                        });
                        //orderHistory.addItem(cart);
                    }
                }
                adapter = new OrderHistoryAdapter(getBaseContext(), R.layout.order_history_layout, cart.getCartArrList());
                lv_OrderHistory = findViewById(R.id.lv_order_history);
                lv_OrderHistory.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}