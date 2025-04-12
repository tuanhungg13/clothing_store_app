package com.project.clothingstore.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.project.clothingstore.MainActivity;
import com.project.clothingstore.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
            boolean isFirstTime = prefs.getBoolean("isFirstTime", true);

            if (isFirstTime) {
                prefs.edit().putBoolean("isFirstTime", false).apply();
                startActivity(new Intent(this, IntroActivity.class));
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }

            finish(); // đóng SplashActivity
        }, 2000);
    }
}