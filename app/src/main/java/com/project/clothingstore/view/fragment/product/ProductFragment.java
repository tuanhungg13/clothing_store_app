package com.project.clothingstore.view.fragment.product;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.clothingstore.R;
import com.project.clothingstore.adapter.product.FeatureProductAdapter;
import com.project.clothingstore.adapter.product.ProductAdapter;
import com.project.clothingstore.viewmodel.Product.FeaturedProductViewModel;
import com.project.clothingstore.viewmodel.Product.ProductViewModel;

public class ProductFragment extends Fragment {
    private ProductViewModel productViewModel;
    private ProductAdapter productAdapter;
    private RecyclerView recyclerView;
    String categoryId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        if (getArguments() != null) {
            categoryId = getArguments().getString("categoryId");
        }

        recyclerView = view.findViewById(R.id.rcv_product_items);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        productAdapter = new ProductAdapter();
        recyclerView.setAdapter(productAdapter);

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        // Load data from ViewModel
        productViewModel.loadProduct(categoryId);
        productViewModel.getListProduct().observe(getViewLifecycleOwner(), list -> {
            productAdapter.setData(list);
        });

        return view;
    }
}