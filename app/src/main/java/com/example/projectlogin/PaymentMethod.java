package com.example.projectlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PaymentMethod extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
    }

    public void Back_Payment(View view) {
        onBackPressed();
    }

    public void backToStore(View view) {
        startActivity(new Intent(this, MainUI.class));
    }
}