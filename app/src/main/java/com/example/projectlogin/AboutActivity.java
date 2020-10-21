package com.example.projectlogin;

import android.os.Bundle;
import android.view.View;

public class AboutActivity extends MainLayout {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("About TechNow");
    }

    public void Back_cart(View view) {
        onBackPressed();
    }
}