package com.project.clothingstore.view.fragment.product;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.clothingstore.R;
import com.project.clothingstore.adapter.product.FeatureProductAdapter;
import com.project.clothingstore.adapter.product.RecommentProductAdapter;
import com.project.clothingstore.viewmodel.Product.FeaturedProductViewModel;
import com.project.clothingstore.viewmodel.Product.RecommentProductViewModel;

public class RecommentProductFragment extends Fragment {
    private RecommentProductViewModel productViewModel;
    private RecommentProductAdapter recommentProductAdapter;
    private RecyclerView recyclerView;

    private int limit = 5;

    public static RecommentProductFragment newInstance(int limit) {
        RecommentProductFragment fragment = new RecommentProductFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recomment_product, container, false);

        recyclerView = view.findViewById(R.id.rcv_recomment_product);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        recommentProductAdapter = new RecommentProductAdapter();
        recyclerView.setAdapter(recommentProductAdapter);
        recyclerView.setHorizontalScrollBarEnabled(false); // Tắt thanh cuộn ngang

        productViewModel = new ViewModelProvider(this).get(RecommentProductViewModel.class);

        productViewModel.LoadProduct(limit); // Gọi phương thức để tải sản phẩm với limit
        productViewModel.getListProduct().observe(getViewLifecycleOwner(), list -> {
            recommentProductAdapter.setData(list);
        });

        return view;
    }
}