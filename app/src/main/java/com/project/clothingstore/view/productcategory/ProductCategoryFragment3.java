package com.project.clothingstore.view.productcategory;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.clothingstore.R;
import com.project.clothingstore.adapter.product.RecommentProductAdapter;
import com.project.clothingstore.adapter.productcategory.ProductCategoryAdapter2;
import com.project.clothingstore.adapter.productcategory.ProductCategoryAdapter3;
import com.project.clothingstore.viewmodel.Product.RecommentProductViewModel;
import com.project.clothingstore.viewmodel.productcategory.ProductCategoryViewModel2;
import com.project.clothingstore.viewmodel.productcategory.ProductCategoryViewModel3;

import me.relex.circleindicator.CircleIndicator3;

public class ProductCategoryFragment3 extends Fragment {

    private ProductCategoryAdapter3 adapterBST3;
    private ProductCategoryViewModel3 boSuuTapViewModel3;
    private RecyclerView rcv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_category3, container, false);
        rcv = view.findViewById(R.id.rcv_product_category3);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcv.setLayoutManager(layoutManager);

        adapterBST3 = new ProductCategoryAdapter3();
        rcv.setAdapter(adapterBST3);

        boSuuTapViewModel3 = new ViewModelProvider(this).get(ProductCategoryViewModel3.class);
        boSuuTapViewModel3.getListProductCategory().observe(getViewLifecycleOwner(), list -> {
            adapterBST3.setData(list);
        });
        return view;
    }

}