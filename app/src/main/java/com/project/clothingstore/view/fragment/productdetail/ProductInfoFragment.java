package com.project.clothingstore.view.fragment.productdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.project.clothingstore.R;
import com.project.clothingstore.viewmodel.ProductDetailViewModel;

public class ProductInfoFragment extends Fragment {
    private ProductDetailViewModel viewModel;
    private TextView tvProductName, tvProductPrice, tvRatingCount;
    private RatingBar ratingBar;
    private TextView colorBeige, colorBlack, colorPink;
    private TextView sizeS, sizeM, sizeL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        tvProductName = view.findViewById(R.id.tvProductName);
        tvProductPrice = view.findViewById(R.id.tvProductPrice);
        tvRatingCount = view.findViewById(R.id.tvRatingCount);
        ratingBar = view.findViewById(R.id.ratingBar);

        colorBeige = view.findViewById(R.id.colorBeige);
        colorBlack = view.findViewById(R.id.colorBlack);
        colorPink = view.findViewById(R.id.colorPink);

        sizeS = view.findViewById(R.id.sizeS);
        sizeM = view.findViewById(R.id.sizeM);
        sizeL = view.findViewById(R.id.sizeL);

        // Get shared ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(ProductDetailViewModel.class);

        // Setup color selection
        colorBeige.setOnClickListener(v -> updateColorSelection("Beige"));
        colorBlack.setOnClickListener(v -> updateColorSelection("Đen"));
        colorPink.setOnClickListener(v -> updateColorSelection("Đỏ"));

        // Setup size selection
        sizeS.setOnClickListener(v -> updateSizeSelection("S"));
        sizeM.setOnClickListener(v -> updateSizeSelection("M"));
        sizeL.setOnClickListener(v -> updateSizeSelection("L"));

        // Observe product data
        viewModel.getProduct().observe(getViewLifecycleOwner(), product -> {
            if (product != null) {
                tvProductName.setText(product.getProductName());
                tvProductPrice.setText("$ " + product.getPrice() + ".00");
                ratingBar.setRating(product.getTotalRating());
            }
        });

        // Observe ratings data for count
        viewModel.getRatings().observe(getViewLifecycleOwner(), ratings -> {
            if (ratings != null) {
                tvRatingCount.setText("(" + ratings.size() + ")");
            }
        });

        // Observe selected color
        viewModel.getSelectedColor().observe(getViewLifecycleOwner(), color -> {
            updateColorUI(color);
        });

        // Observe selected size
        viewModel.getSelectedSize().observe(getViewLifecycleOwner(), size -> {
            updateSizeUI(size);
        });
    }

    private void updateColorSelection(String color) {
        viewModel.setSelectedColor(color);
    }

    private void updateSizeSelection(String size) {
        viewModel.setSelectedSize(size);
    }

    private void updateColorUI(String selectedColor) {
        // Reset all colors to unselected state
        colorBeige.setBackgroundResource(R.drawable.dmq_color_text_unselected);
        colorBlack.setBackgroundResource(R.drawable.dmq_color_text_unselected);
        colorPink.setBackgroundResource(R.drawable.dmq_color_text_unselected);

        // Set selected color
        if ("Beige".equals(selectedColor)) {
            colorBeige.setBackgroundResource(R.drawable.dmq_color_text_selector);
        } else if ("Đen".equals(selectedColor)) {
            colorBlack.setBackgroundResource(R.drawable.dmq_color_text_selector);
        } else if ("Đỏ".equals(selectedColor)) {
            colorPink.setBackgroundResource(R.drawable.dmq_color_text_selector);
        }
    }

    private void updateSizeUI(String selectedSize) {
        // Reset all sizes to unselected state
        sizeS.setBackgroundResource(R.drawable.dmq_size_circle_unselected);
        sizeS.setTextColor(getResources().getColor(R.color.black));
        sizeM.setBackgroundResource(R.drawable.dmq_size_circle_unselected);
        sizeM.setTextColor(getResources().getColor(R.color.black));
        sizeL.setBackgroundResource(R.drawable.dmq_size_circle_unselected);
        sizeL.setTextColor(getResources().getColor(R.color.black));

        // Set selected size
        if ("S".equals(selectedSize)) {
            sizeS.setBackgroundResource(R.drawable.dmq_size_circle_selected);
            sizeS.setTextColor(getResources().getColor(R.color.white));
        } else if ("M".equals(selectedSize)) {
            sizeM.setBackgroundResource(R.drawable.dmq_size_circle_selected);
            sizeM.setTextColor(getResources().getColor(R.color.white));
        } else if ("L".equals(selectedSize)) {
            sizeL.setBackgroundResource(R.drawable.dmq_size_circle_selected);
            sizeL.setTextColor(getResources().getColor(R.color.white));
        }
    }
}
