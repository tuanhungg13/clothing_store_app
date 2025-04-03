package com.project.clothingstore.view.product;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_featured_product, container, false);

        recyclerView = view.findViewById(R.id.rcv_product_freatured);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        feautureProductAdapter = new FeatureProductAdapter();
        recyclerView.setAdapter(feautureProductAdapter);

        productViewModel = new ViewModelProvider(this).get(FeaturedProductViewModel.class);
        productViewModel.getListProduct().observe(getViewLifecycleOwner(), list -> {
            feautureProductAdapter.setData(list);
        });

        return view;
    }
}