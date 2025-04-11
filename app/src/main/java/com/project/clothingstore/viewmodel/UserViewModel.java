package com.project.clothingstore.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Base64;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.project.clothingstore.model.User;
import com.project.clothingstore.service.UserService;
import com.project.clothingstore.utils.Event;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class UserViewModel extends ViewModel {

    // LiveData chứa thông tin người dùng hiện tại
    private final MutableLiveData<User> currentUser = new MutableLiveData<>(null);

    // LiveData dùng để theo dõi trạng thái đang loading dữ liệu
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    // LiveData dùng để lưu thông báo lỗi (nếu có) dưới dạng Event
    private final MutableLiveData<Event<String>> error = new MutableLiveData<>(null);
    private final MutableLiveData<Boolean> updateSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> avatarUploadStatus = new MutableLiveData<>();
    private final UserService userService = new UserService();

    // Hàm getter để Fragment/Activity có thể quan sát currentUser
    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    // Hàm getter để theo dõi trạng thái đang loading
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    // Hàm getter để lấy thông báo lỗi
    public LiveData<Event<String>> getError() {
        return error;
    }
    public LiveData<Boolean> getUpdateSuccess() {
        return updateSuccess;
    }

    public LiveData<String> getAvatarUploadStatus() {
        return avatarUploadStatus;
    }

    /**
     * Gọi hàm này để lấy thông tin người dùng từ Firestore
     * @param uid UID của người dùng (lấy từ Firebase Authentication)
     */
    public void fetchUserInfo(String uid) {
        // Bắt đầu loading
        isLoading.setValue(true);
        // Xoá lỗi cũ (nếu có)
        error.setValue(null);

        // Gọi đến UserService để lấy dữ liệu từ Firestore
        UserService.getUserProfile(uid, task -> {
            // Dừng loading sau khi có kết quả
            isLoading.setValue(false);

            if (task.isSuccessful()) {
                // Lấy document trả về từ Firestore
                DocumentSnapshot doc = task.getResult();

                if (doc.exists()) {
                    // Chuyển dữ liệu Firestore thành đối tượng User
                    User user = doc.toObject(User.class);

                    // Cập nhật currentUser
                    currentUser.setValue(user);
                } else {
                    // Nếu không tìm thấy document
                    error.setValue(new Event<>("Không tìm thấy thông tin người dùng"));
                }
            } else {
                // Nếu có lỗi truy vấn
                error.setValue(new Event<>("Lỗi tải thông tin: " + task.getException().getMessage()));
            }
        });
    }

    public void updateUserProfile(String fullName, String phoneNumber, Map<String, Object> addressData) {
        userService.updateUserProfile(fullName, phoneNumber, addressData,
                unused -> updateSuccess.setValue(true),
                e -> updateSuccess.setValue(false));
    }

    public void uploadAvatar(Context context, Uri imageUri) {
        avatarUploadStatus.setValue("loading");

        userService.uploadAvatarBase64(context, imageUri,
                base64 -> {
                    avatarUploadStatus.setValue("success");

                    // Cập nhật avatarBase64 cho currentUser
                    User user = currentUser.getValue();
                    if (user != null) {
                        user.setAvatar(base64);
                        currentUser.setValue(user);
                    }
                },
                e -> avatarUploadStatus.setValue("fail"));
    }


    /**
     * Dùng khi đăng xuất hoặc làm mới trạng thái người dùng
     */
    public void clearUser() {
        currentUser.setValue(null);
    }
}
