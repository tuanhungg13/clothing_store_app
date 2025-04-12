package com.project.clothingstore;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.clothingstore.view.fragment.OrderFragment;
import com.project.clothingstore.view.fragment.ProfileFragment;
import com.project.clothingstore.view.fragment.HomeFragment;
import com.project.clothingstore.view.fragment.SearchFragment;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo BottomNavigationView
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Thiết lập mặc định fragment khi ứng dụng khởi động
        if (savedInstanceState == null) {
            // Mặc định là HomeFragment
            loadFragment(new HomeFragment());
        }

        // Lắng nghe sự kiện chọn item trong BottomNavigationView
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                loadFragment(new HomeFragment());
                return true;
            } else if (item.getItemId() == R.id.nav_search) {
                loadFragment(new SearchFragment());
                return true;
            } else if (item.getItemId() == R.id.nav_cart) {
                loadFragment(new OrderFragment());
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                loadFragment(new ProfileFragment());
                return true;
            }
            return false;
        });


    }

    // Hàm để load fragment mới vào container
    private void loadFragment(Fragment fragment) {
        // Thực hiện giao dịch fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Kiểm tra Intent để xác định có cần mở lại SearchFragment không
        if (getIntent().getBooleanExtra("openSearchFragment", false)) {
            loadFragment(new SearchFragment());
            getIntent().removeExtra("openSearchFragment"); // Xóa intent data để tránh mở lại
        }
    }

}
