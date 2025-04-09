package com.project.clothingstore.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

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
    private TextView txt_xemthem_productFeatured, txt_xemthem_productRecomment;
    private int limitfreatureProduct = 5;
    private int limitRecommentProduct = 5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        btnao0 = view.findViewById(R.id.btnao0);
        btngiay1 = view.findViewById(R.id.btngiay1);
        btnphukien2 = view.findViewById(R.id.btnphukien2);
        btnlamdep3 = view.findViewById(R.id.btnlamdep3);

        txt_xemthem_productFeatured = view.findViewById(R.id.txt_xemthem_freatureProduct);
        txt_xemthem_productRecomment = view.findViewById(R.id.txt_xemthem_recommentProduct);

        if (savedInstanceState == null) {
            loadFragment(new ProductCollectionFragment());
            loadFragmentFeaturedProduct(FeaturedProductFragment.newInstance(limitfreatureProduct));
            loadFragmentCategory2(new ProductCollectionFragment2());
         loadFragmentRecommentProduct(RecommentProductFragment.newInstance(limitRecommentProduct));
            loadFragmentCategory3(new ProductCollectionFragment3());
        }
        txt_xemthem_productFeatured.setOnClickListener(v -> {
            limitfreatureProduct += 5;
            loadFragmentFeaturedProduct(FeaturedProductFragment.newInstance(limitfreatureProduct));
        });

        txt_xemthem_productRecomment.setOnClickListener(v -> {
            limitRecommentProduct += 5;
            loadFragmentRecommentProduct(RecommentProductFragment.newInstance(limitRecommentProduct));
        });


        btnao0.setOnClickListener(v -> openCategory("0"));
        btngiay1.setOnClickListener(v -> openCategory("1"));
        btnphukien2.setOnClickListener(v -> openCategory("2"));
        btnlamdep3.setOnClickListener(v -> openCategory("3"));

        return view;
    }

    private void openCategory(String categoryId) {
        Intent intent = new Intent(getActivity(), ProductsActivity.class);
        intent.putExtra("categoryId", categoryId);
        startActivity(intent);
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
