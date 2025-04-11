package com.project.clothingstore.service;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.clothingstore.modal.Product;
import com.project.clothingstore.utils.helper.FirebaseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductService {
    private final CollectionReference productRef;

    public ProductService() {
        productRef = FirebaseHelper.getProductCollection();
    }

    public ProductService(CollectionReference productRef) {
        this.productRef = productRef;
    }

    // ✅ Lấy tất cả sản phẩm
    public void getAllProducts(OnCompleteListener<QuerySnapshot> listener) {
        productRef.get().addOnCompleteListener(listener);
    }

    // ✅ Lấy sản phẩm theo ID
    public void getProductById(String productId, OnCompleteListener<DocumentSnapshot> listener) {
        productRef.document(productId).get().addOnCompleteListener(listener);
    }

    // ✅ Lọc sản phẩm theo category
    public void getProductsByCategory(String categoryId, OnCompleteListener<QuerySnapshot> listener) {
        productRef.whereEqualTo("categoryId", categoryId).get().addOnCompleteListener(listener);
    }

    // ✅ Tìm kiếm sản phẩm theo tên
    public void searchProductsByName(String keyword, OnCompleteListener<QuerySnapshot> listener) {
        productRef.orderBy("productName")
                .startAt(keyword)
                .endAt(keyword + "\uf8ff")
                .get().addOnCompleteListener(listener);
    }

    // ✅ Phân trang sản phẩm
    public void getProductsPaged(DocumentSnapshot lastDoc, int limit, OnCompleteListener<QuerySnapshot> listener) {
        Query query = productRef.orderBy("productName").limit(limit);
        if (lastDoc != null) {
            query = query.startAfter(lastDoc);
        }
        query.get().addOnCompleteListener(listener);
    }


    // Fake data (sau xóa)
    public List<Product> getSanPhamList() {
        List<Product> list = new ArrayList<>();
        List<String> images1 = new ArrayList<>();
        images1.add("https://imgs.search.brave.com/WH0EAoxRlNgLWFEGOgz-IgnpZf9JYyrBGYs0xeHVUys/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9wcm9k/dWN0LmhzdGF0aWMu/bmV0LzIwMDAwMDI3/MjA4MS9wcm9kdWN0/LzQ0MDlfNC40MDFf/NTk5MDAwX3dlYl9h/ZGQyZWZlNzljMzA0/YTc2OGQwZTgyMTI3/YTExNGJkNF9ncmFu/ZGUuanBn");
        list.add(new Product("Áo len", 39, Arrays.asList("https://imgs.search.brave.com/D2e42mhBDr38wRWcxwtQLjkjoJskWXMIXRyoOYSLUMI/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9maWxl/LmhzdGF0aWMubmV0/LzEwMDAxNzg3Nzkv/ZmlsZS9hb19sZW5f/Y29fbG9fbnUuanBn")));
        list.add(new Product("Áo dài", 45, Arrays.asList("https://imgs.search.brave.com/NALIw-mSjKwuO0a0zIUbPIoxBcyF_cqYvoYRqlckWEI/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly90YWls/b2N3ZWRkaW5nLnZu/L3dwLWNvbnRlbnQv/dXBsb2Fkcy8yMDIz/LzA0L0FuaC1tYXUt/YW8tZGFpLXR1eWV0/LWRlcC01MzN4ODAw/LmpwZw")));
        list.add(new Product("Đồ thể thao", 80, Arrays.asList("https://imgs.search.brave.com/1Yylxm02DhkadoPI486UVUP3SqUPlgiPCoQMiJXVk5M/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9wcm9k/dWN0LmhzdGF0aWMu/bmV0LzIwMDAwMDI0/Nzk2OS9wcm9kdWN0/LzJfMGU0YzBjNTY4/YzY1NGIzNjliMDEx/ZDY1OGNkMWQ2Y2Vf/bGFyZ2UuanBn")));
        list.add(new Product("Đồ mùa hè", 89, images1));
        return list;
    }

    public List<Product> getListRecommentProduct() {
        List<Product> list = new ArrayList<>();
        list.add(new Product("Áo hoodie trắng", 20, Arrays.asList("https://imgs.search.brave.com/MDZsD7FkUd4PtBeWW6KDOrz6LU9-QjPoA5SHxdhexN0/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9idW1z/aG9wLmNvbS52bi93/cC1jb250ZW50L3Vw/bG9hZHMvMjAxOC8w/OS9tYXUtYW8taG9v/ZGllLWRlcC0yLmpw/ZWc")));
        list.add(new Product("Áo phông", 30, Arrays.asList("https://imgs.search.brave.com/nVsiA9Sv8-fDMI2zukVEPuVshyxKwxmSQYijsAbcdLg/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9tY2Ru/LmNvb2xtYXRlLm1l/L2ltYWdlL0F1Z3Vz/dDIwMjIvYW8taW4t/aGluaF83NjkuanBn")));
        list.add(new Product("Áo len", 39, Arrays.asList("https://imgs.search.brave.com/D2e42mhBDr38wRWcxwtQLjkjoJskWXMIXRyoOYSLUMI/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9maWxl/LmhzdGF0aWMubmV0/LzEwMDAxNzg3Nzkv/ZmlsZS9hb19sZW5f/Y29fbG9fbnUuanBn")));
        list.add(new Product("Áo dài", 45, Arrays.asList("https://imgs.search.brave.com/NALIw-mSjKwuO0a0zIUbPIoxBcyF_cqYvoYRqlckWEI/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly90YWls/b2N3ZWRkaW5nLnZu/L3dwLWNvbnRlbnQv/dXBsb2Fkcy8yMDIz/LzA0L0FuaC1tYXUt/YW8tZGFpLXR1eWV0/LWRlcC01MzN4ODAw/LmpwZw")));
        list.add(new Product("Đồ thể thao", 80, Arrays.asList("https://imgs.search.brave.com/1Yylxm02DhkadoPI486UVUP3SqUPlgiPCoQMiJXVk5M/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9wcm9k/dWN0LmhzdGF0aWMu/bmV0LzIwMDAwMDI0/Nzk2OS9wcm9kdWN0/LzJfMGU0YzBjNTY4/YzY1NGIzNjliMDEx/ZDY1OGNkMWQ2Y2Vf/bGFyZ2UuanBn")));
        return list;
    }
}
