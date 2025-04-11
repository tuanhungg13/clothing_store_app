package com.project.clothingstore.view.fragment.productdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.project.clothingstore.R;
import com.project.clothingstore.viewmodel.ProductDetailViewModel;

public class ProductDescriptionFragment extends Fragment {
    private ProductDetailViewModel viewModel;
    private TextView tvDescription, tvReadMore;
    private ImageView imgExpandDescription;
    private LinearLayout layoutDescription;
    private boolean isExpanded = false;
    private String fullDescription = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_description, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        tvDescription = view.findViewById(R.id.tvDescription);
        tvReadMore = view.findViewById(R.id.tvReadMore);
        imgExpandDescription = view.findViewById(R.id.imgExpandDescription);
        layoutDescription = view.findViewById(R.id.layoutDescription);

        // Get shared ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(ProductDetailViewModel.class);

        // Setup click listeners
        layoutDescription.setOnClickListener(v -> toggleDescriptionExpansion());
        imgExpandDescription.setOnClickListener(v -> toggleDescriptionExpansion());
        tvReadMore.setOnClickListener(v -> toggleDescriptionExpansion());

        // Observe product data
        viewModel.getProduct().observe(getViewLifecycleOwner(), product -> {
            if (product != null && product.getDescription() != null) {
                fullDescription = product.getDescription();
                updateDescriptionText();
            }
        });
    }

    private void toggleDescriptionExpansion() {
        isExpanded = !isExpanded;
        updateDescriptionText();
        imgExpandDescription.setRotation(isExpanded ? 180 : 0);
    }

    private void updateDescriptionText() {
        if (isExpanded) {
            tvDescription.setText(android.text.Html.fromHtml(fullDescription));
            tvReadMore.setVisibility(View.GONE);
        } else {
            // Show truncated description if it's long enough
            if (fullDescription.length() > 100) {
                String fullDes = fullDescription.substring(0, 100) + "...";
                tvDescription.setText(android.text.Html.fromHtml(fullDes));
                tvReadMore.setVisibility(View.VISIBLE);
            } else {
                tvDescription.setText(fullDescription);
                tvReadMore.setVisibility(View.GONE);
            }
        }
    }
}
