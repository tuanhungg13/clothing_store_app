package com.project.clothingstore.view.fragment.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.clothingstore.R;
import com.project.clothingstore.view.activity.AuthActivity;
import com.project.clothingstore.viewmodel.AuthViewModel;

public class RegisterFragment extends Fragment {
    private LinearLayout layoutLogin;
    private EditText editRegisterName, editRegisterEmail, editRegisterPhone, editRegisterPassword, editConfirmPassword;
    private Button btnRegister;
    private ImageButton imbLoginGoogle;
    private AuthViewModel authViewModel;

    // Biến tạm lưu email và password để truyền sang LoginFragment
    private String registeredEmail = "";
    private String registeredPassword = "";
    private static final int RC_GOOGLE_SIGN_IN = 1001;

    public RegisterFragment() {
    }

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
        imbLoginGoogle = view.findViewById(R.id.imb_login_google);
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

                            // Truyền email & password sang LoginFragment
                            LoginFragment loginFragment = new LoginFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("email", registeredEmail);
                            bundle.putString("password", registeredPassword);
                            loginFragment.setArguments(bundle);

                            ((AuthActivity) requireActivity()).replaceFragment(loginFragment, true);
                        } else {
                            Toast.makeText(getContext(), "Gửi email xác thực thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        // ===== Xử lý đăng nhập bằng Google=====
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // ID client trong strings.xml từ Firebase
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        // Luôn hiện cửa sổ chọn tài khoản
        imbLoginGoogle.setOnClickListener(v -> {
            googleSignInClient.signOut().addOnCompleteListener(task -> {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
            });
        });

        // Chuyển sang LoginFragment (nếu người dùng bấm "đã có tài khoản")
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
        if (password.length() < 6) {
            Toast.makeText(getContext(), "Mật khẩu phải từ 6 ký tự trở lên", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!phone.matches("^(0|\\+84)[0-9]{9}$")) {
            editRegisterPhone.setError("Số điện thoại không hợp lệ");
            editRegisterPhone.requestFocus();
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(getContext(), "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lưu lại để truyền sau khi đăng ký thành công
        registeredEmail = email;
        registeredPassword = password;

        authViewModel.register(email, password, name, phone);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    String idToken = account.getIdToken();
                    authViewModel.loginWithGoogle(idToken);
                }
            } catch (ApiException e) {
                Toast.makeText(getContext(), "Đăng nhập Google thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
