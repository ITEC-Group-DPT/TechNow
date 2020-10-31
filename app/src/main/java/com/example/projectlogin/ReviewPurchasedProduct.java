package com.example.projectlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReviewPurchasedProduct extends AppCompatActivity {

    private ImageView product_pic;
    private TextView product_name;
    private TextInputEditText feedback_text;
    private RatingBar ratingBar;
    private TextView ratingText;
    private String itemType;
    private String p_name;
    private float Rate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_purchased_product);

        product_pic = findViewById(R.id.product_pic);
        product_name = findViewById(R.id.product_name);
        ratingBar = findViewById(R.id.ratingBar);
        ratingText = findViewById(R.id.rating_text);
        feedback_text = findViewById(R.id.feedback_text);
        p_name = getIntent().getExtras().getString("P_Name");
        product_name.setText(p_name);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                int rate = (int) v;
                switch (rate) {
                    case 1:
                        ratingText.setText("Awful");
                        Rate = 1;
                        break;
                    case 2:
                        ratingText.setText("Bad");
                        Rate = 2;
                        break;
                    case 3:
                        ratingText.setText("Not Good");
                        Rate = 3;
                        break;
                    case 4:
                        ratingText.setText("Very Good");
                        Rate = 4;
                        break;
                    case 5:
                        ratingText.setText("Perfect");
                        Rate = 5;
                        break;
                }
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    for (DataSnapshot _data : dataSnapshot.getChildren()) {
                        if (_data.child("name").getValue().equals(p_name)) {
                            itemType = dataSnapshot.getKey();
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

    public void pic_onClikc(View view) {
        Intent intent = new Intent(this, ItemDetail.class);
        intent.putExtra("itemName", product_name.getText().toString());
        intent.putExtra("itemType", itemType);
        startActivity(intent);
    }

    public void submitFeedback(View view) {
        if (ratingBar.getRating() == 0) {
            Toast.makeText(this, "Please rate us", Toast.LENGTH_SHORT).show();
        } else if (feedback_text.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill-in your feedback", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Your review has been recorded!");
            builder.setMessage("Thank you for your review!");
            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                for (DataSnapshot _data : dataSnapshot.getChildren()) {
                                    if (_data.child("name").getValue().equals(p_name)) {
                                        Product temp  = _data.getValue(Product.class);
                                        String key = _data.getKey();
                                        temp.setRateNo(temp.getRateNo() + 1);
                                        float tempRate = temp.getRating() + Rate;
                                        temp.setRating(tempRate / temp.getRateNo());
                                        databaseReference.child(temp.getType()).child(key).child(temp.getName()).child("rateNo").setValue(temp.getRateNo());
                                        databaseReference.child(temp.getType()).child(key).child(temp.getName()).child("rating").setValue(temp.getRating());
                                        break;
                                    }
                                }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    ratingText.setText("");
                    feedback_text.setText("");
                    ratingBar.setRating(0);
                    onBackPressed();
                }
            });
            AlertDialog showNote = builder.create();
            showNote.show();
        }
    }
}