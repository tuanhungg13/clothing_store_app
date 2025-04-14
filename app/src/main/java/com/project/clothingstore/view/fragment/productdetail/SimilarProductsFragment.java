//package com.project.clothingstore.view.fragment.productdetail;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.project.clothingstore.R;
//import com.project.clothingstore.adapter.productdetail.SimilarProductAdapter;
//import com.project.clothingstore.viewmodel.ProductDetailViewModel;
//
//import java.util.ArrayList;
//
//public class SimilarProductsFragment extends Fragment {
//    private ProductDetailViewModel viewModel;
//    private RecyclerView recyclerSimilarProducts;
//    private SimilarProductAdapter adapter;
//    private ImageView imgExpandSimilar;
//    private LinearLayout layoutSimilarProducts;
//    private boolean isExpanded = true; // Start expanded
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_similar_products, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        // Initialize views
//        recyclerSimilarProducts = view.findViewById(R.id.recyclerSimilarProducts);
//        imgExpandSimilar = view.findViewById(R.id.imgExpandSimilar);
//        layoutSimilarProducts = view.findViewById(R.id.layoutSimilarProducts);
//
//        // Get shared ViewModel
//        viewModel = new ViewModelProvider(requireActivity()).get(ProductDetailViewModel.class);
//
//        // Setup RecyclerView
//        adapter = new SimilarProductAdapter(new ArrayList<>(), productId -> {
//            // Handle product click - navigate to that product's detail
//            // In a real app, you'd use Navigation component or an interface callback
//        });
//
//        recyclerSimilarProducts.setLayoutManager(
//                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//        recyclerSimilarProducts.setAdapter(adapter);
//
//        // Setup click listeners
//        layoutSimilarProducts.setOnClickListener(v -> toggleSimilarProductsExpansion());
//        imgExpandSimilar.setOnClickListener(v -> toggleSimilarProductsExpansion());
//
//        // Observe similar products data
//        viewModel.getSimilarProducts().observe(getViewLifecycleOwner(), products -> {
//            if (products != null) {
//                adapter.updateProducts(products);
//            }
//        });
//
//        // Set initial expansion state
//        updateExpansionUI();
//    }
//
//    private void toggleSimilarProductsExpansion() {
//        isExpanded = !isExpanded;
//        updateExpansionUI();
//    }
//
//    private void updateExpansionUI() {
//        recyclerSimilarProducts.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
//        imgExpandSimilar.setRotation(isExpanded ? 180 : 0);
//    }
//}


package com.project.clothingstore.view.fragment.productdetail;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.clothingstore.R;
import com.project.clothingstore.adapter.productdetail.SimilarProductAdapter;
import com.project.clothingstore.modal.Product;
import com.project.clothingstore.view.activity.ProductDetailActivity;
import com.project.clothingstore.viewmodel.ProductDetailViewModel;

import java.util.ArrayList;

public class SimilarProductsFragment extends Fragment implements SimilarProductAdapter.OnProductClickListener {
    private ProductDetailViewModel viewModel;
    private RecyclerView recyclerSimilarProducts;
    private SimilarProductAdapter adapter;
    private ImageView imgExpandSimilar;
    private LinearLayout layoutSimilarProducts;
    private boolean isExpanded = true; // Start expanded
    private String categoryId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_similar_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get category ID from arguments
        Bundle args = getArguments();
        if (args != null) {
            categoryId = args.getString("data");
        }

        // Initialize views
        recyclerSimilarProducts = view.findViewById(R.id.recyclerSimilarProducts);
        imgExpandSimilar = view.findViewById(R.id.imgExpandSimilar);
        layoutSimilarProducts = view.findViewById(R.id.layoutSimilarProducts);

        // Get shared ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(ProductDetailViewModel.class);

        // Setup RecyclerView
        adapter = new SimilarProductAdapter(new ArrayList<>(), this);
        recyclerSimilarProducts.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerSimilarProducts.setAdapter(adapter);

        // Setup click listeners
        layoutSimilarProducts.setOnClickListener(v -> toggleSimilarProductsExpansion());
        imgExpandSimilar.setOnClickListener(v -> toggleSimilarProductsExpansion());

        // Observe similar products data
        viewModel.getSimilarProducts().observe(getViewLifecycleOwner(), products -> {
            if (products != null && !products.isEmpty()) {
                adapter.updateProducts(products);
                view.setVisibility(View.VISIBLE); // Show only if we have similar products
            } else {
                view.setVisibility(View.GONE); // Hide if no similar products
            }
        });

        // Set initial expansion state
        updateExpansionUI();

        // Load similar products if needed
        if (categoryId != null && !categoryId.isEmpty()) {
            viewModel.loadSimilarProducts(categoryId);
        }
    }

    @Override
    public void onProductClick(String productId) {
        // Navigate to product detail for the clicked product
        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
        intent.putExtra("productId", productId);
        startActivity(intent);
    }

    private void toggleSimilarProductsExpansion() {
        isExpanded = !isExpanded;
        updateExpansionUI();
    }

    private void updateExpansionUI() {
        recyclerSimilarProducts.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        imgExpandSimilar.setRotation(isExpanded ? 180 : 0);
    }
}