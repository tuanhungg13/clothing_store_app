package com.project.clothingstore.view.fragment.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.project.clothingstore.MainActivity;
import com.project.clothingstore.R;
import com.project.clothingstore.view.activity.AuthActivity;
import com.project.clothingstore.viewmodel.AuthViewModel;
import com.project.clothingstore.viewmodel.UserViewModel;

public class LoginFragment extends Fragment {
    private LinearLayout layoutRegister;
    private TextView txtForgotPassword;
    private EditText editLoginEmail, editLoginPassword;
    private Button btnLogin;
    private AuthViewModel authViewModel;
    private UserViewModel userViewModel;
    private ImageButton imbLoginGoogle;
    private static final int RC_GOOGLE_SIGN_IN = 1001;

    public LoginFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        layoutRegister = view.findViewById(R.id.layout_register);
        txtForgotPassword = view.findViewById(R.id.txt_forgot_password);
        editLoginEmail = view.findViewById(R.id.edit_login_email);
        editLoginPassword = view.findViewById(R.id.edit_login_password);
        btnLogin = view.findViewById(R.id.btn_login);
        imbLoginGoogle = view.findViewById(R.id.imb_login_google);

        // ===== Xử lý đăng nhập =====
        btnLogin.setOnClickListener(v -> {
            String email = editLoginEmail.getText().toString().trim();
            String password = editLoginPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            authViewModel.login(email, password);
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

        // ===== Quan sát UID đăng nhập thành công =====
        authViewModel.getCurrentUid().observe(getViewLifecycleOwner(), uid -> {
            if (uid != null) {
                // Kiểm tra email đã xác thực chưa
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser != null && !firebaseUser.isEmailVerified()) {
                    Toast.makeText(getContext(), "Vui lòng xác thực email trước khi đăng nhập", Toast.LENGTH_LONG).show();
                    FirebaseAuth.getInstance().signOut(); // Đăng xuất nếu email chưa xác thực
                } else {
                    userViewModel.fetchUserInfo(uid); // Tiếp tục lấy thông tin người dùng nếu email đã xác thực
                }
            }
        });

        // ===== Quan sát người dùng để xử lý phân quyền =====
        userViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                switch (user.getRole()) {
                    case "admin":
                        startActivity(new Intent(getContext(), MainActivity.class));
                        break;
                    case "user":
                    default:
                        startActivity(new Intent(getContext(), MainActivity.class));
                        break;
                }
                requireActivity().finish(); // đóng AuthActivity
            }
        });

        // ===== Quan sát lỗi đăng nhập =====
        authViewModel.getAuthError().observe(getViewLifecycleOwner(), event -> {
            if (event != null) {
                String errorMessage = event.getContentIfNotHandled();
                if (errorMessage != null) {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // ===== Quan sát lỗi khi lấy thông tin người dùng =====
        userViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        // ===== Chuyển Fragment =====
        layoutRegister.setOnClickListener(v ->
                ((AuthActivity) requireActivity()).replaceFragment(new RegisterFragment(), true)
        );
        txtForgotPassword.setOnClickListener(v ->
                ((AuthActivity) requireActivity()).replaceFragment(new ForgotPasswordFragment(), true)
        );

        return view;
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
