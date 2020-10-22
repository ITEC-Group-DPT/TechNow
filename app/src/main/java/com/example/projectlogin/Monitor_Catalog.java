package com.example.projectlogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Monitor_Catalog extends MainLayout {

    ArrayList<Product> monitorList;
    ListView screen_lv;
    DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_catalog, null, false);
        linearLayout.addView(contentView, 1);

        cart_btn.findViewById(R.id.cart_btn);
        drawerLayout.setBackgroundColor(0xFFFFFF);
        navigationView.setCheckedItem(R.id.screen);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Log.d("@LOG", "bundle yes");
            username = bundle.getString("name");
            noOfItem = bundle.getInt("noItem");
            View newview = navigationView.getHeaderView(0);
            tv_username = newview.findViewById(R.id.username);
            tv_username.setText(username);
            if (noOfItem != 0) {
                noOfItemInCart.setText(String.valueOf(noOfItem));
            } else noOfItemInCart.setVisibility(View.GONE);
        }

        changeToolbarTitle("Monitor");
        loadData();
    }
    private void loadData() {
        monitorList = new ArrayList<>();
        databaseRef = FirebaseDatabase.getInstance().getReference("Products").child("Monitor");
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Product product = postSnapshot.getValue(Product.class);
                    monitorList.add(product);
                }

                ProductListViewAdapter adapter = new ProductListViewAdapter(Monitor_Catalog.this, R.layout.product_listview_layout, monitorList);
                adapter.setOnAddtoCartInterface(new ProductListViewAdapter.onAddToCart() {
                    @Override
                    public void onAddToCart(ImageButton imageButtonAddToCart, int number) {
                        imageButtonAddToCart.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.icon_add_to_cart));
                        cart_btn.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.icon_shake));

                        noOfItem = number;
                        noOfItemInCart.setVisibility(View.VISIBLE);
                        noOfItemInCart.setText(String.valueOf(noOfItem));
                    }
                });
                screen_lv = findViewById(R.id.catalog_lv);
                screen_lv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainUI.class);
        Log.d("@LOG", "onBackPressed: " + String.valueOf(noOfItem));
        intent.putExtra("noItem", noOfItem);
        intent.putExtra("username", username);
        startActivity(intent);
    }


}