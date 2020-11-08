package com.example.projectlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ReenterPassword extends AppCompatActivity {
    private ScrollView scrollView;
    private String username;
    private com.google.android.material.textfield.TextInputEditText tv_username;
    private com.google.android.material.textfield.TextInputEditText et_pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reenter_password);

        tv_username = findViewById(R.id.et_usn);
        et_pw = findViewById(R.id.et_pw);
        scrollView = findViewById(R.id.scroll_view);

        DatabaseRef.getDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username = snapshot.getKey();
                tv_username.setHint(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    public void onClick(View view) {
        final String pass_str = et_pw.getText().toString();

        DatabaseRef.getDatabaseReference().child("Information").child("password").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String passwordDTB = (String) snapshot.getValue();
                if (pass_str.equals(passwordDTB)) {
                    Intent intent = new Intent(getApplicationContext(), EditAccount.class);
                    startActivity(intent);
                } else
                    Toast.makeText(ReenterPassword.this, "Incorrect password", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void backBtn(View view) {
        onBackPressed();
    }
}