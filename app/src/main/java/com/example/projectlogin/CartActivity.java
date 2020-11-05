package com.example.projectlogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class CartActivity extends AppCompatActivity {
    private static TextView tv_totalCash;
    private LinearLayout linearLayout;
    private ConstraintLayout emptyCart_cslo;
    private ImageView emptyCartIV;
    protected CartListViewAdapter adapter;
    private Intent intent;
    protected ListView lv_cart;
    private Cart cart = new Cart();
    private static NumberFormat format = new DecimalFormat("#,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initIntent();
    }

    private void initIntent() {
        linearLayout = findViewById(R.id.lnlo);
        emptyCart_cslo = findViewById(R.id.empty_cart);
        emptyCartIV = findViewById(R.id.empty_cart_IV);
        tv_totalCash = findViewById(R.id.tv_totalCash);
        DatabaseRef.getDatabaseReference().child("Cart").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Product product = dataSnapshot.getValue(Product.class);
                        cart.addItem(product);
                    }
                }
                setTV_totalCash(cart.calTotalCash());
                adapter = new CartListViewAdapter(getBaseContext(), R.layout.cart_listview_layout, cart.getCartArrList());
                adapter.setOnRemoveItemFromCart(new CartListViewAdapter.onRemoveItemFromCart() {
                    @Override
                    public void onRemoveItemFromCart() {
                        if (adapter.getCount() == 0) {
                            EmptyCartUI();
                        } else {
                            CartWithItemUI();
                        }
                    }
                });
                lv_cart = findViewById(R.id.lv_cart);
                lv_cart.setAdapter(adapter);
                if (adapter.getCount() == 0) {
                    EmptyCartUI();
                } else {
                    CartWithItemUI();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void EmptyCartUI() {
        emptyCart_cslo.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);
    }

    public void CartWithItemUI() {
        emptyCart_cslo.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);
    }

    public void submit(View view) {
        intent = new Intent(this, PaymentActivity.class);
        startActivity(intent);
    }

    public void Back_cart(View view) {
        onBackPressed();
    }

    public static void setTV_totalCash(int _totalCash) {
        String formattedTotal = format.format(_totalCash) + "₫";
        formattedTotal = formattedTotal.replace(',', '.');
        tv_totalCash.setText(formattedTotal);
        ;
    }
}