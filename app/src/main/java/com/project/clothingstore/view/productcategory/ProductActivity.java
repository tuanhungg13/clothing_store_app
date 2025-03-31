package com.project.clothingstore.view.productcategory;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.project.clothingstore.R;
import com.project.clothingstore.view.product.FeaturedProductFragment;
import com.project.clothingstore.view.product.RecommentProductFragment;

public class ProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product);
        if (savedInstanceState == null) {
            loadFragment(new ProductCategoryFragment());
            loadFragmentFeaturedProduct(new FeaturedProductFragment());
            loadFragmentCategory2(new ProductCategoryFragment2());
            loadFragmentRecommentProduct(new RecommentProductFragment());
            loadFragmentCategory3(new ProductCategoryFragment3());
//            loadFragmentSPNB(new SanPhanNoiBatFragment());
//            loadFragmentBSTNew(new BoSuuTapNewFragment());
//            loadFragmentSPDX(new SanPhamDeXuatFragment());
        }

    }
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_BST, fragment)
                .commit();
    }

    private void loadFragmentFeaturedProduct(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_SPNB, fragment)
                .commit();
    }

    private void loadFragmentCategory2(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_BSTNew, fragment)
                .commit();
    }

    private void loadFragmentRecommentProduct(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_recomment_product, fragment)
                .commit();
    }

    private void loadFragmentCategory3(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_ProductCategory3, fragment)
                .commit();
    }


}