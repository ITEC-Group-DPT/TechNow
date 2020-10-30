package com.example.projectlogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class CartActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    LinearLayout emptyCart_lnlo;
    ImageView emptyCartIV;
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
        linearLayout = findViewById(R.id.lnlo);
        emptyCart_lnlo = findViewById(R.id.empty_cart);
        emptyCartIV = findViewById(R.id.empty_cart_IV);
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
        emptyCart_lnlo.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);
        Glide.with(CartActivity.this).load("https://firebasestorage.googleapis.com/v0/b/technow-4b3ab.appspot.com/o/UI%2Femptycart.webp?alt=media&token=94afe345-b44e-4b80-9d60-3e5a2df2d0d0").into(emptyCartIV);
    }

    public void CartWithItemUI() {
        emptyCart_lnlo.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);
    }

    public void submit(View view) {
        intent = new Intent(this, PaymentActivity.class);
        startActivity(intent);
    }

    public void Back_cart(View view) {
        onBackPressed();
    }
}