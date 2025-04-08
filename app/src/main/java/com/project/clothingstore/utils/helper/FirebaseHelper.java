package com.project.clothingstore.utils.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseHelper {
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final FirebaseAuth auth = FirebaseAuth.getInstance();

    public static FirebaseFirestore getFirestore() {
        return db;
    }

    public static FirebaseAuth getAuth() {
        return auth;
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
}
