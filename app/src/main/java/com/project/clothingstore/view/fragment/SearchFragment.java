package com.project.clothingstore.view.fragment;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.project.clothingstore.R;
import com.project.clothingstore.view.fragment.Filter.FilterFragment;
import com.project.clothingstore.view.fragment.Filter.SearchFilterFragment;
import com.project.clothingstore.view.fragment.discover.Discover0Fragment;
import com.project.clothingstore.view.fragment.discover.Discover1Fragment;
import com.project.clothingstore.view.fragment.product.FeaturedProductFragment;

public class SearchFragment extends Fragment {
    private ImageButton imgbtn_filter;
    private DrawerLayout drawerLayout;
    private int categoryId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
//        // Inflate the layout for this fragment


        imgbtn_filter = view.findViewById(R.id.imgbtn_filter_search);
        drawerLayout = view.findViewById(R.id.drawer_layout_search);
        // Ẩn tất cả các container fragment ban đầu

        imgbtn_filter.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END);
            } else {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        if(savedInstanceState == null) {
            loadFragmentFilter(new FilterFragment());
            loadFragment(new SearchFilterFragment());
        }



        return view;
    }

    private void loadFragmentFilter(Fragment fragment) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_filter, fragment)  // Ensure container ID is correct
                .commit();
    }

    private void loadFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_product_discount, fragment)  // Ensure container ID is correct
                .commit();
    }


}