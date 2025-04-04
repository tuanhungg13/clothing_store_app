package com.project.clothingstore.view.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.clothingstore.R;

public class LoginFragment extends Fragment {
    private LinearLayout layoutRegister;
    private TextView txtForgotPassword;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        layoutRegister = view.findViewById(R.id.layout_register);
        txtForgotPassword = view.findViewById(R.id.txt_forgot_password);

        // Chuyển sang RegisterFragment
        layoutRegister.setOnClickListener(v ->
                ((AuthActivity) requireActivity()).replaceFragment(new RegisterFragment(), true)
        );
        txtForgotPassword.setOnClickListener(v ->
                ((AuthActivity) requireActivity()).replaceFragment(new ForgotPasswordFragment(), true)
        );

        return view;
    }
}
