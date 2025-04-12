//package com.project.clothingstore.service;
//
//import android.util.Log;
//
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.project.clothingstore.modal.Rating;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.Consumer;
//
////public class RatingRepository {
////    public void getRatingsByProductId(String productId, Consumer<List<Rating>> callback) {
////        // In a real app, this would query Firebase or another data source
////        // For now, we'll create dummy ratings
////        List<Rating> ratings = new ArrayList<>();
////
////        // Create 10 dummy ratings
////        for (int i = 1; i <= 10; i++) {
////            Rating rating = new Rating();
////            rating.setRatingId("rating_" + i);
////            rating.setUidId("user_" + i);
////            rating.setProductId(productId);
////            rating.setRate(i % 5 == 0 ? 4 : 5); // Mix of 4 and 5 star ratings
////            rating.setComment("Đây là một sản phẩm tuyệt vời. Tôi rất hài lòng với chất lượng và giá cả.");
////            rating.setCreatedAt(System.currentTimeMillis() - (i * 86400000)); // Different days
////
////            ratings.add(rating);
////        }
////
////        callback.accept(ratings);
////    }
////}
//
//
//public class RatingRepository {
//
//    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//
//    public void getRatingsByProductId(String productId, Consumer<List<Rating>> callback) {
//
//        firestore.collection("ratings")
//                .whereEqualTo("productId", productId)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful() && task.getResult() != null) {
//                        for (DocumentSnapshot doc : task.getResult()) {
//                            Log.d("RatingRepository", "Rating ID: " + doc.getId());
//                            Log.d("RatingRepository", "createdAt type: " +
//                                    (doc.get("createdAt") != null ? doc.get("createdAt").getClass().getName() : "null"));
//                        }
//                        List<Rating> ratings = new ArrayList<>();
//                        task.getResult().forEach(document -> {
//                            Rating rating = document.toObject(Rating.class);
//                            rating.setRatingId(document.getId()); // Đảm bảo ID được thiết lập
//                            ratings.add(rating);
//                        });
//                        Log.d("RatingRepository", "Loaded " + ratings.size() + " ratings");
//                        callback.accept(ratings);
//                    } else {
//                        Log.e("RatingRepository", "Error loading ratings", task.getException());
//                        callback.accept(new ArrayList<>()); // Return empty list on failure
//                    }
//                });
//    }
//}

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
