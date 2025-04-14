package com.project.clothingstore.view.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.project.clothingstore.R;
import com.project.clothingstore.view.fragment.auth.LoginFragment;
import com.project.clothingstore.view.fragment.auth.RegisterFragment;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);
        if (savedInstanceState == null) {
            String fragmentToOpen = getIntent().getStringExtra("fragment");
            if ("register".equals(fragmentToOpen)) {
                replaceFragment(new RegisterFragment(), false);
            } else {
                replaceFragment(new LoginFragment(), false); // mặc định là login
            }
        }
    }
    // Hàm chuyển đổi Fragment
    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.auth_container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
}