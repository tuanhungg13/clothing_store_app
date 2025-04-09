package com.project.clothingstore.service;

import com.project.clothingstore.R;
import com.project.clothingstore.modal.ProductCollections;

import java.util.ArrayList;
import java.util.List;

public class ProductCollectionService {

    // Fake API
    public List<ProductCollections> getListBST(){
        List<ProductCollections> list = new ArrayList<>();
        list.add(new ProductCollections(R.drawable.aophong, "Bộ sưu tập mùa thu năm 2025"));
        list.add(new ProductCollections(R.drawable.spnb, "Bộ sưu tập mùa đông năm 2025"));
        list.add(new ProductCollections(R.drawable.spnb, "Bộ sưu tập mùa xuân năm 2025"));
        return list;
    }

    public List<ProductCollections> getListBSTNew(){
        List<ProductCollections> list = new ArrayList<>();
        list.add(new ProductCollections(R.drawable.mau2, "Bộ sưu tập mới ", "Đi chơi & Tiệc tùng"));
        list.add(new ProductCollections(R.drawable.mau1, "Bộ sưu tập đặc biệt ", "Du lịch"));
        list.add(new ProductCollections(R.drawable.mau2, "Bộ sưu tập cũ ", "Thể thao"));
        list.add(new ProductCollections(R.drawable.mau1, "Bộ sưu tập độc quyền ", "Kết hôn"));
        list.add(new ProductCollections(R.drawable.mau2, "Bộ sưu tập táo bạo ", "Kỷ niệm"));
        return list;
    }

    public List<ProductCollections> getListProductCategory3() {
        List<ProductCollections> list = new ArrayList<>();
        list.add(new ProductCollections(R.drawable.mau1, "Giảm giá tới 40%", "THON GỌN & XINH DẸP"));
        list.add(new ProductCollections(R.drawable.longlay, "Bộ sưu tập mùa hè", "Thiết kế gợi cảm & lộng lẫy nhất"));
        list.add(new ProductCollections(R.drawable.congso, "Áo phông", "Công sở"));
        list.add(new ProductCollections(R.drawable.vaysangtrong, "Váy", "Thiết kế sang trọng"));
        return list;
    }
}
