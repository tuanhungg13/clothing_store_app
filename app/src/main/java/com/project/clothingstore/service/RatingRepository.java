package com.project.clothingstore.service;

import com.project.clothingstore.modal.Rating;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RatingRepository {
    public void getRatingsByProductId(String productId, Consumer<List<Rating>> callback) {
        // In a real app, this would query Firebase or another data source
        // For now, we'll create dummy ratings
        List<Rating> ratings = new ArrayList<>();

        // Create 10 dummy ratings
        for (int i = 1; i <= 10; i++) {
            Rating rating = new Rating();
            rating.setRatingId("rating_" + i);
            rating.setUidId("user_" + i);
            rating.setProductId(productId);
            rating.setRate(i % 5 == 0 ? 4 : 5); // Mix of 4 and 5 star ratings
            rating.setComment("Đây là một sản phẩm tuyệt vời. Tôi rất hài lòng với chất lượng và giá cả.");
            rating.setCreatedAt(System.currentTimeMillis() - (i * 86400000)); // Different days

            ratings.add(rating);
        }

        callback.accept(ratings);
    }
}
