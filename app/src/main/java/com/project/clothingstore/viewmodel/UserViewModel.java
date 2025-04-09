package com.project.clothingstore.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.project.clothingstore.model.User;
import com.project.clothingstore.service.UserService;

public class UserViewModel extends ViewModel {

    // LiveData chứa thông tin người dùng hiện tại
    private final MutableLiveData<User> currentUser = new MutableLiveData<>(null);

    // LiveData dùng để theo dõi trạng thái đang loading dữ liệu
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    // LiveData dùng để lưu thông báo lỗi (nếu có)
    private final MutableLiveData<String> error = new MutableLiveData<>(null);

    // Hàm getter để Fragment/Activity có thể quan sát currentUser
    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    // Hàm getter để theo dõi trạng thái đang loading
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    // Hàm getter để lấy thông báo lỗi
    public LiveData<String> getError() {
        return error;
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
                    error.setValue("Không tìm thấy thông tin người dùng");
                }
            } else {
                // Nếu có lỗi truy vấn
                error.setValue("Lỗi tải thông tin: " + task.getException().getMessage());
            }
        });
    }

    /**
     * Dùng khi đăng xuất hoặc làm mới trạng thái người dùng
     */
    public void clearUser() {
        currentUser.setValue(null);
    }
}
