package com.project.clothingstore.view.fragment.productcollection;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.clothingstore.R;
import com.project.clothingstore.adapter.productcollections.ProductCollectionAdapter;
import com.project.clothingstore.viewmodel.productcollections.ProductCollectionViewModel;

import me.relex.circleindicator.CircleIndicator3;


public class ProductCollectionFragment extends Fragment {
    private ViewPager2 mviewPager2;
    private CircleIndicator3 mcircleIndicator3;
    private ProductCollectionAdapter adapterBST;
    private ProductCollectionViewModel boSuuTapViewModel;
    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mviewPager2.getCurrentItem() == adapterBST.getItemCount() - 1) {
                mviewPager2.setCurrentItem(0);
            } else {
                mviewPager2.setCurrentItem(mviewPager2.getCurrentItem() + 1);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_collection, container, false);
        mviewPager2 = view.findViewById(R.id.vp2_product_category);
        mcircleIndicator3 = view.findViewById(R.id.cicle_BST);

        // Khởi tạo ViewModel
        boSuuTapViewModel = new ViewModelProvider(this).get(ProductCollectionViewModel.class);

        // Khởi tạo Adapter với danh sách trống
        adapterBST = new ProductCollectionAdapter();
        mviewPager2.setAdapter(adapterBST);
        mcircleIndicator3.setViewPager(mviewPager2);

        // Lắng nghe dữ liệu từ ViewModel
        boSuuTapViewModel.getListProductCategory().observe(getViewLifecycleOwner(), list -> {
            adapterBST.setData(list);
            mcircleIndicator3.createIndicators(list.size(), 0);
        });

        mviewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 5000);
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 5000);
    }
}