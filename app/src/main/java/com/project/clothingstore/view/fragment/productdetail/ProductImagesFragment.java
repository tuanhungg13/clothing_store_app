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

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.project.clothingstore.R;
import com.project.clothingstore.adapter.productdetail.ProductImageAdapter;
import com.project.clothingstore.viewmodel.ProductDetailViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductImagesFragment extends Fragment {
    private ProductDetailViewModel viewModel;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
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
        tabLayout = view.findViewById(R.id.tabLayoutIndicator);

        // Get shared ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(ProductDetailViewModel.class);

        // Setup adapter with empty list initially
        adapter = new ProductImageAdapter(new ArrayList<>());
        viewPager.setAdapter(adapter);

        // Connect TabLayout with ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // No text for tab indicators
        }).attach();

        tabLayout.post(() -> {
            ViewGroup tabStrip = (ViewGroup) tabLayout.getChildAt(0);
            for (int i = 0; i < tabStrip.getChildCount(); i++) {
                View tabView = tabStrip.getChildAt(i);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) tabView.getLayoutParams();
                params.setMargins(10, 0, 10, 0); // Điều chỉnh khoảng cách giữa các tab
                tabView.setLayoutParams(params);
                tabView.requestLayout();
            }
        });

        // Observe product data
        viewModel.getProduct().observe(getViewLifecycleOwner(), product -> {
            if (product != null && product.getImages() != null) {
                adapter.updateImages(product.getImages());
            }
        });



    }
}