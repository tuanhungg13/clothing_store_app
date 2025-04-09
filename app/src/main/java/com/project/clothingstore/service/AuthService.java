package com.project.clothingstore.service;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.clothingstore.model.User;

import java.util.HashMap;

public class AuthService {

    private static final FirebaseAuth auth = FirebaseAuth.getInstance();

    /**
     * Đăng ký tài khoản:
     * - Tạo tài khoản Firebase Auth
     * - Tạo cartId
     * - Lưu User với cartId vào Firestore
     */
    public static void register(String email, String password, String fullName, String phone, OnCompleteListener<AuthResult> callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    // Gọi callback để trả kết quả về ViewModel
                    callback.onComplete(task);

                    if (task.isSuccessful()) {
                        String uid = task.getResult().getUser().getUid();

                        // Tạo đối tượng User với role "user", address rỗng và cartId null
                        User newUser = new User(
                                uid,
                                email,
                                phone,
                                fullName,
                                "user", // role mặc định
                                null,
                                new HashMap<>(), // address rỗng
                                null // cartId sẽ gán sau khi tạo
                        );

                        // Gọi hàm tạo cart và lưu user
                        UserService.createUserWithCart(newUser, userTask -> {
                            if (userTask.isSuccessful()) {
                                Log.d("AuthService", "User profile + cart created successfully");
                            } else {
                                Log.e("AuthService", "Failed to create user + cart", userTask.getException());
                            }
                        });
                    }
                });
    }


    // Đăng nhập
    public static void login(String email, String password, OnCompleteListener<AuthResult> callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(callback);
    }

    // Đăng xuất
    public static void logout() {
        auth.signOut();
    }

    // Lấy user hiện tại
    public static FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    // Kiểm tra đã đăng nhập chưa
    public static boolean isLoggedIn() {
        return auth.getCurrentUser() != null;
    }

    // Lấy UID hiện tại
    public static String getCurrentUid() {
        FirebaseUser user = auth.getCurrentUser();
        return user != null ? user.getUid() : null;
    }

    // Đăng nhập bằng Credential (Google, Facebook...)
    public static void loginWithCredential(AuthCredential credential, OnCompleteListener<AuthResult> callback) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(callback);
    }
}
