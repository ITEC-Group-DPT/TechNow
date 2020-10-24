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

import static com.example.projectlogin.Cart.getCartArrList;

public class CartActivity extends AppCompatActivity {
    protected CartListViewAdapter adapter;
    private Intent intent;
    protected ListView lv_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            noOfItem = bundle.getInt("noItem");
        }
        initIntent();
    }

    private void initIntent() {
        DatabaseRef.getDatabaseReference().child("Cart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Cart.clearAll();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Product product = dataSnapshot.getValue(Product.class);
                    Cart.addItem(product);
                }
                adapter = new CartListViewAdapter(getBaseContext(), R.layout.cart_listview_layout, getCartArrList());
                adapter.setOnListenerInterface(new CartListViewAdapter.onListener() {
                    @Override
                    public void onListener() {
                        noOfItem--;
                        Log.d("@LOG", "noOfItem in CartActivity after remove: " + String.valueOf(noOfItem));
                    }
                });
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
            for (int i = 0; i < getCartArrList().size(); i++) {
                Product temp =  getCartArrList().get(i);
            }
            intent = new Intent(this, PaymentActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        Log.d("@LOG", "noOfItem in onBackPressed: " + noOfItem);
        returnIntent.putExtra("noItem", noOfItem);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    public void Back_cart(View view) {
        onBackPressed();
    }
}