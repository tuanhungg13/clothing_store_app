package com.project.clothingstore.utils.helper;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseHelper {
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static FirebaseFirestore getFirestore() {
        return db;
    }
    public static CollectionReference getProductCollection() {
        return db.collection("products");
    }

    public static CollectionReference getCartCollection() {
        return db.collection("carts");
    }
    public static CollectionReference getOrderCollection() {
        return db.collection("orders");
    }
    public static CollectionReference getUserCollection() {
        return db.collection("users");
    }
    public static CollectionReference getCouponCollection() {
        return db.collection("coupons");
    }

}