package com.example.projectlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReviewPurchasedProduct extends AppCompatActivity {

    private ImageView product_pic;
    private TextView product_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_purchased_product);

        product_pic = findViewById(R.id.product_pic);
        product_name = findViewById(R.id.product_name);

        final String p_name = getIntent().getExtras().getString("P_Name");
        product_name.setText(p_name);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    for (DataSnapshot _data: dataSnapshot.getChildren())
                    {
                        if (_data.child("name").getValue().equals(p_name)) {
                            Glide.with(ReviewPurchasedProduct.this).load(_data.child("avatarURL").getValue()).into(product_pic);
                            break;
                        }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void Back_review(View view) {
        onBackPressed();
    }
}