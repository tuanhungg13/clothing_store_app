package com.project.clothingstore.view.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.project.clothingstore.R;
import com.project.clothingstore.view.fragment.intro.WelcomeFragment;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_intro);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.intro_container, new WelcomeFragment())
                .commit();
    }
}