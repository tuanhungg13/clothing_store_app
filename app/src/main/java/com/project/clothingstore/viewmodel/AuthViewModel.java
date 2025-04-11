package com.project.clothingstore.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.project.clothingstore.model.Address;
import com.project.clothingstore.model.User;
import com.project.clothingstore.service.AuthService;
import com.project.clothingstore.service.UserService;
import com.project.clothingstore.utils.Event;

import java.util.HashMap;

public class AuthViewModel extends ViewModel {
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Event<String>> authError = new MutableLiveData<>();
    private final MutableLiveData<String> currentUid = new MutableLiveData<>(null);
    private final MutableLiveData<Boolean> isPasswordReset = new MutableLiveData<>(false);

    // ------------------ GETTER ------------------
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Event<String>> getAuthError() {
        return authError;
    }

    public LiveData<String> getCurrentUid() {
        return currentUid;
    }

    public LiveData<Boolean> getIsPasswordReset() {
        return isPasswordReset;
    }

    // ------------------ ĐĂNG NHẬP ------------------
    public void login(String email, String password) {
        isLoading.setValue(true);
        authError.setValue(null);

        AuthService.login(email, password, task -> {
            isLoading.setValue(false);
            if (task.isSuccessful()) {
                currentUid.setValue(AuthService.getCurrentUid());
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
                currentUid.setValue(AuthService.getCurrentUid());
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

    // ------------------ KIỂM TRA ĐĂNG NHẬP ------------------
    public void checkLoggedIn() {
        FirebaseUser user = AuthService.getCurrentUser();
        currentUid.setValue(user != null ? user.getUid() : null);
    }

    // ------------------ ĐĂNG NHẬP GOOGLE ------------------
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

                    // Nếu user chưa có trong Firestore thì tạo mới
                    UserService.getUserProfile(firebaseUser.getUid(), userTask -> {
                        if (!userTask.isSuccessful() || userTask.getResult() == null || !userTask.getResult().exists()) {
                            User newUser = new User(
                                    firebaseUser.getUid(),
                                    firebaseUser.getEmail(),
                                    "", // không có số điện thoại từ Google
                                    firebaseUser.getDisplayName() != null ? firebaseUser.getDisplayName() : "",
                                    "user",
                                    null,
                                    new Address(),
                                    null
                            );
                            UserService.createUserWithCart(newUser, createTask -> {
                                if (!createTask.isSuccessful()) {
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

    // ------------------ GỬI EMAIL RESET MẬT KHẨU ------------------
    public void sendPasswordReset(String email) {
        isLoading.setValue(true);
        authError.setValue(null);

        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    isLoading.setValue(false);
                    if (task.isSuccessful()) {
                        isPasswordReset.setValue(true);
                    } else {
                        authError.setValue(new Event<>("Gửi email khôi phục mật khẩu thất bại: " + task.getException().getMessage()));
                    }
                });
    }
}
