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

        backgroundAnim();
        tv_username = findViewById(R.id.et_usn);
        et_pw = findViewById(R.id.et_pw);

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

    public void backgroundAnim() {
        scrollView = findViewById(R.id.scroll_view);
        AnimationDrawable animationDrawable = (AnimationDrawable) scrollView.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
    }

    public void onClick(View view) {
        //manageUser = new ManageUser(this);
        final String pass_str = et_pw.getText().toString();

/*        User user = new User(username, pass_str);
        if (manageUser.checkLoginUser(user) == true) {

            //intent.putExtra("username_edit", username);
            //intent.putExtra("noItem", noOfItem);

        }*/
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
}