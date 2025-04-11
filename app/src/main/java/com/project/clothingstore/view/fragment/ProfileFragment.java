package com.project.clothingstore.view.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.clothingstore.R;
import com.project.clothingstore.adapter.ProfileOptionAdapter;
import com.project.clothingstore.modal.ProfileOption;
import com.project.clothingstore.utils.DividerItemDecorator;
import com.project.clothingstore.view.activity.AuthActivity;
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


    public ProfileFragment() {
        // Required empty public constructor
    }

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

        authViewModel.getCurrentUid().observe(getViewLifecycleOwner(), uid -> {
            if (uid == null) {
                layoutLoggedOut.setVisibility(View.VISIBLE);
                layoutLoggedIn.setVisibility(View.GONE);
            } else {
                layoutLoggedOut.setVisibility(View.GONE);
                layoutLoggedIn.setVisibility(View.VISIBLE);
            }
        });
        authViewModel.checkLoggedIn();

        loadUserInfo();

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
    // Tải thông tin người dùng về
    private void loadUserInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            tvEmail.setText(user.getEmail());
            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(user.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String fullName = documentSnapshot.getString("fullName");
                            String role = documentSnapshot.getString("role");
                            String avatarBase64 = documentSnapshot.getString("avatar");

                            tvName.setText(fullName);

                            if (avatarBase64 != null && !avatarBase64.isEmpty()) {
                                byte[] decodedString = Base64.decode(avatarBase64, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                imgAvatar.setImageBitmap(decodedByte);
                            }

                            setupOptions(role);
                        }
                    });
        }
    }

    // Hiển thị menu chức năng profile
    private void setupOptions(String role) {
        List<ProfileOption> options;

        if ("admin".equals(role)) {
            options = Arrays.asList(
                    new ProfileOption("Voucher", R.drawable.ic_voucher),
                    new ProfileOption("Quản lí cửa hàng", R.drawable.ic_store),
                    new ProfileOption("Đăng xuất", R.drawable.ic_logout)
            );
        } else {
            options = Arrays.asList(
                    new ProfileOption("Voucher", R.drawable.ic_voucher),
                    new ProfileOption("Đăng xuất", R.drawable.ic_logout)
            );
        }

        ProfileOptionAdapter adapter = new ProfileOptionAdapter(options, option -> {
            switch (option.getTitle()) {
                case "Voucher":
                    startActivity(new Intent(getContext(), AuthActivity.class));
                    break;
                case "Quản lí cửa hàng":
                    startActivity(new Intent(getContext(), AuthActivity.class));
                    break;
                case "Đăng xuất":
                    showLogoutConfirmationDialog();
                    break;
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Thêm dòng kẻ giữa các item
        recyclerView.addItemDecoration(new DividerItemDecorator());
    }
    // Đăng xuất
    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    authViewModel.logout();         // Đăng xuất qua ViewModel
                    userViewModel.clearUser();      // Xoá thông tin người dùng

                    // Chuyển về HomeFragment
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
