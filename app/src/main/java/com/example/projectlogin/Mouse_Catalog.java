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

public class Mouse_Catalog extends MainLayout {

    private ArrayList<Product> Products;
    private ListView lv_Products;
    private ProductListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_catalog, null, false);
        linearLayout.addView(contentView, 1);
        Log.d("@LOG", "back to onCreate MOUSE");

        cart_btn = findViewById(R.id.cart_btn);

        drawerLayout.setBackgroundColor(0xFFFFFF);
        navigationView.setCheckedItem(R.id.mouse);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            username = bundle.getString("name");
            noOfItem = bundle.getInt("noItem");
            View newview = navigationView.getHeaderView(0);
            tv_username = newview.findViewById(R.id.username);
            tv_username.setText(username);
            if (noOfItem != 0) {
                noOfItemInCart.setVisibility(View.VISIBLE);
                noOfItemInCart.setText(String.valueOf(noOfItem));
            } else noOfItemInCart.setVisibility(View.GONE);
        }

        changeToolbarTitle("Mouse");
        loadData();
        initComponents();


    }



    private void loadData() {
      /*  int[] avatarIDs = {R.drawable.acer_predator_cestus_310, R.drawable.asus_rog_gladius_ii_core, R.drawable.corsair_ironclaw_rgb, R.drawable.corsair_scimitar_rgb_elite,
                R.drawable.logitech_g403_hero, R.drawable.logitech_g703_hero_lightspeed_wireless, R.drawable.razer_deathadder_v2, R.drawable.razer_mamba_wireless,
                R.drawable.steelseries_rival_3, R.drawable.zowie_benq_ec2_v2};
        String[] _name = {"Acer Predator Cestus 310", "Asus Rog Gladius II Core", "Corsair Ironclaw RGB", "Corsair Scimitar RGB Elite",
                "Logitech G403 Hero", "Logitech G703 Hero Lightspeed Wireless", "Razer Deathadder V2", "Razer Mamba Wireless", "Steelseries Rival 3",
                "Zowie BenQ EC2 V2"};
        int[] _price = {1750000, 1090000, 1590000, 2090000, 1190000, 2090000, 1690000, 2390000, 890000, 1690000};
        Products = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            Product temp = new Product(BitmapFactory.decodeResource(getResources(), avatarIDs[i], options), _name[i], _price[i]);
            Products.add(temp);
        }*/
    }

    private void initComponents() {
        adapter = new ProductListViewAdapter(this, R.layout.product_listview_layout, Products);
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
        lv_Products = findViewById(R.id.catalog_lv);
        lv_Products.setAdapter(adapter);

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