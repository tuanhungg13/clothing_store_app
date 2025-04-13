package com.project.clothingstore.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.project.clothingstore.modal.Orders;

import java.util.List;

public class OrderViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Orders>> ordersLiveData;
    private final FirebaseFirestore firestore;

    public OrderViewModel(@NonNull Application application) {
        super(application);
        firestore = FirebaseFirestore.getInstance();
        ordersLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Orders>> getOrdersLiveData() {
        return ordersLiveData;
    }

    // Hàm lọc đơn hàng theo userId và status
    public void loadOrdersByUserIdAndStatus(String userId, String status) {
        firestore.collection("orders")
                .whereEqualTo("uid", userId)
                .whereEqualTo("status", status)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Orders> orders = queryDocumentSnapshots.toObjects(Orders.class);
                    ordersLiveData.postValue(orders); // cập nhật LiveData
                })
                .addOnFailureListener(e -> {
                    // Nếu cần xử lý lỗi, bạn có thể log hoặc cập nhật giá trị rỗng
                    ordersLiveData.postValue(null);
                });
    }
}
