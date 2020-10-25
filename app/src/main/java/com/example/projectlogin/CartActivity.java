package com.example.projectlogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CartActivity extends AppCompatActivity {
    protected CartListViewAdapter adapter;
    private Intent intent;
    protected ListView lv_cart;
    private Cart cart = new Cart();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initIntent();
    }

    private void initIntent() {
        DatabaseRef.getDatabaseReference().child("Cart").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Product product = dataSnapshot.getValue(Product.class);
                        cart.addItem(product);
                    }
                }
                adapter = new CartListViewAdapter(getBaseContext(), R.layout.cart_listview_layout, cart.getCartArrList());
                lv_cart = findViewById(R.id.lv_cart);
                lv_cart.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void submit(View view) {
        if (adapter.getCount() == 0) {
            Toast.makeText(this,"There's nothing here",Toast.LENGTH_LONG).show();
        }
        else {
            intent = new Intent(this, PaymentActivity.class);
            startActivity(intent);
        }
    }

    public void Back_cart(View view) {
        onBackPressed();
    }
}