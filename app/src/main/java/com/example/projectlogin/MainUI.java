package com.example.projectlogin;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.core.view.GravityCompat;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

public class MainUI extends MainLayout {
    private int[] sampleImages = {R.drawable.carousel_image_0, R.drawable.carousel_image_1,
            R.drawable.carousel_image_2, R.drawable.carousel_image_3, R.drawable.carousel_image_4};
    private CarouselView carouselView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_main_u_i, null, false);
        linearLayout.addView(contentView, 1);
        navigationView.setCheckedItem(R.id.nav_home);

        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            username = bundle.getString("username");
        } else {
            username = savedInstanceState.getString("name");
            Log.d("@LOG", "MainActivity has savedInstance: " + username);
        }

        Log.d("@LOG", "NoItem onCreate MainUI before bundle: " + String.valueOf(noOfItem));
        Bundle bundle = getIntent().getExtras();
        noOfItem = bundle.getInt("noItem");
        Log.d("@LOG", "NoItem onCreate MainUI after bundle: " + String.valueOf(noOfItem));

        if(noOfItem == 0) noOfItemInCart.setVisibility(View.GONE);
        else {
            noOfItemInCart.setVisibility(View.VISIBLE);
            noOfItemInCart.setText(String.valueOf(noOfItem));
        }
        View newview = navigationView.getHeaderView(0);
        tv_username = newview.findViewById(R.id.username);
        tv_username.setText(username);

        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(sampleImages[position]);
            }
        };

        carouselView.setImageListener(imageListener);

        carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent;
                ActivityOptions options;
                switch (position) {
                    case 0:
                        intent = new Intent(MainUI.this, Mouse_Catalog.class);
                        intent.putExtra("name", username);
                        intent.putExtra("noItem", noOfItem);
                        options = ActivityOptions.makeSceneTransitionAnimation(MainUI.this, findViewById(R.id.carouselView), "trans_catalog");
                        startActivity(intent, options.toBundle());
                        break;
                    case 1:
                        intent = new Intent(MainUI.this, Keyboard_Catalog.class);
                        intent.putExtra("name", username);
                        intent.putExtra("noItem", noOfItem);
                        options = ActivityOptions.makeSceneTransitionAnimation(MainUI.this, findViewById(R.id.carouselView), "trans_catalog");
                        startActivity(intent, options.toBundle());
                        break;
                    case 2:
                        intent = new Intent(MainUI.this, Monitor_Catalog.class);
                        intent.putExtra("name", username);
                        intent.putExtra("noItem", noOfItem);
                        options = ActivityOptions.makeSceneTransitionAnimation(MainUI.this, findViewById(R.id.carouselView), "trans_catalog");
                        startActivity(intent, options.toBundle());
                        break;
                    case 3:
                        intent = new Intent(MainUI.this, Laptop_Catalog.class);
                        intent.putExtra("name", username);
                        intent.putExtra("noItem", noOfItem);
                        options = ActivityOptions.makeSceneTransitionAnimation(MainUI.this, findViewById(R.id.carouselView), "trans_catalog");
                        startActivity(intent, options.toBundle());
                        break;

                    case 4:
                        break;
                }

            }
        });
    }


    public void Card_onClick(View view) {
        Intent intent;
        ActivityOptions options;
        switch (view.getId()) {
            case (R.id.keyboard_cv):
                intent = new Intent(this, Keyboard_Catalog.class);
                intent.putExtra("name", username);
                intent.putExtra("noItem", noOfItem);
                options = ActivityOptions.makeSceneTransitionAnimation(this, findViewById(R.id.keyboard_cv), "trans_catalog");
                startActivity(intent, options.toBundle());
                break;
            case (R.id.mouse_cv):
                intent = new Intent(this, Mouse_Catalog.class);
                intent.putExtra("name", username);
                intent.putExtra("noItem", noOfItem);
                options = ActivityOptions.makeSceneTransitionAnimation(this, findViewById(R.id.mouse_cv), "trans_catalog");
                startActivity(intent, options.toBundle());
                break;
            case (R.id.screen_cv):
                intent = new Intent(this, Monitor_Catalog.class);
                intent.putExtra("name", username);
                intent.putExtra("noItem", noOfItem);
                options = ActivityOptions.makeSceneTransitionAnimation(this, findViewById(R.id.screen_cv), "trans_catalog");
                startActivity(intent, options.toBundle());
                break;
            case (R.id.laptop_cv):
                intent = new Intent(this, Laptop_Catalog.class);
                intent.putExtra("name", username);
                intent.putExtra("noItem", noOfItem);
                options = ActivityOptions.makeSceneTransitionAnimation(this, findViewById(R.id.laptop_cv), "trans_catalog");
                startActivity(intent, options.toBundle());
                break;
        }

    }

    @Override
    public void onBackPressed() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }
}