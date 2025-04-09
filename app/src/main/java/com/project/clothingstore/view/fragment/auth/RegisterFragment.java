package com.project.clothingstore.view.fragment.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.clothingstore.R;
import com.project.clothingstore.view.activity.AuthActivity;
import com.project.clothingstore.viewmodel.AuthViewModel;

public class RegisterFragment extends Fragment {
    private LinearLayout layoutLogin;
    private EditText editRegisterName, editRegisterEmail, editRegisterPhone, editRegisterPassword, editConfirmPassword;
    private Button btnRegister;
    private AuthViewModel authViewModel;

    public RegisterFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        layoutLogin = view.findViewById(R.id.layout_login);
        editRegisterName = view.findViewById(R.id.edit_register_name);
        editRegisterEmail = view.findViewById(R.id.edit_register_email);
        editRegisterPhone = view.findViewById(R.id.edit_register_phone_number);
        editRegisterPassword = view.findViewById(R.id.edit_register_password);
        editConfirmPassword = view.findViewById(R.id.edit_confirm_password);
        btnRegister = view.findViewById(R.id.btn_register);

        // ===== Đăng ký =====
        btnRegister.setOnClickListener(v -> handleRegister());

        // ===== Quan sát lỗi nếu có =====
        authViewModel.getAuthError().observe(getViewLifecycleOwner(), event -> {
            if (event != null) {
                String errorMessage = event.getContentIfNotHandled();
                if (errorMessage != null) {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // ===== Quan sát kết quả đăng ký thành công =====
        authViewModel.getCurrentUid().observe(getViewLifecycleOwner(), uid -> {
            if (uid != null) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null && !user.isEmailVerified()) {
                    user.sendEmailVerification().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Vui lòng xác thực email trước khi đăng nhập", Toast.LENGTH_LONG).show();
                            ((AuthActivity) requireActivity()).replaceFragment(new LoginFragment(), true);
                        } else {
                            Toast.makeText(getContext(), "Gửi email xác thực thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        // Chuyển sang LoginFragment
        layoutLogin.setOnClickListener(v ->
                ((AuthActivity) requireActivity()).replaceFragment(new LoginFragment(), true)
        );

        return view;
    }

    private void handleRegister() {
        String name = editRegisterName.getText().toString().trim();
        String email = editRegisterEmail.getText().toString().trim();
        String phone = editRegisterPhone.getText().toString().trim();
        String password = editRegisterPassword.getText().toString().trim();
        String confirmPassword = editConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(getContext(), "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        authViewModel.register(email, password, name, phone);
    }
}
