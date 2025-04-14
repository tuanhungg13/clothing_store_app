package com.project.clothingstore;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.project.clothingstore.modal.User;
import com.project.clothingstore.view.fragment.OrderFragment;
import com.project.clothingstore.view.fragment.ProfileFragment;
import com.project.clothingstore.view.fragment.HomeFragment;
import com.project.clothingstore.view.fragment.SearchFragment;
import com.project.clothingstore.viewmodel.UserViewModel;

public class MainActivity extends AppCompatActivity {
    // Khai báo DrawerLayout
    private DrawerLayout drawerLayout;
    private ImageButton btnMenu;

    private UserViewModel userViewModel;

    private boolean isUserLoggedIn = false;

    private TextView title;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.tvTitle);
        // Khởi tạo BottomNavigationView
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Khởi tạo DrawerLayout
        drawerLayout = findViewById(R.id.main_drawer_layout);
        btnMenu = findViewById(R.id.btnMenu);

        // Thiết lập mặc định fragment khi ứng dụng khởi động
        if (savedInstanceState == null) {
            // Mặc định là HomeFragment
            loadFragment(new HomeFragment());
            navigationView.setCheckedItem(R.id.nav_home_hvq);
        }
        // Thiết lập sự kiện click cho nút menu
        btnMenu.setOnClickListener(v -> {
            // Mở hoặc đóng DrawerLayout
            toggleDrawer();

        });

        // Lắng nghe sự kiện chọn item trong BottomNavigationView
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                loadFragment(new HomeFragment());
                navigationView.setCheckedItem(R.id.nav_home_hvq);
                title.setText("FluxStore");
                return true;
            } else if (item.getItemId() == R.id.nav_search) {
                loadFragment(new SearchFragment());
                navigationView.setCheckedItem(R.id.nav_search_hvq);
                title.setText("FluxStore");
                return true;
            } else if (item.getItemId() == R.id.nav_cart) {
                loadFragment(new OrderFragment());
                navigationView.setCheckedItem(R.id.nav_cart_hvq);
                title.setText("Đơn hàng");
                if(FirebaseAuth.getInstance().getCurrentUser() == null) {
                    Toast.makeText(this, "Vui lòng đăng nhập để xem đơn hàng", Toast.LENGTH_SHORT).show();
                    // Mở lại HomeFragment
                    loadFragment(new HomeFragment());
                    bottomNav.setSelectedItemId(R.id.nav_home);
                    navigationView.setCheckedItem(R.id.nav_home_hvq);
                    return false;
                }
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                loadFragment(new ProfileFragment());
                navigationView.setCheckedItem(R.id.nav_profile_hvq);
                title.setText("FluxStore");
                return true;
            }
            return false;
        });

        // Lắng nghe sự kiện chọn item trong NavigationView
        navigationView.setNavigationItemSelectedListener(item -> {
            // Đóng DrawerLayout khi chọn item
            drawerLayout.closeDrawer(GravityCompat.START);
            // Kiểm tra item được chọn và load fragment tương ứng
            if (item.getItemId() == R.id.nav_home_hvq) {
                loadFragment(new HomeFragment());
                bottomNav.setSelectedItemId(R.id.nav_home);
                return true;
            } else if (item.getItemId() == R.id.nav_search_hvq) {
                loadFragment(new SearchFragment());
                bottomNav.setSelectedItemId(R.id.nav_search);
                return true;
            } else if (item.getItemId() == R.id.nav_cart_hvq) {
                loadFragment(new OrderFragment());
                bottomNav.setSelectedItemId(R.id.nav_cart);
                return true;
            } else if (item.getItemId() == R.id.nav_profile_hvq) {
                loadFragment(new ProfileFragment());
                bottomNav.setSelectedItemId(R.id.nav_profile);
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

    private void toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

}
