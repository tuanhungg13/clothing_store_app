package com.project.clothingstore.view.fragment.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.clothingstore.R;

public class ForgotPasswordFragment extends Fragment {
    private ImageButton btnBack;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v ->{
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }
        );
        return view;
    }
}
