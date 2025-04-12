package com.project.clothingstore.service;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
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

//     Lay ra 5 san pham co luot ban cao nhat (limit)
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
    // Lay san pham theo categoryId
    public void getProductByCategoriIdList(MutableLiveData<List<Product>> liveData, String categoryId) {
        if (categoryId == null || categoryId.isEmpty()) {
            liveData.setValue(new ArrayList<>()); // Tránh trường hợp categoryId không hợp lệ
            return;
        }

        CollectionReference productsRef = db.collection("products");

        productsRef.whereEqualTo("categoryId", categoryId)
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
                        liveData.setValue(new ArrayList<>()); // Tránh null nếu truy vấn thất bại
                    }
                });
    }

    //Loc san pham theo categoryId, categoriType, minPrice, maxPrice, rating, discount

    public void getFilteredProducts(MutableLiveData<List<Product>> liveData, int productType, int minPrice, int maxPrice, double rating, List<Integer> discounts) {
        CollectionReference productsRef = db.collection("products");
        Query query = productsRef;


        // Thêm điều kiện nếu có
        if (productType != -1) {
            query = query.whereEqualTo("productType", productType);
        }

        if (minPrice != -1) {
            query = query.whereGreaterThanOrEqualTo("price", minPrice);
        }

        if (maxPrice != -1) {
            query = query.whereLessThanOrEqualTo("price", maxPrice);
        }

        // Lọc theo đánh giá
        if (rating != -1) {
            query = query.whereGreaterThanOrEqualTo("totalRating", rating);
        }

        // Lọc sơ bộ theo discount
        if (discounts != null && !discounts.isEmpty()) {
            double minDiscountPercent = (discounts.get(0) + 1) / 10.0;
            for (int d : discounts) {
                double percent = (d + 1) / 10.0;
                if (percent < minDiscountPercent) {
                    minDiscountPercent = percent;
                }
            }
            query = query.whereGreaterThanOrEqualTo("discount", minDiscountPercent);
        }

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Product> productList = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    Product product = doc.toObject(Product.class);
                    product.setProductId(doc.getId());

                    if (discounts != null && !discounts.isEmpty()) {
                        double productDiscount = product.getDiscount(); // ví dụ: 0.3
                        boolean isMatched = false;

                        for (int level : discounts) {
                            double requiredDiscount = (level + 1) / 10.0;
                            if (productDiscount == requiredDiscount) {
                                isMatched = true;
                                break;
                            }
                        }

                        if (!isMatched) continue;
                    }
                    productList.add(product);
                }
                liveData.setValue(productList);
            } else {
                liveData.setValue(new ArrayList<>()); // Tránh null nếu truy vấn thất bại
            }
        });
    }



    // Lay san pham theo CollectionId
    public void getProductByCollectioIDList(MutableLiveData<List<Product>> liveData, String collectionId) {
        if (collectionId == null || collectionId.isEmpty()) {
            liveData.setValue(new ArrayList<>()); // Tránh trường hợp categoryId không hợp lệ
            return;
        }

        CollectionReference productsRef = db.collection("products");

        productsRef.whereEqualTo("collectionId", collectionId)
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
                        liveData.setValue(new ArrayList<>()); // Tránh null nếu truy vấn thất bại
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



}
