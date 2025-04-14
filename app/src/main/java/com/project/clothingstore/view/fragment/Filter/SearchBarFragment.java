package com.project.clothingstore.view.fragment.Filter;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;

import com.project.clothingstore.R;
import com.project.clothingstore.view.activity.product.ProductsActivity;

import androidx.annotation.NonNull;
import android.content.Context;
import android.widget.TextView;


public class SearchBarFragment extends Fragment {
    ImageButton imgbtn_find, imgbtn_filter;
    EditText edt_find;
    String productName;

    // Interface callback
    public interface OnFilterButtonClickListener {
        void onFilterButtonClick();
    }

    private OnFilterButtonClickListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Ưu tiên lấy listener từ Fragment cha nếu có
        if (getParentFragment() instanceof OnFilterButtonClickListener) {
            listener = (OnFilterButtonClickListener) getParentFragment();
        }
        // Nếu không có Fragment cha, kiểm tra context chính là activity có implement interface
        else if (context instanceof OnFilterButtonClickListener) {
            listener = (OnFilterButtonClickListener) context;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_bar, container, false);

        imgbtn_find = view.findViewById(R.id.imgbtn_find_search_bar);
        imgbtn_filter = view.findViewById(R.id.imgbtn_filter_search_bar);
        edt_find = view.findViewById(R.id.edt_find_discover_search_bar);

        if (getArguments() != null) {
            productName = getArguments().getString("productName");
            if (productName != null) {
                edt_find.setText(productName);
            }
        }


        imgbtn_filter.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFilterButtonClick();
            }
        });
        edt_find.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    String searchText = edt_find.getText().toString().trim();
                    if (!searchText.isEmpty()) {
                        Intent intent = new Intent(getActivity(), ProductsActivity.class);
                        intent.putExtra("productName", searchText);
                        startActivity(intent);
                    } else {
                        edt_find.setError("Vui lòng nhập từ khóa tìm kiếm");
                    }
                    return true;
                }
                return false;
            }
        });


        imgbtn_find.setOnClickListener(v -> {
            String searchText = edt_find.getText().toString();
            if (!searchText.isEmpty()) {
                Intent intent = new Intent(getActivity(), ProductsActivity.class);
                intent.putExtra("productName", searchText);
                startActivity(intent);
            }else {
                edt_find.setError("Vui lòng nhập từ khóa tìm kiếm");
            }
        });

        return view;
    }
}
