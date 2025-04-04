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

public class RegisterFragment extends Fragment {
    private LinearLayout layoutLogin;


    public RegisterFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        layoutLogin = view.findViewById(R.id.layout_login);

        // Chuyển sang RegisterFragment
        layoutLogin.setOnClickListener(v ->
                ((AuthActivity) requireActivity()).replaceFragment(new LoginFragment(), true)
        );

        return view;
    }
}
