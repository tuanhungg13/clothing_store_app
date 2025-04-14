
package com.project.clothingstore.service;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.clothingstore.modal.Rating;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RatingRepository {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public void getRatingsByProductId(String productId, Consumer<List<Rating>> callback) {
        firestore.collection("ratings")
                .whereEqualTo("productId", productId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<Rating> ratings = new ArrayList<>();
                        for (DocumentSnapshot doc : task.getResult()) {
                            Rating rating = doc.toObject(Rating.class);
                            rating.setRatingId(doc.getId()); // Đảm bảo ID được thiết lập
                            ratings.add(rating);
                        }
                        Log.d("RatingRepository", "Loaded " + ratings.size() + " ratings");
                        callback.accept(ratings);
                    } else {
                        Log.e("RatingRepository", "Error loading ratings", task.getException());
                        callback.accept(new ArrayList<>()); // Trả về danh sách rỗng nếu có lỗi
                    }
                });
    }
}
