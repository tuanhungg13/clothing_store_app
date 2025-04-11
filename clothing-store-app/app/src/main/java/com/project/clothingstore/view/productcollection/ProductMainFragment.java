package com.project.clothingstore.view.productcollection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.project.clothingstore.R;
import com.project.clothingstore.view.product.FeaturedProductFragment;
import com.project.clothingstore.view.product.RecommentProductFragment;

public class ProductMainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_main, container, false);

        if (savedInstanceState == null) {
            loadFragment(new ProductCollectionFragment());
            loadFragmentFeaturedProduct(new FeaturedProductFragment());
            loadFragmentCategory2(new ProductCollectionFragment2());
            loadFragmentRecommentProduct(new RecommentProductFragment());
            loadFragmentCategory3(new ProductCollectionFragment3());
        }

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
