package com.project.clothingstore.service;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.project.clothingstore.modal.ProductCategories;
import com.project.clothingstore.modal.ProductCollections;
import com.project.clothingstore.utils.helper.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoriesService {

    private final CollectionReference productRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ProductCategoriesService() {
        productRef = FirebaseHelper.getProductCollection();
    }

    public ProductCategoriesService(CollectionReference productRef) {
        this.productRef = productRef;
    }

    // Lay category theo type khong co so luong
    public void getListCategoryByType(MutableLiveData<List<ProductCategories>> liveData, int categoriType) {
        CollectionReference collectionsRef = db.collection("categories");

        Query query = collectionsRef;

        query = query.whereEqualTo("categoriType", categoriType);

        // Lay tat ca kết quả
        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<ProductCategories> productCategoriList = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            ProductCategories productCategories = doc.toObject(ProductCategories.class);
                            productCategories.setCategoryId(doc.getId());
                            productCategoriList.add(productCategories);
                        }
                        liveData.setValue(productCategoriList);
                    } else {
                        liveData.setValue(new ArrayList<>()); // Tránh null
                    }
                });
    }

    // Lay category theo type co so luong
    public void getListCategoryWithProductCount(MutableLiveData<List<ProductCategories>> liveData, int categoriType) {
        CollectionReference categoriesRef = db.collection("categories");
        CollectionReference productsRef = db.collection("products");

        Query query = categoriesRef.whereEqualTo("categoriType", categoriType);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<ProductCategories> categoryList = new ArrayList<>();

                for (QueryDocumentSnapshot doc : task.getResult()) {
                    ProductCategories category = doc.toObject(ProductCategories.class);
                    String categoryId = doc.getId();
                    category.setCategoryId(categoryId);

                    productsRef.whereEqualTo("categoryId", categoryId).get().addOnSuccessListener(productTask -> {
                        int count = productTask.size();
                        category.setQuantity(count); // ← gán số lượng
                        categoryList.add(category);

                        // Nếu đủ số lượng category thì cập nhật LiveData
                        if (categoryList.size() == task.getResult().size()) {
                            liveData.setValue(categoryList);
                        }
                    });
                }
            } else {
                liveData.setValue(new ArrayList<>());
            }
        });
    }


}
