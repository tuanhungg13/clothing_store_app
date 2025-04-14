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
import com.project.clothingstore.view.fragment.Filter.SearchBarFragment;
import com.project.clothingstore.view.fragment.Filter.SearchFilterFragment;
import com.project.clothingstore.view.fragment.discover.Discover0Fragment;
import com.project.clothingstore.view.fragment.discover.Discover1Fragment;
import com.project.clothingstore.view.fragment.product.FeaturedProductFragment;

public class SearchFragment extends Fragment implements SearchBarFragment.OnFilterButtonClickListener {
    private DrawerLayout drawerLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        drawerLayout = view.findViewById(R.id.drawer_layout_search);


        if(savedInstanceState == null) {
            loadFragmentFilter(new FilterFragment());
            loadFragment(new SearchFilterFragment());
            loadFragmentSearchBar(new SearchBarFragment());
        }

        return view;
    }

    private void toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            drawerLayout.openDrawer(GravityCompat.END);
        }
    }

    @Override
    public void onFilterButtonClick() {
        toggleDrawer(); // Gọi mở/đóng drawer từ fragment con
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

    private void loadFragmentSearchBar(Fragment fragment) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_search_bar, fragment)  // Ensure container ID is correct
                .commit();
    }
    // Các hàm loadFragment giữ nguyên như cũ
}


