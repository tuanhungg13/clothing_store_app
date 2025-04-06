package com.project.clothingstore.view.fragment.discover;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.clothingstore.R;
import com.project.clothingstore.adapter.productcategories.ProductCategoriesAdapter;
import com.project.clothingstore.viewmodel.discover.ProductCategoriesViewModal;

public class Discover2Fragment extends Fragment {
    private ProductCategoriesAdapter productCategoriesAdapter;
    private ProductCategoriesViewModal productCategoriesViewModal;

    private RecyclerView recyclerView;

    private String type;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover2, container, false);
        if (getArguments() != null) {
            type = getArguments().getString("type");
        }
        recyclerView = view.findViewById(R.id.rcv_fragment_discover2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        productCategoriesAdapter = new ProductCategoriesAdapter();
        recyclerView.setAdapter(productCategoriesAdapter);

        productCategoriesViewModal = new ProductCategoriesViewModal();
        productCategoriesViewModal.getListProductCategory(type).observe(getViewLifecycleOwner(), list -> {
            productCategoriesAdapter.setData(list);
        });
        return view;
    }
}