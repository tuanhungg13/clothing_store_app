package com.project.clothingstore.service;

import com.project.clothingstore.modal.User;
import java.util.function.Consumer;

public class UserRepository {
    public void getUserById(String userId, Consumer<User> callback) {
        // In a real app, this would query Firebase or another data source
        // For now, we'll create a dummy user
        User user = new User();
        user.setUid(userId);
        user.setFullName("Người dùng " + userId.replace("user_", ""));
        user.setEmail("user" + userId.replace("user_", "") + "@example.com");

        callback.accept(user);
    }
}