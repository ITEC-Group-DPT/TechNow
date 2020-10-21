package com.example.projectlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

public class ReenterPassword extends AppCompatActivity {
    private ScrollView scrollView;
    private ManageUser manageUser;
    private String username;
    private int noOfItem;
    private com.google.android.material.textfield.TextInputEditText tv_username;
    private com.google.android.material.textfield.TextInputEditText et_pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reenter_password);

        backgroundAnim();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            username = bundle.getString("username_reenter");
            noOfItem = bundle.getInt("noItem");
        }
        tv_username = findViewById(R.id.et_usn);
        tv_username.setHint(username);
        et_pw = findViewById(R.id.et_pw);
    }

    public void backgroundAnim() {
        scrollView = findViewById(R.id.scroll_view);
        AnimationDrawable animationDrawable = (AnimationDrawable) scrollView.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
    }

    public void onClick(View view) {
        manageUser = new ManageUser(this);
        String pass_str = et_pw.getText().toString();
        User user = new User(username, pass_str);
        if (manageUser.checkLoginUser(user) == true) {
            Intent intent = new Intent(getApplicationContext(), EditAccount.class);
            intent.putExtra("username_edit", username);
            intent.putExtra("noItem", noOfItem);
            startActivity(intent);
        }
        else Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
    }
}