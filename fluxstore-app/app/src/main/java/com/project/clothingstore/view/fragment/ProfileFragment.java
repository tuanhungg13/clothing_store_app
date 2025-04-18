package com.project.clothingstore.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.clothingstore.R;
import com.project.clothingstore.adapter.ProfileOptionAdapter;
import com.project.clothingstore.modal.ProfileOption;
import com.project.clothingstore.utils.DividerItemDecorator;
import com.project.clothingstore.view.activity.AuthActivity;
import com.project.clothingstore.view.activity.CartActivity;
import com.project.clothingstore.view.activity.CouponActivity;
import com.project.clothingstore.view.activity.DashboardActivity;
import com.project.clothingstore.view.activity.ProfileActivity;
import com.project.clothingstore.viewmodel.AuthViewModel;
import com.project.clothingstore.viewmodel.UserViewModel;

import java.util.Arrays;
import java.util.List;

public class ProfileFragment extends Fragment {

    private TextView tvName, tvEmail;
    private ImageButton btnSetting;
    private RecyclerView recyclerView;
    private ShapeableImageView imgAvatar;
    private AuthViewModel authViewModel;
    private UserViewModel userViewModel;
    private LinearLayout layoutLoggedIn, layoutLoggedOut;
    private Button btnLoginProfile, btnRegisterProfile;

    public ProfileFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        tvName = view.findViewById(R.id.tv_full_name);
        tvEmail = view.findViewById(R.id.tv_email);
        imgAvatar = view.findViewById(R.id.img_avatar);
        recyclerView = view.findViewById(R.id.recycler_profile_options);
        btnSetting = view.findViewById(R.id.btn_settings);
        btnLoginProfile = view.findViewById(R.id.btn_login_profile);
        btnRegisterProfile = view.findViewById(R.id.btn_register_profile);
        layoutLoggedIn = view.findViewById(R.id.layout_logged_in);
        layoutLoggedOut = view.findViewById(R.id.layout_logged_out);

        // Xử lý nút đăng nhập / đăng ký
        btnLoginProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AuthActivity.class);
            intent.putExtra("fragment", "login");
            startActivity(intent);
        });

        btnRegisterProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AuthActivity.class);
            intent.putExtra("fragment", "register");
            startActivity(intent);
        });

        // Quan sát trạng thái đăng nhập
        authViewModel.getCurrentUid().observe(getViewLifecycleOwner(), uid -> {
            if (uid == null) {
                layoutLoggedOut.setVisibility(View.VISIBLE);
                layoutLoggedIn.setVisibility(View.GONE);
            } else {
                layoutLoggedOut.setVisibility(View.GONE);
                layoutLoggedIn.setVisibility(View.VISIBLE);
                loadUserInfo(uid);
            }
        });

        authViewModel.checkLoggedIn();

        btnSetting.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ProfileActivity.class);
            startActivity(intent);
        });

        return view;
    }

    // Tải thông tin người dùng
    private void loadUserInfo(String uid) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userViewModel.fetchUserInfo(uid);
            tvEmail.setText(user.getEmail());

            userViewModel.getCurrentUser().observe(getViewLifecycleOwner(), userLive -> {
                if (userLive != null) {
                    tvEmail.setText(userLive.getEmail());
                    tvName.setText(userLive.getFullName());

                    if (userLive.getAvatar() != null && !userLive.getAvatar().isEmpty()) {
                        Glide.with(requireContext())
                                .load(userLive.getAvatar())
                                .placeholder(R.drawable.avatar)
                                .error(R.drawable.avatar)
                                .into(imgAvatar);
                    } else {
                        imgAvatar.setImageResource(R.drawable.avatar);
                    }

                    setupOptions(userLive.getRole());
                }
            });
        }
    }

    // Cài đặt menu profile
    private void setupOptions(String role) {
        List<ProfileOption> options;

        if ("admin".equals(role)) {
            options = Arrays.asList(
                    new ProfileOption("Voucher", R.drawable.ic_voucher),
                    new ProfileOption("Giỏ hàng", R.drawable.icon_cart),
                    new ProfileOption("Quản lí cửa hàng", R.drawable.ic_store),
                    new ProfileOption("Đăng xuất", R.drawable.ic_logout)
            );
        } else {
            options = Arrays.asList(
                    new ProfileOption("Voucher", R.drawable.ic_voucher),
                    new ProfileOption("Giỏ hàng", R.drawable.icon_cart),
                    new ProfileOption("Đăng xuất", R.drawable.ic_logout)
            );
        }

        ProfileOptionAdapter adapter = new ProfileOptionAdapter(options, option -> {
            switch (option.getTitle()) {
                case "Voucher":
                    startActivity(new Intent(getContext(), CouponActivity.class));
                    break;
                case "Giỏ hàng":
                    startActivity(new Intent(getContext(), CartActivity.class));
                    break;
                case "Quản lí cửa hàng":
                    startActivity(new Intent(getContext(), DashboardActivity.class));
                    break;
                case "Đăng xuất":
                    showLogoutConfirmationDialog();
                    break;
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecorator());
    }

    // Hộp thoại xác nhận đăng xuất
    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    authViewModel.logout();
                    userViewModel.clearUser();

                    requireActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new HomeFragment())
                            .commit();

                    BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
                    bottomNav.setSelectedItemId(R.id.nav_home);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

}
