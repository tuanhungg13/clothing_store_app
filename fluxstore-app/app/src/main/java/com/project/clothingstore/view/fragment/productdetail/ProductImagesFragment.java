package com.project.clothingstore.view.fragment.productdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.project.clothingstore.R;
import com.project.clothingstore.adapter.productdetail.ProductImageAdapter;
import com.project.clothingstore.viewmodel.ProductDetailViewModel;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;

public class ProductImagesFragment extends Fragment {
    private ProductDetailViewModel viewModel;
    private ViewPager2 viewPager;
    private CircleIndicator3 circleIndicator;
    private ProductImageAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_images, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        viewPager = view.findViewById(R.id.viewPagerProductImages);
        circleIndicator = view.findViewById(R.id.circleIndicatorProductImages);

        // Get shared ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(ProductDetailViewModel.class);

        // Setup adapter with empty list initially
        adapter = new ProductImageAdapter(new ArrayList<>());
        viewPager.setAdapter(adapter);

        // Observe product data
        viewModel.getProduct().observe(getViewLifecycleOwner(), product -> {
            if (product != null && product.getImages() != null && !product.getImages().isEmpty()) {
                adapter.updateImages(product.getImages());

                // Đặt lại Indicator sau khi dữ liệu được cập nhật
                circleIndicator.setViewPager(viewPager);
            }
        });
    }
}
