package com.example.projectlogin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class EditAccount extends AppCompatActivity {
    private ScrollView scrollView;
    private String username;
    private com.google.android.material.textfield.TextInputEditText tv_username;
    private com.google.android.material.textfield.TextInputEditText et_new_pw;
    private AlertDialog.Builder confirmSignOutBuilder;
    public static final String SHARED_PREFS = "rememberMe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        et_new_pw = findViewById(R.id.et_new_pw);
        tv_username = findViewById(R.id.et_usn);
        scrollView = findViewById(R.id.scroll_view);
        DatabaseRef.getDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    username = snapshot.getKey();
                    tv_username.setHint(username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        confirmSignOutBuilder = new AlertDialog.Builder(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.save_btn):
                String newPassword = et_new_pw.getText().toString();
                if (newPassword.length() < 6 || newPassword.length() > 20) {
                    Toast.makeText(this, "Password must be between 6 and 20 characters", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseRef.getDatabaseReference().child("Information").child("password").setValue(newPassword);
                    Intent intent = new Intent(getApplicationContext(), MainUI.class);
                    startActivity(intent);
                }
                break;
            case (R.id.delacc_btn):
                confirmSignOutBuilder.setTitle("Confirmation");
                confirmSignOutBuilder.setMessage("Do you want to delete this account?");
                confirmSignOutBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseRef.getDatabaseReference().removeValue();
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(username, false);
                        editor.commit();
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainUI.class);
        startActivity(intent);
    }

    public void backBtn(View view) {
        onBackPressed();
    }
}