package com.project.clothingstore.service;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.project.clothingstore.R;
import com.project.clothingstore.modal.Product;
import com.project.clothingstore.modal.ProductCollections;
import com.project.clothingstore.utils.helper.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ProductCollectionService {

    private final CollectionReference collectionsRef ;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ProductCollectionService() {
        collectionsRef  = FirebaseHelper.getProductCollection();
    }

    public ProductCollectionService(CollectionReference collectionsRef ) {
        this.collectionsRef  = collectionsRef ;
    }

    public void getCollectionList(MutableLiveData<List<ProductCollections>> liveData, @Nullable String collectionType) {
        CollectionReference collectionsRef = db.collection("collections");

        Query query = collectionsRef;

        // Nếu collectionType khác null hoặc không rỗng thì lọc
        if (collectionType != null && !collectionType.isEmpty()) {
            // Chuyển đổi collectionType thành số nguyên
            query = query.whereEqualTo("collectionType", collectionType);
        }

        // Giới hạn 5 kết quả
        query.limit(5).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<ProductCollections> productCollectionsList = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            ProductCollections productCollections = doc.toObject(ProductCollections.class);
                            productCollections.setCollectionId(doc.getId());
                            productCollectionsList.add(productCollections);
                        }
                        liveData.setValue(productCollectionsList);
                    } else {
                        liveData.setValue(new ArrayList<>()); // Tránh null
                    }
                });
    }


}
