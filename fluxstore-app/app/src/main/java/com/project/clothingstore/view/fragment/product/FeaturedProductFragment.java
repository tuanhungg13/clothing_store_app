package com.project.clothingstore.view.fragment.product;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.project.clothingstore.adapter.product.FeatureProductAdapter;

import com.project.clothingstore.R;
import com.project.clothingstore.viewmodel.Product.FeaturedProductViewModel;


public class FeaturedProductFragment extends Fragment {
    private FeaturedProductViewModel productViewModel;
    private FeatureProductAdapter feautureProductAdapter;
    private RecyclerView recyclerView;
    private int limit = 5;

    // Factory method để tạo fragment với limit
    public static FeaturedProductFragment newInstance(int limit) {
        FeaturedProductFragment fragment = new FeaturedProductFragment();
        Bundle args = new Bundle();
        args.putInt("limit", limit);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            limit = getArguments().getInt("limit", 5);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_featured_product, container, false);

        recyclerView = view.findViewById(R.id.rcv_product_freatured);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        feautureProductAdapter = new FeatureProductAdapter();
        recyclerView.setAdapter(feautureProductAdapter);
        recyclerView.setHorizontalScrollBarEnabled(false); // Tắt thanh cuộn ngang

        productViewModel = new ViewModelProvider(this).get(FeaturedProductViewModel.class);
        productViewModel.loadProductWithLimit(limit); // Gọi phương thức để tải sản phẩm với limit
        productViewModel.getListProduct().observe(getViewLifecycleOwner(), list -> {
            feautureProductAdapter.setData(list);
        });

        return view;
    }
}