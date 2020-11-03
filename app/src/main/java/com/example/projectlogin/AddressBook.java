package com.example.projectlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class AddressBook extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book);
    }

    public void Back_btn(View view) {
        onBackPressed();
    }

    public void delete_item(View view) {
    }
}