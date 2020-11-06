package com.example.projectlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class AddressBook extends AppCompatActivity {

    LinearLayout address_book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book);

        address_book = findViewById(R.id.address_book_lnlo);
        loadData();
    }

    private void loadData() {
        final DatabaseReference databaseReference = DatabaseRef.getDatabaseReference().child("Address book");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (final DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    View view = LayoutInflater.from(AddressBook.this).inflate(R.layout.address_book_template, address_book, false);
                    TextView name_tv = view.findViewById(R.id.ab_name);
                    TextView phone_tv = view.findViewById(R.id.ab_phonenum);
                    TextView address_tv = view.findViewById(R.id.ab_address);

                    name_tv.setText((CharSequence) dataSnapshot.child("Name").getValue());
                    phone_tv.setText((CharSequence) dataSnapshot.child("Phone Number").getValue());
                    address_tv.setText((CharSequence) dataSnapshot.child("Address").getValue());

                    ImageButton imageButton = view.findViewById(R.id.delete_item_ab);
                    imageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            databaseReference.child(dataSnapshot.getKey()).removeValue();
                            address_book.removeAllViews();
                            loadData();
                        }
                    });

                    address_book.addView(view);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void Back_btn(View view) {
        onBackPressed();
    }

    public void backToStore(View view) {
        startActivity(new Intent(this, MainUI.class));
    }
}