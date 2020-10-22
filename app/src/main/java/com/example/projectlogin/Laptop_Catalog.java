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

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Laptop_Catalog extends MainLayout {

    private ArrayList<Product> laptopList;
    private ListView listView;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_catalog, null, false);

        cart_btn.findViewById(R.id.cart_btn);
        linearLayout.addView(contentView, 1);
        navigationView.setCheckedItem(R.id.laptop);
        drawerLayout.setBackgroundColor(0xFFFFFF);

        listView = findViewById(R.id.catalog_lv);

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

        changeToolbarTitle("Laptop");
        loadData();
        //initComponents();

    }


   /* private void initComponents() {
        ProductListViewAdapter adapter = new ProductListViewAdapter(this, R.layout.product_listview_layout, listProduct);
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
        listView.setAdapter(adapter);
    }*/

    private void loadData() {
       /* listProduct = new ArrayList<Product>();
        int[] image = {R.drawable.acerswift3, R.drawable.aspire5, R.drawable.dellf3, R.drawable.hp13, R.drawable.macair13,
                R.drawable.nitro5, R.drawable.pro2019, R.drawable.rogstrixg, R.drawable.vivobooks15, R.drawable.yogas740};
        String[] name = {"Acer Swift 3", "Acer Aspire 5", "Dell G3 15", "HP 14s", "Apple MacBook Air 13",
                "Acer Nitro 5", "Apple MacBook Pro 2019", "Asus ROG Strix G", "Asus Vivobook S15", "Lenovo Yoga S740"};
        int[] price = {13000000, 15000000, 24000000, 18000000, 32000000, 16000000, 45000000, 30000000, 14000000, 17000000};

        for (int i = 0; i < image.length; i++) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;

            listProduct.add(new Product(BitmapFactory.decodeResource(getResources(), image[i], options), name[i], price[i]));
        }*/
        laptopList = new ArrayList<>();
        databaseRef = FirebaseDatabase.getInstance().getReference("Products").child("Laptop");
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Product product = postSnapshot.getValue(Product.class);
                    laptopList.add(product);
                }

                ProductListViewAdapter adapter = new ProductListViewAdapter(Laptop_Catalog.this, R.layout.product_listview_layout, laptopList);
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
                listView = findViewById(R.id.catalog_lv);
                listView.setAdapter(adapter);
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