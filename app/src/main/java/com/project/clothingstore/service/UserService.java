package com.project.clothingstore.service;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.project.clothingstore.model.User;
import com.project.clothingstore.utils.helper.FirebaseHelper;

import java.util.HashMap;
import java.util.Map;

public class UserService {

    private static final CollectionReference userRef = FirebaseHelper.getUserCollection();
    private static final CollectionReference cartRef = FirebaseHelper.getCartCollection();

    public static void createUserProfile(User user, OnCompleteListener<Void> callback) {
        userRef.document(user.getUid()).set(user).addOnCompleteListener(callback);
    }

    public static void getUserProfile(String uid, OnCompleteListener<DocumentSnapshot> callback) {
        userRef.document(uid).get().addOnCompleteListener(callback);
    }

    public static void updateUserProfile(String uid, Map<String, Object> updates, OnCompleteListener<Void> callback) {
        userRef.document(uid).update(updates).addOnCompleteListener(callback);
    }

    public static void deleteUserProfile(String uid, OnCompleteListener<Void> callback) {
        userRef.document(uid).delete().addOnCompleteListener(callback);
    }

    public static void createUserWithCart(User user, OnCompleteListener<Void> onCompleteListener) {
        // Bước 1: Tạo document mới trong carts
        cartRef.add(new HashMap<>()) // có thể thêm { "uid": user.getUid() } nếu muốn
                .addOnSuccessListener(cartDocRef -> {
                    String cartId = cartDocRef.getId(); // Lấy cartId vừa tạo
                    // Bước 2: Gán cartId vào user
                    user.setCartId(cartId);

                    // Bước 3: Lưu user vào Firestore
                    userRef.document(user.getUid()).set(user)
                            .addOnCompleteListener(onCompleteListener);
                })
                .addOnFailureListener(e -> {
                    // Trường hợp lỗi khi tạo giỏ hàng
                    onCompleteListener.onComplete(Tasks.forException(e));
                });
    }

}
