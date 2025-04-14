package com.project.clothingstore.view.fragment.productcollection;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.clothingstore.R;
import com.project.clothingstore.adapter.productcollections.ProductCollectionAdapter3;
import com.project.clothingstore.viewmodel.productcollections.ProductCollectionViewModel3;

public class ProductCollectionFragment3 extends Fragment {

    private ProductCollectionAdapter3 adapterBST3;
    private ProductCollectionViewModel3 boSuuTapViewModel3;
    private RecyclerView rcv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_collection3, container, false);
        rcv = view.findViewById(R.id.rcv_product_category3);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcv.setLayoutManager(layoutManager);

        adapterBST3 = new ProductCollectionAdapter3();
        rcv.setAdapter(adapterBST3);

        boSuuTapViewModel3 = new ViewModelProvider(this).get(ProductCollectionViewModel3.class);
        boSuuTapViewModel3.getListProductCategory().observe(getViewLifecycleOwner(), list -> {
            adapterBST3.setData(list);
        });
        return view;
    }

}