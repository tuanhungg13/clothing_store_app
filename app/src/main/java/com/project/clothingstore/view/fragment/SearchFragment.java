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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.project.clothingstore.R;
import com.project.clothingstore.view.fragment.Filter.FilterFragment;
import com.project.clothingstore.view.fragment.discover.Discover0Fragment;
import com.project.clothingstore.view.fragment.discover.Discover1Fragment;
import com.project.clothingstore.view.fragment.discover.Discover2Fragment;
import com.project.clothingstore.view.fragment.discover.Discover3Fragment;

public class SearchFragment extends Fragment {
    private boolean isFragmentVisible0 = false; // Biến kiểm tra trạng thái fragment con
    private boolean isFragmentVisible1 = false; // Biến kiểm tra trạng thái fragment con
    private boolean isFragmentVisible2 = false; // Biến kiểm tra trạng thái fragment con
    private boolean isFragmentVisible3 = false; // Biến kiểm tra trạng thái fragment con
    private CardView Type0, Type1, Type2, Type3;
    private View fragmentContainer0, fragmentContainer1, fragmentContainer2, fragmentContainer3;

    private ImageButton imgbtn_filter;
    private DrawerLayout drawerLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        // Inflate the layout for this fragment
        Type0 = view.findViewById(R.id.card0);
        Type1 = view.findViewById(R.id.card1);
        Type2 = view.findViewById(R.id.card2);
        Type3 = view.findViewById(R.id.card3);

        fragmentContainer0 = view.findViewById(R.id.fragment_discover0);
        fragmentContainer1 = view.findViewById(R.id.fragment_discover1);
        fragmentContainer2 = view.findViewById(R.id.fragment_discover2);
        fragmentContainer3 = view.findViewById(R.id.fragment_discover3);

        imgbtn_filter = view.findViewById(R.id.imgbtn_filter_search);
        drawerLayout = view.findViewById(R.id.drawer_layout_search);
        // Ẩn tất cả các container fragment ban đầu
        fragmentContainer0.setVisibility(View.GONE);
        fragmentContainer1.setVisibility(View.GONE);
        fragmentContainer2.setVisibility(View.GONE);
        fragmentContainer3.setVisibility(View.GONE);
        Type0.setOnClickListener(v -> toggleChildFragment("0", fragmentContainer0, R.id.fragment_discover0));
        Type1.setOnClickListener(v -> toggleChildFragment("1", fragmentContainer1, R.id.fragment_discover1));
        Type2.setOnClickListener(v -> toggleChildFragment("2", fragmentContainer2, R.id.fragment_discover2));
        Type3.setOnClickListener(v -> toggleChildFragment("3", fragmentContainer3, R.id.fragment_discover3));

        imgbtn_filter.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END);
            } else {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        if(savedInstanceState == null) {
            loadFragmentFilter(new FilterFragment());
        }



        return view;
    }

    private void loadFragmentFilter(Fragment fragment) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_filter, fragment)  // Ensure container ID is correct
                .commit();
    }

    private void toggleChildFragment(String type, View fragment, int IDfragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        boolean isFragmentVisible;
        Fragment newFragment = null;

        switch (type) {
            case "0":
                isFragmentVisible = isFragmentVisible0;
                newFragment = new Discover0Fragment();
                isFragmentVisible0 = !isFragmentVisible0;
                break;
            case "1":
                isFragmentVisible = isFragmentVisible1;
                newFragment = new Discover1Fragment();
                isFragmentVisible1 = !isFragmentVisible1;
                break;
            case "2":
                isFragmentVisible = isFragmentVisible2;
                newFragment = new Discover2Fragment();
                isFragmentVisible2 = !isFragmentVisible2;
                break;
            case "3":
                isFragmentVisible = isFragmentVisible3;
                newFragment = new Discover3Fragment();
                isFragmentVisible3 = !isFragmentVisible3;
                break;
            default: return; // Không làm gì nếu type không hợp lệ
        }

        if (!isFragmentVisible) {
            // Nếu chưa hiển thị, thêm DiscoverFragment vào container
            Bundle bundle = new Bundle();
            bundle.putString("type", type);
            newFragment.setArguments(bundle);

            transaction.replace(IDfragment, newFragment, "ChildFragment");
            transaction.commitAllowingStateLoss();

            // Hiển thị container chứa fragment
            fragment.setVisibility(View.VISIBLE);
        } else {
            // Nếu đang hiển thị, xóa fragment con
            Fragment existingFragment = fragmentManager.findFragmentById(IDfragment);
            if (existingFragment != null) {
                transaction.remove(existingFragment);
                transaction.commitAllowingStateLoss();
            }

            // Ẩn container chứa fragment
            fragment.setVisibility(View.GONE);
        }


    }





}