package com.example.projectlogin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIMER = 3000;
    private ImageView appLogo;

    private Animation anim1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);


        Toast.makeText(this, "asjdgahjsd", Toast.LENGTH_SHORT).show();
        appLogo = findViewById(R.id.app_logo);
        anim1 = AnimationUtils.loadAnimation(this, R.anim.splash_screen_logo);
        appLogo.setAnimation(anim1);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(getApplicationContext(), UserLogin.class);
                startActivity(intent);
            }
        }, SPLASH_TIMER);
        
    }
}
