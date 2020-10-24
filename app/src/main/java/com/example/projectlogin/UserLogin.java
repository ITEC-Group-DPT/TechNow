package com.example.projectlogin;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class UserLogin extends AppCompatActivity {
    private com.google.android.material.textfield.TextInputEditText et_usn;
    private com.google.android.material.textfield.TextInputEditText et_pw;
    private CheckBox rememberMe;
    private ScrollView scrollView;
    private DatabaseReference databaseRef;
    public static final String SHARED_PREFS = "rememberMe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Map<String, ?> keys = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            if (entry.getValue().equals(true)) {
                Log.d("!LOG", "sharedpreferences TRUE");
                String username = entry.getKey();
                Intent intent = new Intent(getApplicationContext(), MainUI.class);
                DatabaseRef.setDatabaseReference(FirebaseDatabase.getInstance().getReference("Users").child(username));
                startActivity(intent);
                return;
            }
        }

        setContentView(R.layout.activity_user_login);
        backgroundAnim();

        et_usn = findViewById(R.id.et_usn);
        et_pw = findViewById(R.id.et_pw);
        rememberMe = findViewById(R.id.checkbox_rememberme);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.btnLogin):
                final String name_str = et_usn.getText().toString();
                String pass_str = et_pw.getText().toString();
                final User user = new User(name_str, pass_str);

                databaseRef = FirebaseDatabase.getInstance().getReference("Users");

                databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(user.getUsername()).exists()) {
                            String account_pass = (String) snapshot.child(name_str).child("Information").child("password").getValue();

                            if (account_pass.equals(user.getPassword())) {
                                if (rememberMe.isChecked()) {
                                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean(user.getUsername(), true);
                                    editor.commit();

                                } else {
                                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean(user.getUsername(), false);
                                    editor.commit();
                                }
                                DatabaseRef.setDatabaseReference(FirebaseDatabase.getInstance().getReference("Users").child(user.getUsername()));
                                Intent intent1 = new Intent(getApplicationContext(), MainUI.class);
                                startActivity(intent1);
                                return;
                            }

                        }
                        Toast.makeText(UserLogin.this, "Incorrect username or password", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                break;
            case (R.id.tvSignUp):
                Intent intent2 = new Intent(getApplicationContext(), UserSignUp.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, findViewById(R.id.tvSignUp), "trans_signup");
                startActivity(intent2, options.toBundle());
                break;
        }
    }

    public void backgroundAnim() {
        scrollView = findViewById(R.id.scrollView);
        AnimationDrawable animationDrawable = (AnimationDrawable) scrollView.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
    }

    @Override
    public void onBackPressed() {
    }
}