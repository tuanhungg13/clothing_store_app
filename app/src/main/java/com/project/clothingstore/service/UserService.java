package com.project.clothingstore.service;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.project.clothingstore.modal.User;
import com.project.clothingstore.utils.helper.FirebaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class UserService {

    private static final String CLOUD_NAME = "dpn2spmzo";
    private static final String UPLOAD_PRESET = "unsigned_upload";

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

    // Upload avatar lên Cloudinary bằng OkHttpClient
    public void uploadAvatarToCloudinary(Context context, Uri imageUri,
                                         OnSuccessListener<String> onSuccess,
                                         OnFailureListener onFailure) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            byte[] imageBytes = new byte[inputStream.available()];
            inputStream.read(imageBytes);

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", "avatar.jpg",
                            RequestBody.create(imageBytes, MediaType.parse("image/*")))
                    .addFormDataPart("upload_preset", UPLOAD_PRESET)
                    .build();

            Request request = new Request.Builder()
                    .url("https://api.cloudinary.com/v1_1/" + CLOUD_NAME + "/image/upload")
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    new Handler(Looper.getMainLooper()).post(() -> onFailure.onFailure(e));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBody = response.body().string();
                    try {
                        JSONObject json = new JSONObject(responseBody);
                        String imageUrl = json.getString("secure_url");

                        // Cập nhật avatar vào Firestore
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("avatar", imageUrl);

                        userRef.document(uid).update(updates)
                                .addOnSuccessListener(unused -> {
                                    new Handler(Looper.getMainLooper()).post(() -> onSuccess.onSuccess(imageUrl));
                                })
                                .addOnFailureListener(e -> {
                                    new Handler(Looper.getMainLooper()).post(() -> onFailure.onFailure(e));
                                });

                    } catch (JSONException e) {
                        new Handler(Looper.getMainLooper()).post(() -> onFailure.onFailure(e));
                    }
                }
            });

        } catch (Exception e) {
            onFailure.onFailure(e);
        }
    }



    public static void getUserByEmail(String email, OnCompleteListener<QuerySnapshot> listener) {
        userRef.whereEqualTo("email", email).get().addOnCompleteListener(listener);
    }
}
