package com.project.clothingstore.view.fragment.auth;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.project.clothingstore.R;
import com.project.clothingstore.view.activity.AuthActivity;
import com.project.clothingstore.viewmodel.AuthViewModel;

public class ForgotPasswordFragment extends Fragment {
    private ImageButton btnBack;
    private EditText editForgotEmail;
    private Button btnSendReset;
    private AuthViewModel authViewModel;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        btnBack = view.findViewById(R.id.btn_back);
        editForgotEmail = view.findViewById(R.id.edit_forgot_email);
        btnSendReset = view.findViewById(R.id.btn_send_otp); // reuse button ID

        btnSendReset.setOnClickListener(v -> {
            String email = editForgotEmail.getText().toString().trim();
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getContext(), "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            authViewModel.sendPasswordReset(email);
        });

        authViewModel.getIsPasswordReset().observe(getViewLifecycleOwner(), isSent -> {
            if (isSent != null && isSent) {
                Toast.makeText(getContext(), "Đã gửi email đặt lại mật khẩu", Toast.LENGTH_LONG).show();
                ((AuthActivity) requireActivity()).replaceFragment(new LoginFragment(), true);
            }
        });

        authViewModel.getAuthError().observe(getViewLifecycleOwner(), event -> {
            if (event != null) {
                String error = event.getContentIfNotHandled();
                if (error != null) {
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }
}
