package com.example.projectlogin;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserSignUp extends AppCompatActivity {
    private ScrollView scrollView;
    private com.google.android.material.textfield.TextInputEditText et_usn;
    private com.google.android.material.textfield.TextInputEditText et_pw;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        backgroundAnim();
        et_usn = findViewById(R.id.et_usn);
        et_pw = findViewById(R.id.et_pw);
    }

    private class AsyncTaskSignUp extends AsyncTask<User, String, String> {
        @Override
        protected String doInBackground(User... users) {
            final User user = users[0];
            databaseRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUsername());
            databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        databaseRef.child("Information").setValue(user);
                        Log.d("!LOG", "intent USERLOGIN");
                        Intent intent1 = new Intent(getApplicationContext(), UserLogin.class);
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(UserSignUp.this, findViewById(R.id.signup_btn), "trans_login");
                        startActivity(intent1, options.toBundle());
                    }
                    else {
                        onPostExecute(user.getUsername());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Button button = findViewById(R.id.signup_btn);
            ProgressBar progressBar = findViewById(R.id.progress_signup);
            button.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String command) {
            super.onPostExecute(command);
            if (command != null) {
                Toast.makeText(UserSignUp.this, "There's already a user with the username " + "'" + command + "'", Toast.LENGTH_SHORT).show();
                Button button = findViewById(R.id.signup_btn);
                ProgressBar progressBar = findViewById(R.id.progress_signup);
                button.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.signup_btn):
                String name_str = et_usn.getText().toString();
                String pass_str = et_pw.getText().toString();
                final User user = new User(name_str, pass_str);
                new AsyncTaskSignUp().execute(user);
                /*databaseRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUsername());
                databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (!snapshot.exists()) {
                            databaseRef.child("Information").setValue(user);
                            Log.d("!LOG", "intent USERLOGIN");
                            Intent intent1 = new Intent(getApplicationContext(), UserLogin.class);
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(UserSignUp.this, findViewById(R.id.signup_btn), "trans_login");
                            startActivity(intent1, options.toBundle());
                        }

                        else
                        {
                            //do nothing
                            Toast.makeText(UserSignUp.this, "There's already a user with the username " + "'" + user.getUsername() + "'", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });*/
                break;
            case(R.id.tvLogin):
                Intent intent2 = new Intent(getApplicationContext(), UserLogin.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, findViewById(R.id.tvLogin), "trans_login");
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
}