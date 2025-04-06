package com.project.clothingstore.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import com.project.clothingstore.R;
import com.project.clothingstore.view.activity.ProductsActivity;
import com.project.clothingstore.view.fragment.product.FeaturedProductFragment;
import com.project.clothingstore.view.fragment.product.RecommentProductFragment;
import com.project.clothingstore.view.fragment.productcollection.ProductCollectionFragment;
import com.project.clothingstore.view.fragment.productcollection.ProductCollectionFragment2;
import com.project.clothingstore.view.fragment.productcollection.ProductCollectionFragment3;

public class HomeFragment extends Fragment {
    private ImageButton btnao0, btngiay1, btnphukien2, btnlamdep3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        btnao0 = view.findViewById(R.id.btnao0);
        btngiay1 = view.findViewById(R.id.btngiay1);
        btnphukien2 = view.findViewById(R.id.btnphukien2);
        btnlamdep3 = view.findViewById(R.id.btnlamdep3);

        if (savedInstanceState == null) {
            loadFragment(new ProductCollectionFragment());
            loadFragmentFeaturedProduct(new FeaturedProductFragment());
            loadFragmentCategory2(new ProductCollectionFragment2());
            loadFragmentRecommentProduct(new RecommentProductFragment());
            loadFragmentCategory3(new ProductCollectionFragment3());
        }

        btnao0.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProductsActivity.class);
            intent.putExtra("categoryId", "0");  // Category for Ao
            startActivity(intent);
        });

        btngiay1.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProductsActivity.class);
            intent.putExtra("categoryId", "1");  // Category for Giay
            startActivity(intent);
        });

        btnphukien2.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProductsActivity.class);
            intent.putExtra("categoryId", "2");  // Category for Phu Kien
            startActivity(intent);
        });

        btnlamdep3.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProductsActivity.class);
            intent.putExtra("categoryId", "3");  // Category for Lam Dep
            startActivity(intent);
        });

        return view;
    }

    private void loadFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_BST, fragment)  // Replace the correct container ID for each fragment
                .commit();
    }

    private void loadFragmentFeaturedProduct(Fragment fragment) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_SPNB, fragment)  // Ensure container ID is correct
                .commit();
    }

    private void loadFragmentCategory2(Fragment fragment) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_BSTNew, fragment)  // Ensure container ID is correct
                .commit();
    }

    private void loadFragmentRecommentProduct(Fragment fragment) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_recomment_product, fragment)  // Ensure container ID is correct
                .commit();
    }

    private void loadFragmentCategory3(Fragment fragment) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_ProductCategory3, fragment)  // Ensure container ID is correct
                .commit();
    }
}
