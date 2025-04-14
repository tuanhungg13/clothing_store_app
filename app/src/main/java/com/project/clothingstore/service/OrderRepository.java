package com.project.clothingstore.service;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.project.clothingstore.modal.Orders;

import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void getOrdersByStatus(String userId, String status, MutableLiveData<List<Orders>> liveData) {
        db.collection("orders")
                .whereEqualTo("uid", userId)
                .whereEqualTo("status", status)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Orders> ordersList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Orders order = doc.toObject(Orders.class);
                        order.setOrderId(doc.getId());
                        ordersList.add(order);
                    }
                    liveData.setValue(ordersList);
                })
                .addOnFailureListener(e -> liveData.setValue(null));
    }
}
