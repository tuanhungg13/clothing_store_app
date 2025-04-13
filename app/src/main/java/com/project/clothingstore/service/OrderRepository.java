package com.project.clothingstore.service;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.project.clothingstore.modal.Orders;

import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

    private final FirebaseFirestore db;

    public OrderRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void getOrdersByUserId(String userId, MutableLiveData<List<Orders>> ordersLiveData) {
        db.collection("orders")
                .whereEqualTo("uid", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Orders> orderList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Orders order = doc.toObject(Orders.class);
//                        order.setOrderId(doc.getId()); // set orderId nếu cần
                        orderList.add(order);
                    }
                    ordersLiveData.setValue(orderList);
                })
                .addOnFailureListener(e -> {
                    ordersLiveData.setValue(null); // hoặc xử lý lỗi tùy bạn
                });
    }

    public void getOrdersByStatus(String userId, String status, MutableLiveData<List<Orders>> ordersLiveData) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("orders")
                .whereEqualTo("uid", userId)
                .whereEqualTo("status", status)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Orders> orders = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            Orders order = document.toObject(Orders.class);
                            orders.add(order);
                        }
                        ordersLiveData.setValue(orders);
                    } else {
                        // Handle failure
                    }
                });
    }
}
