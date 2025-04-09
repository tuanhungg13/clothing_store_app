package com.project.clothingstore.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.project.clothingstore.model.User;
import com.project.clothingstore.service.AuthService;
import com.project.clothingstore.service.UserService;
import com.project.clothingstore.utils.Event;

import java.util.HashMap;
import java.util.Map;

public class AuthViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<Event<String>> authError = new MutableLiveData<>();
    private final MutableLiveData<String> currentUid = new MutableLiveData<>(null);

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Event<String>> getAuthError() {
        return authError;
    }

    public LiveData<String> getCurrentUid() {
        return currentUid;
    }

    // ------------------ ĐĂNG NHẬP ------------------
    public void login(String email, String password) {
        isLoading.setValue(true);
        authError.setValue(null);

        AuthService.login(email, password, task -> {
            isLoading.setValue(false);
            if (task.isSuccessful()) {
                String uid = AuthService.getCurrentUid();
                currentUid.setValue(uid);
            } else {
                authError.setValue(new Event<>("Đăng nhập thất bại: " + task.getException().getMessage()));
            }
        });
    }

    // ------------------ ĐĂNG KÝ ------------------
    public void register(String email, String password, String fullName, String phoneNumber) {
        isLoading.setValue(true);
        authError.setValue(null);

        AuthService.register(email, password, fullName, phoneNumber, task -> {
            isLoading.setValue(false);
            if (task.isSuccessful()) {
                String uid = AuthService.getCurrentUid();
                currentUid.setValue(uid);
            } else {
                authError.setValue(new Event<>("Đăng ký thất bại: " + task.getException().getMessage()));
            }
        });
    }

    // ------------------ ĐĂNG XUẤT ------------------
    public void logout() {
        AuthService.logout();
        currentUid.setValue(null);
    }

    // ------------------ KIỂM TRA ĐÃ ĐĂNG NHẬP ------------------
    public void checkLoggedIn() {
        FirebaseUser user = AuthService.getCurrentUser();
        if (user != null) {
            currentUid.setValue(user.getUid());
        } else {
            currentUid.setValue(null);
        }
    }

    public void loginWithGoogle(String idToken) {
        isLoading.setValue(true);
        authError.setValue(null);

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        AuthService.loginWithCredential(credential, task -> {
            isLoading.setValue(false);
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = task.getResult().getUser();
                if (firebaseUser != null) {
                    currentUid.setValue(firebaseUser.getUid());

                    // Kiểm tra nếu user chưa có trong Firestore => tạo mới
                    UserService.getUserProfile(firebaseUser.getUid(), userTask -> {
                        if (!userTask.isSuccessful() || userTask.getResult() == null || !userTask.getResult().exists()) {
                            // Tạo user mới
                            User newUser = new User(
                                    firebaseUser.getUid(),
                                    firebaseUser.getEmail(),
                                    "", // Số điện thoại Google không cung cấp
                                    firebaseUser.getDisplayName() != null ? firebaseUser.getDisplayName() : "",
                                    "user", // Role mặc định
                                    null,
                                    new HashMap<>(), // address rỗng
                                    null // cartId null, sẽ tạo sau
                            );

                            UserService.createUserWithCart(newUser, createTask -> {
                                if (createTask.isSuccessful()) {
                                    currentUid.setValue(firebaseUser.getUid());
                                } else {
                                    authError.setValue(new Event<>("Không thể tạo tài khoản mới từ Google: " + createTask.getException().getMessage()));
                                }
                            });
                        }
                    });
                }
            } else {
                authError.setValue(new Event<>("Đăng nhập Google thất bại: " + task.getException().getMessage()));
            }
        });
    }

}
