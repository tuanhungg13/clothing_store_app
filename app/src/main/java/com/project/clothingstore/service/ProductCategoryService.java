package com.project.clothingstore.service;

import com.project.clothingstore.R;
import com.project.clothingstore.modal.ProductCategory;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryService {

    // Fake API
    public List<ProductCategory> getListBST(){
        List<ProductCategory> list = new ArrayList<>();
        list.add(new ProductCategory(R.drawable.spnb, "Bộ sưu tập mùa thu năm 2025"));
        list.add(new ProductCategory(R.drawable.spnb, "Bộ sưu tập mùa đông năm 2025"));
        list.add(new ProductCategory(R.drawable.spnb, "Bộ sưu tập mùa xuân năm 2025"));
        return list;
    }

    public List<ProductCategory> getListBSTNew(){
        List<ProductCategory> list = new ArrayList<>();
        list.add(new ProductCategory(R.drawable.mau2, "Bộ sưu tập mới ", "Đi chơi & Tiệc tùng"));
        list.add(new ProductCategory(R.drawable.mau1, "Bộ sưu tập đặc biệt ", "Du lịch"));
        list.add(new ProductCategory(R.drawable.mau2, "Bộ sưu tập cũ ", "Thể thao"));
        list.add(new ProductCategory(R.drawable.mau1, "Bộ sưu tập độc quyền ", "Kết hôn"));
        list.add(new ProductCategory(R.drawable.mau2, "Bộ sưu tập táo bạo ", "Kỷ niệm"));
        return list;
    }

    public List<ProductCategory> getListProductCategory3() {
        List<ProductCategory> list = new ArrayList<>();
        list.add(new ProductCategory(R.drawable.mau1, "Giảm giá tới 40%", "THON GỌN & XINH DẸP"));
        list.add(new ProductCategory(R.drawable.longlay, "Bộ sưu tập mùa hè", "Thiết kế gợi cảm & lộng lẫy nhất"));
        list.add(new ProductCategory(R.drawable.congso, "Áo phông", "Công sở"));
        list.add(new ProductCategory(R.drawable.vaysangtrong, "Váy", "Thiết kế sang trọng"));
        return list;
    }
}
