package com.project.clothingstore.repository;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.*;
import com.project.clothingstore.utils.helper.FirebaseHelper;

public class ProductRepository {

    private final CollectionReference productRef;

    public ProductRepository() {
        productRef = FirebaseHelper.getProductCollection();
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
}
