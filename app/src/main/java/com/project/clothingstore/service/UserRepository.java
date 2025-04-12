package com.project.clothingstore.service;

import com.project.clothingstore.modal.User;
import java.util.function.Consumer;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
//package com.project.clothingstore.service;


import java.util.function.Consumer;

public class UserRepository {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public void getUserById(String uid, Consumer<User> callback) {
        if (uid == null) {
            callback.accept(null); // Trả về null nếu userId là null
            return;
        }

        DocumentReference userRef = firestore.collection("users").document(uid);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                User user = documentSnapshot.toObject(User.class);
                callback.accept(user);
            } else {
                callback.accept(null); // Trả về null nếu không tìm thấy người dùng
            }
        }).addOnFailureListener(e -> {
            e.printStackTrace();
            callback.accept(null); // Trả về null nếu có lỗi
        });
    }
}

