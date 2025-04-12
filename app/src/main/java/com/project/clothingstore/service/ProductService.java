package com.project.clothingstore.service;


import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.clothingstore.modal.Product;
import com.project.clothingstore.utils.helper.FirebaseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductService {
    private final CollectionReference productRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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

//     Lay ra 5 san pham co luot ban cao nhat
    public void getSanPhamList(MutableLiveData<List<Product>> liveData, String field, int limitt) {
        CollectionReference productsRef = db.collection("products");

        // Lấy 5 sản phẩm có lượt bán cao nhất
        productsRef.orderBy(field, Query.Direction.DESCENDING)
                .limit(limitt)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Product> productList = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Product product = doc.toObject(Product.class);
                            product.setProductId(doc.getId());
                            productList.add(product);
                        }
                        liveData.setValue(productList);
                    } else {
                        liveData.setValue(new ArrayList<>()); // Tránh null
                    }
                });
    }



    // Lấy tất cả sản phẩm
    public void getAllSanPhamList(MutableLiveData<List<Product>> liveData) {
        CollectionReference productsRef = db.collection("products");
        productsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Product> productList = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    Product product = doc.toObject(Product.class);
                    product.setProductId(doc.getId());
                    productList.add(product);
                }
                liveData.setValue(productList);
            } else {
                liveData.setValue(new ArrayList<>()); // Tránh null
            }
        });
    }


    public List<Product> getListProduct() {
        List<Product> list = new ArrayList<>();
        return list;
    }

    public List<Product> getListProductByType(String categoryId) {
        List<Product> list = new ArrayList<>();
        for (Product product : getListProduct()) {
            if (product.getCategoryId().equals(categoryId)) {
                list.add(product);
            }
        }
        return list;
    }

    public void getProductImages(String productId, OnSuccessListener<List<String>> listener, OnFailureListener onFailureListener) {
        productRef.document(productId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<String> imageUrls = (List<String>) documentSnapshot.get("images");
                        if (imageUrls != null) {
                            listener.onSuccess(imageUrls);
                        } else {
                            listener.onSuccess(new ArrayList<>()); // Trả về danh sách rỗng nếu không có ảnh
                        }
                    } else {
                        onFailureListener.onFailure(new Exception("Sản phẩm không tồn tại"));
                    }
                })
                .addOnFailureListener(onFailureListener);
    }

}
