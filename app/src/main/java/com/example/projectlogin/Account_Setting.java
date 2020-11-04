package com.example.projectlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class Account_Setting extends AppCompatActivity {

    private String username;
    private TextView username_TV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account__setting);

        initComponents();
    }

    private void initComponents() {
        username_TV = findViewById(R.id.username_TV);
        DatabaseRef.getDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username = snapshot.getKey();
                username_TV.setText(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final TextView noOfItemInCart = findViewById(R.id.number_of_item_in_cart);
        DatabaseRef.getDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int noOfItem = (int) snapshot.child("Cart").getChildrenCount();
                if (noOfItem == 0) {
                    noOfItemInCart.setVisibility(View.GONE);
                } else {
                    noOfItemInCart.setVisibility(View.VISIBLE);
                    noOfItemInCart.setText(String.valueOf(noOfItem));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        ImageButton cart_btn = findViewById(R.id.cart_btn);
        cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
            }
        });
    }

    public void backBtn(View view) {
        onBackPressed();
    }

    public void Account_onClick(View view) {
        Intent intent;
        switch (view.getId())
        {
            case (R.id.lnlo_order_history):
                intent = new Intent(this, OrderHistoryActivity.class);
                startActivity(intent);
                break;
            case(R.id.lnlo_favourite):
                intent = new Intent(this, FavoriteActivity.class);
                startActivity(intent);
                break;
            case(R.id.setting_feedback):
                intent = new Intent(this, FeedbackActivity.class);
                startActivity(intent);
                break;
            case(R.id.about_us):
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            case(R.id.setting):
                intent = new Intent(this, ReenterPassword.class);
                startActivity(intent);
                break;
            case(R.id.sign_out):
                AlertDialog.Builder confirmSignOutBuilder = new AlertDialog.Builder(this);
                final SharedPreferences sharedPreferences  = getSharedPreferences("rememberMe", MODE_PRIVATE);

                confirmSignOutBuilder.setTitle("Confirmation");
                confirmSignOutBuilder.setMessage("Do you want to sign out?");
                confirmSignOutBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(username, false);
                        editor.commit();
                        Intent intent1 = new Intent(getApplicationContext(), UserLogin.class);
                        startActivity(intent1);
                    }
                });
                confirmSignOutBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                confirmSignOutBuilder.create().show();
                break;
        }
    }
}