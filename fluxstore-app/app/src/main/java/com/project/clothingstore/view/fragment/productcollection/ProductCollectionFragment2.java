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
import com.project.clothingstore.adapter.productcollections.ProductCollectionAdapter2;
import com.project.clothingstore.viewmodel.productcollections.ProductCollectionViewModel2;

import me.relex.circleindicator.CircleIndicator3;


public class ProductCollectionFragment2 extends Fragment {

    private ViewPager2 mviewPager2;
    private CircleIndicator3 mcircleIndicator3;
    private ProductCollectionAdapter2 adapterBST2;
    private ProductCollectionViewModel2 boSuuTapViewModel2;
    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mviewPager2.getCurrentItem() == adapterBST2.getItemCount() - 1) {
                mviewPager2.setCurrentItem(0);
            } else {
                mviewPager2.setCurrentItem(mviewPager2.getCurrentItem() + 1);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_collections2, container, false);
        mviewPager2 = view.findViewById(R.id.vp2_bo_suu_tap_new);
        mcircleIndicator3 = view.findViewById(R.id.cicle_BST_new);

        // Khởi tạo ViewModel
        boSuuTapViewModel2 = new ViewModelProvider(this).get(ProductCollectionViewModel2.class);

        // Khởi tạo Adapter với danh sách trống
        adapterBST2 = new ProductCollectionAdapter2();
        mviewPager2.setAdapter(adapterBST2);
        mcircleIndicator3.setViewPager(mviewPager2);

        // Lắng nghe dữ liệu từ ViewModel
        boSuuTapViewModel2.getListProductCategory().observe(getViewLifecycleOwner(), list -> {
            adapterBST2.setData(list);
            mcircleIndicator3.createIndicators(list.size(), 0);
        });

        mviewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 5500);
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
        handler.postDelayed(runnable, 5500);
    }
}