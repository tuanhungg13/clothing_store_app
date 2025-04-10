package com.project.clothingstore.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.clothingstore.modal.Product;
import com.project.clothingstore.modal.Rating;
import com.project.clothingstore.modal.User;
import com.project.clothingstore.service.ProductRepository;
import com.project.clothingstore.service.RatingRepository;
import com.project.clothingstore.service.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailViewModel extends ViewModel {
    private final ProductRepository productRepository;
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;

    private final MutableLiveData<Product> productLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Rating>> ratingsLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Product>> similarProductsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Map<String, User>> userMapLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> selectedColor = new MutableLiveData<>();
    private final MutableLiveData<String> selectedSize = new MutableLiveData<>();
    private final MutableLiveData<Map<Integer, Integer>> ratingDistributionLiveData = new MutableLiveData<>();

    public ProductDetailViewModel() {
        // Initialize repositories
        productRepository = new ProductRepository();
        ratingRepository = new RatingRepository();
        userRepository = new UserRepository();

        // Default values for selected color and size
        selectedColor.setValue("Beige");
        selectedSize.setValue("L");
    }

    public void loadProductDetails(String productId) {
        // Load product details
        productRepository.getProductById(productId, product -> {
            productLiveData.setValue(product);
            loadSimilarProducts(product.getCategoryId());
        });

        // Load product ratings
        ratingRepository.getRatingsByProductId(productId, ratings -> {
            ratingsLiveData.setValue(ratings);
            calculateRatingDistribution(ratings);
            loadUserDetailsForRatings(ratings);
        });
    }

    private void loadSimilarProducts(String categoryId) {
        productRepository.getProductsByCategory(categoryId, 5, products -> {
            // Filter out current product if it's in the list
            Product currentProduct = productLiveData.getValue();
            List<Product> filteredProducts = new ArrayList<>();

            for (Product product : products) {
                if (currentProduct != null && !product.getProductId().equals(currentProduct.getProductId())) {
                    filteredProducts.add(product);
                }
            }

            similarProductsLiveData.setValue(filteredProducts);
        });
    }

    private void loadUserDetailsForRatings(List<Rating> ratings) {
        Map<String, User> userMap = new HashMap<>();

        for (Rating rating : ratings) {
            userRepository.getUserById(rating.getUidId(), user -> {
                userMap.put(rating.getUidId(), user);
                userMapLiveData.setValue(userMap);
            });
        }
    }

    private void calculateRatingDistribution(List<Rating> ratings) {
        Map<Integer, Integer> distribution = new HashMap<>();
        // Initialize counts for all ratings 1-5
        for (int i = 1; i <= 5; i++) {
            distribution.put(i, 0);
        }

        // Count each rating
        for (Rating rating : ratings) {
            int rate = rating.getRate();
            distribution.put(rate, distribution.get(rate) + 1);
        }

        // Convert to percentages
        int totalRatings = ratings.size();
        if (totalRatings > 0) {
            Map<Integer, Integer> percentages = new HashMap<>();
            for (int i = 1; i <= 5; i++) {
                int percentage = (int) (((float) distribution.get(i) / totalRatings) * 100);
                percentages.put(i, percentage);
            }
            ratingDistributionLiveData.setValue(percentages);
        }
    }

    public void setSelectedColor(String color) {
        selectedColor.setValue(color);
    }

    public void setSelectedSize(String size) {
        selectedSize.setValue(size);
    }

    // Getters for LiveData
    public LiveData<Product> getProduct() {
        return productLiveData;
    }

    public LiveData<List<Rating>> getRatings() {
        return ratingsLiveData;
    }

    public LiveData<List<Product>> getSimilarProducts() {
        return similarProductsLiveData;
    }

    public LiveData<Map<String, User>> getUserMap() {
        return userMapLiveData;
    }

    public LiveData<String> getSelectedColor() {
        return selectedColor;
    }

    public LiveData<String> getSelectedSize() {
        return selectedSize;
    }

    public LiveData<Map<Integer, Integer>> getRatingDistribution() {
        return ratingDistributionLiveData;
    }
}