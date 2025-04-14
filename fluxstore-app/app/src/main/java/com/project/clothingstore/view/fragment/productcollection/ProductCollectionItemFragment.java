package com.project.clothingstore.view.fragment.productcollection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.project.clothingstore.R;
import com.project.clothingstore.adapter.product.ProductAdapter;
import com.project.clothingstore.viewmodel.Product.ProductViewModel;
import com.project.clothingstore.viewmodel.productcollections.ProductCollectionItemsViewModel;

public class ProductCollectionItemFragment extends Fragment {
    private ProductCollectionItemsViewModel productcollectionItemsViewModel;
    private ProductAdapter productAdapter;
    private RecyclerView recyclerView;
    String collectionId;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("collectionId", collectionId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_collection_item, container, false);

        if (getArguments() != null) {
            collectionId = getArguments().getString("collectionId");
        }
        recyclerView = view.findViewById(R.id.rcv_fragment_product_collection_item);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        productAdapter = new ProductAdapter();
        recyclerView.setAdapter(productAdapter);

        productcollectionItemsViewModel = new ViewModelProvider(this).get(ProductCollectionItemsViewModel.class);
        // Load data from ViewModel
        productcollectionItemsViewModel.loadProduct(collectionId);
        productcollectionItemsViewModel.getListProduct().observe(getViewLifecycleOwner(), list -> {
            productAdapter.setData(list);
        });




        return view;
    }
}