package com.project.clothingstore.view.fragment.product;

import android.os.Bundle;

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
import com.project.clothingstore.adapter.product.FeatureProductAdapter;
import com.project.clothingstore.adapter.product.ProductAdapter;
import com.project.clothingstore.viewmodel.Product.FeaturedProductViewModel;
import com.project.clothingstore.viewmodel.Product.ProductViewModel;

import java.util.ArrayList;

public class ProductFragment extends Fragment {
    private ProductViewModel productViewModel;
    private ProductAdapter productAdapter;
    private RecyclerView recyclerView;
    String categoryId, productName;
    int categoriType, minPrice, maxPrice;
    double rating;
    ArrayList<Integer> discountList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        if (getArguments() != null) {
            categoryId = getArguments().getString("categoryId");
            productName = getArguments().getString("productName");

            categoriType = getArguments().getInt("categoriType", -1);
            minPrice = getArguments().getInt("minPrice", -1);
            maxPrice = getArguments().getInt("maxPrice", -1);
            rating = getArguments().getDouble("rating", -1); // Nếu không có thì là -1
            discountList = getArguments().getIntegerArrayList("discountList");

        }

        Log.d("FilterInFragment", "productType: " + categoriType +
                "\nminPrice: " + minPrice
                + "\nmaxPrice: " + maxPrice
                + "\nrating: " + rating
                + "\ndiscountList: " + discountList
                + "\nproductName: " + productName);

        recyclerView = view.findViewById(R.id.rcv_product_items);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        productAdapter = new ProductAdapter();
        recyclerView.setAdapter(productAdapter);

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        if (categoryId != null) {
            productViewModel.loadProduct(categoryId);
        }else{
            productViewModel.loadFilteredProduct( categoriType, minPrice, maxPrice, rating, discountList, productName);
        }
//        else if (productName != null) {
//            productViewModel.loadProductByName(productName);
//        }else{
//            productViewModel.loadFilteredProduct( categoriType, minPrice, maxPrice, rating, discountList, productName);
//        }

        productViewModel.getListProduct().observe(getViewLifecycleOwner(), list -> {
            productAdapter.setData(list);
        });

        return view;
    }
}