package com.project.clothingstore.view.fragment.Filter;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.clothingstore.R;
import com.project.clothingstore.view.fragment.discover.Discover0Fragment;
import com.project.clothingstore.view.fragment.discover.Discover1Fragment;
import com.project.clothingstore.view.fragment.product.FeaturedProductFragment;


public class SearchFilterFragment extends Fragment {

    private boolean isFragmentVisible0 = false; // Biến kiểm tra trạng thái fragment con
    private boolean isFragmentVisible1 = false; // Biến kiểm tra trạng thái fragment con
    private CardView Type0, Type1;
    private View fragmentContainer0, fragmentContainer1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_filter, container, false);

        Type0 = view.findViewById(R.id.card0);
        Type1 = view.findViewById(R.id.card1);


        fragmentContainer0 = view.findViewById(R.id.fragment_discover0);
        fragmentContainer1 = view.findViewById(R.id.fragment_discover1);

        // Ẩn tất cả các container fragment ban đầu
        fragmentContainer0.setVisibility(View.GONE);
        fragmentContainer1.setVisibility(View.GONE);

        Type0.setOnClickListener(v -> toggleChildFragment(0, fragmentContainer0, R.id.fragment_discover0));
        Type1.setOnClickListener(v -> toggleChildFragment(1, fragmentContainer1, R.id.fragment_discover1));

        if(savedInstanceState == null) {
            loadFragmentFeaturedProduct(FeaturedProductFragment.newInstance(8));
        }

        return view;
    }

    private void loadFragmentFeaturedProduct(Fragment fragment) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_discover_product, fragment)  // Ensure container ID is correct
                .commit();
    }

    private void toggleChildFragment(int type, View fragment, int IDfragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        boolean isFragmentVisible;
        Fragment newFragment = null;

        switch (type) {
            case 0:
                isFragmentVisible = isFragmentVisible0;
                newFragment = new Discover0Fragment();
                isFragmentVisible0 = !isFragmentVisible0;
                break;
            case 1:
                isFragmentVisible = isFragmentVisible1;
                newFragment = new Discover1Fragment();
                isFragmentVisible1 = !isFragmentVisible1;
                break;
            default: return; // Không làm gì nếu type không hợp lệ
        }

        if (!isFragmentVisible) {
            // Nếu chưa hiển thị, thêm DiscoverFragment vào container
            Bundle bundle = new Bundle();
            bundle.putInt("type", type);
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