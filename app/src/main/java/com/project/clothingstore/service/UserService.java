package com.project.clothingstore.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.project.clothingstore.modal.User;
import com.project.clothingstore.utils.helper.FirebaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {

    private static final CollectionReference userRef = FirebaseHelper.getUserCollection();
    private static final CollectionReference cartRef = FirebaseHelper.getCartCollection();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

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
        cartRef.add(new HashMap<>())
                .addOnSuccessListener(cartDocRef -> {
                    String cartId = cartDocRef.getId();
                    user.setCartId(cartId);

                    userRef.document(user.getUid()).set(user)
                            .addOnCompleteListener(onCompleteListener);
                })
                .addOnFailureListener(e -> {
                    onCompleteListener.onComplete(Tasks.forException(e));
                });
    }

    public void updateUserProfile(String fullName, String phoneNumber, Map<String, Object> addressData,
                                  OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        String uid = mAuth.getCurrentUser().getUid();

        Map<String, Object> userData = new HashMap<>();
        userData.put("fullName", fullName);
        userData.put("phoneNumber", phoneNumber);
        userData.put("address", addressData);

        userRef.document(uid).update(userData)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    public void uploadAvatarBase64(Context context,
                                   Uri imageUri,
                                   OnSuccessListener<String> onSuccess,
                                   OnFailureListener onFailure) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            saveAvatarBase64(base64Image, onSuccess, onFailure);
        } catch (IOException e) {
            onFailure.onFailure(e);
        }
    }

    private void saveAvatarBase64(String base64,
                                  OnSuccessListener<String> onSuccess,
                                  OnFailureListener onFailure) {
        String uid = mAuth.getCurrentUser().getUid();
        Map<String, Object> update = new HashMap<>();
        update.put("avatar", base64);

        userRef.document(uid).set(update, SetOptions.merge())
                .addOnSuccessListener(unused -> onSuccess.onSuccess(base64))
                .addOnFailureListener(onFailure);
    }

    public static void getUserByEmail(String email, OnCompleteListener<QuerySnapshot> listener) {
        userRef.whereEqualTo("email", email).get().addOnCompleteListener(listener);
    }

}
