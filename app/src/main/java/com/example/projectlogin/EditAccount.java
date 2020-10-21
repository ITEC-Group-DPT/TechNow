package com.example.projectlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

public class EditAccount extends AppCompatActivity {
    private ScrollView scrollView;
    private ManageUser manageUser;
    private String username;
    private int noOfItem;
    private com.google.android.material.textfield.TextInputEditText tv_username;
    private com.google.android.material.textfield.TextInputEditText et_new_pw;
    private AlertDialog.Builder confirmSignOutBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        backgroundAnim();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            username = bundle.getString("username_edit");
            noOfItem = bundle.getInt("noItem");
        }
        confirmSignOutBuilder = new AlertDialog.Builder(this);
        manageUser = new ManageUser(this);
        tv_username = findViewById(R.id.et_usn);
        tv_username.setHint(username);
        et_new_pw = findViewById(R.id.et_new_pw);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.save_btn):
                String newPassword = et_new_pw.getText().toString();
                manageUser.changePassword(username, newPassword,this);
                Intent intent = new Intent(getApplicationContext(), MainUI.class);
                intent.putExtra("username",username);
                intent.putExtra("noItem", noOfItem);
                startActivity(intent);

                break;
            case(R.id.delacc_btn):
                confirmSignOutBuilder.setTitle("Confirmation");
                confirmSignOutBuilder.setMessage("Do you want to delete this account?");
                confirmSignOutBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        manageUser.deleteUser(username, getApplicationContext());
                        Cart.clearAll();
                        Intent intent = new Intent(getApplicationContext(), UserLogin.class);
                        startActivity(intent);
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

    public void backgroundAnim() {
        scrollView = findViewById(R.id.scroll_view);
        AnimationDrawable animationDrawable = (AnimationDrawable) scrollView.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainUI.class);
        intent.putExtra("username",username);
        intent.putExtra("noItem", noOfItem);
        startActivity(intent);
    }
}