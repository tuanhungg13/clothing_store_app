package com.project.clothingstore;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.project.clothingstore.view.activity.AuthActivity;
import com.project.clothingstore.view.fragment.OrderFragment;
import com.project.clothingstore.view.fragment.ProfileFragment;
import com.project.clothingstore.view.fragment.HomeFragment;
import com.project.clothingstore.view.fragment.SearchFragment;
import com.project.clothingstore.viewmodel.UserViewModel;

public class MainActivity extends AppCompatActivity {
    // Khai báo DrawerLayout
    private DrawerLayout drawerLayout;
    private ImageButton btnMenu;

    private ImageView imgVata;
    private TextView txtName, txtEmail;
    private boolean isUserLoggedIn = false;
    private BottomNavigationView bottomNav;
    private NavigationView navigationView;
    UserViewModel userViewModel;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Khởi tạo BottomNavigationView
        bottomNav = findViewById(R.id.bottom_navigation);
        navigationView = findViewById(R.id.nav_view);
        // Khởi tạo DrawerLayout
        drawerLayout = findViewById(R.id.main_drawer_layout);
        btnMenu = findViewById(R.id.btnMenu);
        // Khởi tạo ImageView và TextView
        imgVata = navigationView.getHeaderView(0).findViewById(R.id.iv_avata_profile_hvq);
        txtName = navigationView.getHeaderView(0).findViewById(R.id.tv_name_profile_hvq);
        txtEmail = navigationView.getHeaderView(0).findViewById(R.id.tv_email_profile_hvq);

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
            checkLoginStatus();

        });

        // Lắng nghe sự kiện chọn item trong BottomNavigationView
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                loadFragment(new HomeFragment());
                navigationView.setCheckedItem(R.id.nav_home_hvq);
                return true;
            } else if (item.getItemId() == R.id.nav_search) {
                loadFragment(new SearchFragment());
                navigationView.setCheckedItem(R.id.nav_search_hvq);
                return true;
            } else if (item.getItemId() == R.id.nav_cart) {
                loadFragment(new OrderFragment());
                navigationView.setCheckedItem(R.id.nav_cart_hvq);
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                loadFragment(new ProfileFragment());
                navigationView.setCheckedItem(R.id.nav_profile_hvq);
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
                if (isUserLoggedIn) {
                    loadFragment(new OrderFragment());
                    bottomNav.setSelectedItemId(R.id.nav_cart);
                } else {
                    Intent intent = new Intent(MainActivity.this, AuthActivity.class);
                    intent.putExtra("fragment", "login");
                    startActivity(intent);
                }
                return true;
            } else if (item.getItemId() == R.id.nav_profile_hvq) {
                loadFragment(new ProfileFragment());
                bottomNav.setSelectedItemId(R.id.nav_profile);
                return true;
            } else if (item.getItemId() == R.id.nav_login_hvq) {
                Intent intent = new Intent(MainActivity.this, AuthActivity.class);
                intent.putExtra("fragment", "login");
                startActivity(intent);
                return true;
            }else if (item.getItemId() == R.id.nav_logout_hvq) {
                performLogout();
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
            bottomNav.setSelectedItemId(R.id.nav_search);
            navigationView.setCheckedItem(R.id.nav_search_hvq);
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

    private void checkLoginStatus() {
        // Lấy instance của Firebase Authentication
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        if (auth.getCurrentUser() != null) {
            // Người dùng đã đăng nhập
            isUserLoggedIn = true;
            navigationView.findViewById(R.id.nav_logout_hvq).setVisibility(View.VISIBLE); // Hiển thị nút đăng xuất
            navigationView.findViewById(R.id.nav_login_hvq).setVisibility(View.GONE); // Ẩn nút đăng nhập

            // Lấy UID của người dùng hiện tại
            String uid = auth.getCurrentUser().getUid();

            // Gọi ViewModel để lấy thông tin người dùng từ Firestore (hoặc Realtime Database)
            userViewModel.fetchUserInfo(uid);

            // Quan sát dữ liệu người dùng được trả về từ ViewModel
            userViewModel.getCurrentUser().observe(this, user -> {
                if (user != null) {
                    // Nếu lấy được thông tin người dùng thành công, cập nhật cartId
                    txtName.setText(user.getFullName());
                    txtEmail.setText(user.getEmail());
                    if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                        byte[] decodedString = Base64.decode(user.getAvatar(), Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imgVata.setImageBitmap(decodedByte);
                    }

                }
            });

        } else {
            // Người dùng chưa đăng nhập
            isUserLoggedIn = false;
            txtName.setText("Họ và tên");
            txtEmail.setText("Email");
            imgVata.setImageResource(R.drawable.avata_profile_hvq); // Đặt ảnh mặc định
            navigationView.findViewById(R.id.nav_logout_hvq).setVisibility(View.GONE); // Ẩn nút đăng xuất
            navigationView.findViewById(R.id.nav_login_hvq).setVisibility(View.VISIBLE); // Hiển thị nút đăng nhập

        }


    }
    private void performLogout() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    // Đăng xuất người dùng
                    FirebaseAuth.getInstance().signOut();
                    userViewModel.clearUser();

                    // Cập nhật giao diện sau khi đăng xuất
                    txtName.setText("Họ và tên");
                    txtEmail.setText("Email");
                    imgVata.setImageResource(R.drawable.avata_profile_hvq);
                    navigationView.findViewById(R.id.nav_logout_hvq).setVisibility(View.GONE);
                    navigationView.findViewById(R.id.nav_login_hvq).setVisibility(View.VISIBLE);

                    // Chuyển về HomeFragment
                    loadFragment(new HomeFragment());
                    bottomNav.setSelectedItemId(R.id.nav_home);
                    navigationView.setCheckedItem(R.id.nav_home_hvq);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }


}
