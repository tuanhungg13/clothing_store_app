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
