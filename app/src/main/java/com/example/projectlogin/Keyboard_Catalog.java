package com.example.projectlogin;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class Keyboard_Catalog extends MainLayout {

    private ArrayList<Product> keyboards;
    private ListView keyboard_lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_catalog, null, false);

        linearLayout.addView(contentView, 1);
        cart_btn = findViewById(R.id.cart_btn);
        navigationView.setCheckedItem(R.id.keyboard);
        drawerLayout.setBackgroundColor(0xFFFFFF);

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

        changeToolbarTitle("Keyboard");
        loadData();
        initComponents();
    }


    private void initComponents() {
        ProductListViewAdapter adapter = new ProductListViewAdapter(this, R.layout.product_listview_layout, keyboards);
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
        keyboard_lv = findViewById(R.id.catalog_lv);
        keyboard_lv.setAdapter(adapter);
    }

    private void loadData() {
      /*  String[] name = {"DareU EK810 Queen", "AKKO 3108 v2", "E-Dra EK387 Pro Cherry Switch",
                "Acer Predator Aethon", "AKKO Dragon Ball Super", "Mistel Sleeker X-VIII Gloaming",
                "Akko Designer Studio MOD001", "AKKO 3087 Midnight", "E-DRA EK3087", "Leopold FC900RPD"};
        int[] prices = {640000, 1690000, 1350000, 3290000, 2490000, 2650000, 4050000, 890000, 499000, 3350000};
        int[] image = {R.drawable.dareu_ek810_queen, R.drawable.akko_3108_v2, R.drawable.edra_ek387_pro_cherryswitch,
                R.drawable.acer_predator_aethon_300, R.drawable.akko_dragonball_super,
                R.drawable.mistel_sleeker_gloaming, R.drawable.akko_designer_studio, R.drawable.akko_3087_midnight,
                R.drawable.edra_ek3087, R.drawable.leopold_fc900rpd,};

        keyboards = new ArrayList<>();
        for (int i = 0; i < name.length; i++) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;

            Product keyboard = new Product(BitmapFactory.decodeResource(getResources(), image[i], options), name[i], prices[i]);
            keyboards.add(keyboard);
        }*/
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