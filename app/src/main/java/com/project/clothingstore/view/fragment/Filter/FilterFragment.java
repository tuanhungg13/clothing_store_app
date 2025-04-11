package com.project.clothingstore.view.fragment.Filter;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.project.clothingstore.R;
import com.project.clothingstore.view.activity.product.ProductsActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterFragment extends Fragment {

    String[] categotyitems = {"Áo & quần", "Giày"};
    ArrayAdapter<String> adapterItems;

    private AutoCompleteTextView autoCompleteTxt;

    private TextInputEditText edt_price_min, edt_price_max;

    private Button btn_Reset, btn_Apply;


    Set<Integer> selectedStar = new HashSet<>();
    Set<Integer> selectedDiscount = new HashSet<>();


    List<FrameLayout> framesstar = new ArrayList<>();
    List<FrameLayout> framesdiscount = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        autoCompleteTxt = view.findViewById(R.id.auto_category_filter);
        adapterItems = new ArrayAdapter<>(getContext(), R.layout.hvq_list_items, categotyitems);
        autoCompleteTxt.setAdapter(adapterItems);

        autoCompleteTxt.setOnItemClickListener((parent, v, position, id) -> {
            String item = parent.getItemAtPosition(position).toString();
            // Có thể lưu lại category được chọn nếu cần
        });

        // Khởi tạo frame rating (sao)
        framesstar.add(view.findViewById(R.id.star_frame1));
        framesstar.add(view.findViewById(R.id.star_frame2));
        framesstar.add(view.findViewById(R.id.star_frame3));
        framesstar.add(view.findViewById(R.id.star_frame4));
        framesstar.add(view.findViewById(R.id.star_frame5));

        // Khởi tạo frame discount
        framesdiscount.add(view.findViewById(R.id.discount_frame0));
        framesdiscount.add(view.findViewById(R.id.discount_frame1));
        framesdiscount.add(view.findViewById(R.id.discount_frame2));
        framesdiscount.add(view.findViewById(R.id.discount_frame3));

        edt_price_min = view.findViewById(R.id.txt_PriceMin);
        edt_price_max = view.findViewById(R.id.txt_PriceMax);

        btn_Reset = view.findViewById(R.id.btn_reset_filter);
        btn_Apply = view.findViewById(R.id.btn_apply_filter);

        // Gán sự kiện click
        setupStarFrameClick(framesstar, selectedStar); // chỉ chọn 1 sao
        setupFrameClick(framesdiscount, selectedDiscount); // nhiều discount

        // Xử lý nút Reset
        btn_Reset.setOnClickListener(v -> {
            autoCompleteTxt.setText("");

            edt_price_min.setText("");
            edt_price_max.setText("");

            selectedStar.clear();
            for (FrameLayout f : framesstar) f.setSelected(false);

            selectedDiscount.clear();
            for (FrameLayout f : framesdiscount) f.setSelected(false);

            Toast.makeText(getContext(), "Đã đặt lại bộ lọc", Toast.LENGTH_SHORT).show();
        });

        // Xử lý nút Apply
        btn_Apply.setOnClickListener(v -> {
            String category = autoCompleteTxt.getText().toString().trim();
            int categoryType;
            if (category.equals("Áo & quần")) {
                categoryType = 0;
            } else if (category.equals("Giày")) {
                categoryType = 1;
            } else {
                categoryType = -1; // Không chọn
            }

            String minPrice = edt_price_min.getText().toString().trim();
            String maxPrice = edt_price_max.getText().toString().trim();

            Integer selectedStarIndex = selectedStar.isEmpty() ? null : selectedStar.iterator().next();
            ArrayList<Integer> selectedDiscountList = new ArrayList<>(selectedDiscount);

            // Khởi tạo Intent
            Intent intent = new Intent(requireContext(), ProductsActivity.class);

            // Truyền dữ liệu qua Intent
            intent.putExtra("categoriType", categoryType);
            intent.putExtra("minPrice", minPrice);
            intent.putExtra("maxPrice", maxPrice);
            if (selectedStarIndex != null) {
                intent.putExtra("rating", selectedStarIndex + 1); // 1-5 sao
            }
            intent.putIntegerArrayListExtra("discountList", selectedDiscountList);

            // Chuyển sang ProductsActivity
            startActivity(intent);
        });


        return view;
    }


    private void setupFrameClick(List<FrameLayout> frames, Set<Integer> selectedSet) {
        for (int i = 0; i < frames.size(); i++) {
            final int index = i;
            FrameLayout frame = frames.get(i);
            frame.setOnClickListener(v -> {
                if (selectedSet.contains(index)) {
                    selectedSet.remove(index);
                    v.setSelected(false);
                } else {
                    selectedSet.add(index);
                    v.setSelected(true);
                }
            });
        }
    }
    private void setupStarFrameClick(List<FrameLayout> frames, Set<Integer> selectedSet) {
        for (int i = 0; i < frames.size(); i++) {
            final int index = i;
            FrameLayout frame = frames.get(i);
            frame.setOnClickListener(v -> {
                // Nếu đã chọn thì bỏ chọn tất cả
                if (selectedSet.contains(index)) {
                    selectedSet.clear();
                    for (FrameLayout f : frames) f.setSelected(false);
                } else {
                    // Bỏ chọn tất cả trước
                    selectedSet.clear();
                    for (FrameLayout f : frames) f.setSelected(false);

                    // Chọn cái mới
                    selectedSet.add(index);
                    v.setSelected(true);
                }
            });
        }
    }


}