package com.project.clothingstore.view.fragment.intro;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.project.clothingstore.MainActivity;
import com.project.clothingstore.R;
import com.project.clothingstore.adapter.IntroAdapter;
import com.project.clothingstore.modal.IntroItem;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;
import java.util.List;

public class IntroFragment extends Fragment {

    private ViewPager2 viewPager;

    public IntroFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_intro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = view.findViewById(R.id.viewPager);
        WormDotsIndicator dotsIndicator = view.findViewById(R.id.dots_indicator);
        Button btnStart = view.findViewById(R.id.btnStart);

        List<IntroItem> itemList = new ArrayList<>();
        itemList.add(new IntroItem(R.drawable.intro1, "Khám phá điều mới mẻ", "Sản phẩm đặc biệt dành riêng cho bạn"));
        itemList.add(new IntroItem(R.drawable.intro2, "Trang phục thời thượng", "Thương hiệu yêu thích và xu hướng nóng nhất"));
        itemList.add(new IntroItem(R.drawable.intro3, "Phong cách", "Trang phục độc đáo, tỏa sáng"));

        viewPager.setAdapter(new IntroAdapter(itemList));

        // Gắn ViewPager2 vào DotsIndicator
        dotsIndicator.attachTo(viewPager);

        btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });
    }
}
