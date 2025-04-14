package com.project.clothingstore.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.clothingstore.modal.Orders;
import com.project.clothingstore.service.OrderRepository;

import java.util.List;

public class OrderViewModel extends AndroidViewModel {
    private final OrderRepository repository;
    private final MutableLiveData<List<Orders>> ordersLiveData = new MutableLiveData<>();

    public OrderViewModel(@NonNull Application application) {
        super(application);
        repository = new OrderRepository();
    }

    public LiveData<List<Orders>> getOrdersLiveData() {
        return ordersLiveData;
    }

    public void loadOrdersByStatus(String status) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            repository.getOrdersByStatus(currentUser.getUid(), status, ordersLiveData);
        }
    }
}
