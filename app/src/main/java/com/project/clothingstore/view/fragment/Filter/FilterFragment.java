package com.project.clothingstore.view.fragment.Filter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.project.clothingstore.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterFragment extends Fragment {

    String[] categotyitems = {"Áo & quần", "Giày", "Phụ kiện", "Làm đẹp"};
    ArrayAdapter<String> adapterItems;

    private AutoCompleteTextView autoCompleteTxt;


    Set<Integer> selectedColors = new HashSet<>();
    Set<Integer> selectedStar = new HashSet<>();
    Set<Integer> selectedDiscount = new HashSet<>();

    List<FrameLayout> framescolor = new ArrayList<>();
    List<FrameLayout> framesstar = new ArrayList<>();
    List<FrameLayout> framesdiscount = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        autoCompleteTxt = view.findViewById(R.id.auto_category_filter);
        adapterItems = new ArrayAdapter<>(getContext(), R.layout.hvq_list_items, categotyitems);
        autoCompleteTxt.setAdapter(adapterItems);

        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
            }
        });

        framescolor.add(view.findViewById(R.id.color_frame0));
        framescolor.add(view.findViewById(R.id.color_frame1));
        framescolor.add(view.findViewById(R.id.color_frame2));
        framescolor.add(view.findViewById(R.id.color_frame3));
        framescolor.add(view.findViewById(R.id.color_frame4));
        framescolor.add(view.findViewById(R.id.color_frame5));
        framescolor.add(view.findViewById(R.id.color_frame6));

        framesstar.add(view.findViewById(R.id.star_frame1));
        framesstar.add(view.findViewById(R.id.star_frame2));
        framesstar.add(view.findViewById(R.id.star_frame3));
        framesstar.add(view.findViewById(R.id.star_frame4));
        framesstar.add(view.findViewById(R.id.star_frame5));

        framesdiscount.add(view.findViewById(R.id.discount_frame0));
        framesdiscount.add(view.findViewById(R.id.discount_frame1));
        framesdiscount.add(view.findViewById(R.id.discount_frame2));
        framesdiscount.add(view.findViewById(R.id.discount_frame3));

        // Gán sự kiện click cho từng frame
        setupFrameClick(framescolor, selectedColors);
        setupFrameClick(framesstar, selectedStar);
        setupFrameClick(framesdiscount, selectedDiscount);

        // Lặp và gán sự kiện click cho từng frame
//        for (int i = 0; i < framescolor.size(); i++) {
//            final int index = i;
//            FrameLayout frame = framescolor.get(i);
//
//            frame.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (selectedColors.contains(index)) {
//                        selectedColors.remove(index);
//                        v.setSelected(false);
//                    } else {
//                        selectedColors.add(index);
//                        v.setSelected(true);
//                    }
//                }
//            });
//        }
//
//        // Lặp và gán sự kiện click cho từng frame
//        for (int i = 0; i < framesstar.size(); i++) {
//            final int index = i;
//            FrameLayout frame = framesstar.get(i);
//
//            frame.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (selectedStar.contains(index)) {
//                        selectedStar.remove(index);
//                        v.setSelected(false);
//                    } else {
//                        selectedStar.add(index);
//                        v.setSelected(true);
//                    }
//                }
//            });
//        }
//
//        // Lặp và gán sự kiện click cho từng frame
//        for (int i = 0; i < framesdiscount.size(); i++) {
//            final int index = i;
//            FrameLayout frame = framesdiscount.get(i);
//
//            frame.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (selectedDiscount.contains(index)) {
//                        selectedDiscount.remove(index);
//                        v.setSelected(false);
//                    } else {
//                        selectedDiscount.add(index);
//                        v.setSelected(true);
//                    }
//                }
//            });
//        }
        return view;
    }

    private void setupFrameClick(List<FrameLayout> frames, Set<Integer> selectedSet) {
        for (int i = 0; i < frames.size(); i++) {
            final int index = i;
            FrameLayout frame = frames.get(i);
            frame.setOnClickListener(v -> {
                if (selectedSet.contains(index)) {
                    selectedSet.remove(index);
                    v.setSelected(false);
                } else {
                    selectedSet.add(index);
                    v.setSelected(true);
                }
            });
        }
    }

}