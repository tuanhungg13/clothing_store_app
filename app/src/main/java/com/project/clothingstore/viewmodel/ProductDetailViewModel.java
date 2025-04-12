
package com.project.clothingstore.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.clothingstore.modal.ColorItem;
import com.project.clothingstore.modal.Product;
import com.project.clothingstore.modal.Rating;
import com.project.clothingstore.modal.SizeItem;
import com.project.clothingstore.modal.User;
import com.project.clothingstore.service.ProductRepository;
import com.project.clothingstore.service.RatingRepository;
import com.project.clothingstore.service.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ProductDetailViewModel extends ViewModel {
    private final ProductRepository productRepository;
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;

    private final MutableLiveData<Product> productLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<String>> productImagesLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Rating>> ratingsLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Product>> similarProductsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Map<String, User>> userMapLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> selectedColor = new MutableLiveData<>();
    private final MutableLiveData<String> selectedSize = new MutableLiveData<>();
    private final MutableLiveData<Map<Integer, Integer>> ratingDistributionLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<ColorItem>> colorsLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<SizeItem>> sizesLiveData = new MutableLiveData<>();

    private List<Rating> originalRatings = new ArrayList<>(); // Lưu trữ danh sách gốc

    public ProductDetailViewModel() {
        // Initialize repositories
        productRepository = new ProductRepository();
        ratingRepository = new RatingRepository();
        userRepository = new UserRepository();

//        // Default values for selected color and size
//        selectedColor.setValue("Beige");
//        selectedSize.setValue("L");
//
//        // Khởi tạo phân phối đánh giá mặc định
//        Map<Integer, Integer> defaultDistribution = new HashMap<>();
//        for (int i = 1; i <= 5; i++) {
//            defaultDistribution.put(i, 0);
//        }
//        ratingDistributionLiveData.setValue(defaultDistribution);
    }

//    public void loadProductDetails(String productId) {
//        // Load product details
//        productRepository.getProductById(productId, product -> {
//            if (product != null) {
//                productLiveData.setValue(product);
//                productImagesLiveData.setValue(product.getImages());
//
//                // Extract colors and sizes from variants
//                extractColorsAndSizes(product);
//
//                loadSimilarProducts(product.getCategoryId());
//            }
//        });
//
//        // Load product ratings from Firebase
//        ratingRepository.getRatingsByProductId(productId, ratings -> {
//            originalRatings = new ArrayList<>(ratings); // Lưu trữ bản sao gốc
//            ratingsLiveData.setValue(ratings);
//            calculateRatingDistribution(ratings);
//            loadUserDetailsForRatings(ratings);
//        });
//    }

    public void loadProductDetails(String productId) {
        productRepository.getProductById(productId, product -> {
            if (product != null) {
                productLiveData.setValue(product);
                productImagesLiveData.setValue(product.getImages());
                extractColorsAndSizes(product);
                loadSimilarProducts(product.getCategoryId());
            }
        });

//        ratingRepository.getRatingsByProductId(productId, ratings -> {
//            originalRatings = new ArrayList<>(ratings);
//            ratingsLiveData.setValue(ratings);
//            calculateRatingDistribution(ratings);
//            loadUserDetailsForRatings(ratings);
//        });
        ratingRepository.getRatingsByProductId(productId, ratings -> {
            if (ratings != null && !ratings.isEmpty()) {
                originalRatings = new ArrayList<>(ratings);
                ratingsLiveData.setValue(ratings);
            } else {
                ratingsLiveData.setValue(new ArrayList<>()); // Trả về danh sách rỗng nếu không có đánh giá
            }
            calculateRatingDistribution(ratings);
            loadUserDetailsForRatings(ratings);
        });

    }

    private void extractColorsAndSizes(Product product) {
        List<Product.Variant> variants = product.getVariants();
        if (variants != null && !variants.isEmpty()) {
            // Extract colors
            List<ColorItem> colors = new ArrayList<>();
            for (int i = 0; i < variants.size(); i++) {
                Product.Variant variant = variants.get(i);
                colors.add(new ColorItem(String.valueOf(i), variant.getColor(), "#000000"));
            }
            colorsLiveData.setValue(colors);

            // Set default selected color if none is selected
            if (selectedColor.getValue() == null && !colors.isEmpty()) {
                selectedColor.setValue(colors.get(0).getName());
            }

            // Extract sizes for the first color (or selected color)
            updateSizesForSelectedColor(product);
        } else {
            // Fallback to default values if no variants
            setupDefaultColorsAndSizes(product.getProductType());
        }
    }

    private void updateSizesForSelectedColor(Product product) {
        String selectedColorName = selectedColor.getValue();
        List<Product.Variant> variants = product.getVariants();

        if (variants != null && !variants.isEmpty() && selectedColorName != null) {
            // Find the variant with the selected color
            for (Product.Variant variant : variants) {
                if (variant.getColor().equals(selectedColorName)) {
                    List<Product.Variant.SizeQuantity> sizeQuantities = variant.getSizes();
                    List<SizeItem> sizes = new ArrayList<>();


                    if (sizeQuantities != null) {
                        for (int i = 0; i < sizeQuantities.size(); i++) {
                            Product.Variant.SizeQuantity sizeQty = sizeQuantities.get(i);
                            // Only add sizes that are in stock (quantity > 0)
                            if (sizeQty.getQuantity() > 0) {
                                sizes.add(new SizeItem(String.valueOf(i), sizeQty.getSize()));
                            }
                        }
                    }

                    sizesLiveData.setValue(sizes);

                    // Set default selected size if none is selected or current selection is not available
                    if (selectedSize.getValue() == null && !sizes.isEmpty()) {
                        selectedSize.setValue(sizes.get(0).getName());
                    } else {
                        boolean sizeExists = false;
                        for (SizeItem size : sizes) {
                            if (size.getName().equals(selectedSize.getValue())) {
                                sizeExists = true;
                                break;
                            }
                        }
                        if (!sizeExists && !sizes.isEmpty()) {
                            selectedSize.setValue(sizes.get(0).getName());
                        }
                    }

                    break;
                }
            }
        }
    }

    private void setupDefaultColorsAndSizes(int productType) {
        // Default colors
        List<ColorItem> defaultColors = new ArrayList<>();
        defaultColors.add(new ColorItem("1", "Đen", "#000000"));
        defaultColors.add(new ColorItem("2", "Trắng", "#FFFFFF"));
        defaultColors.add(new ColorItem("3", "Be", "#F5F5DC"));
        colorsLiveData.setValue(defaultColors);

        // Default sizes based on product type
        List<SizeItem> defaultSizes = new ArrayList<>();
        if (productType == 0) { // Áo/quần
            defaultSizes.add(new SizeItem("1", "S"));
            defaultSizes.add(new SizeItem("2", "M"));
            defaultSizes.add(new SizeItem("3", "L"));
            defaultSizes.add(new SizeItem("4", "XL"));
            defaultSizes.add(new SizeItem("5", "2XL"));
        } else if (productType == 1) { // Giày
            defaultSizes.add(new SizeItem("1", "38"));
            defaultSizes.add(new SizeItem("2", "39"));
            defaultSizes.add(new SizeItem("3", "40"));
            defaultSizes.add(new SizeItem("4", "41"));
            defaultSizes.add(new SizeItem("5", "42"));
        } else {
            defaultSizes.add(new SizeItem("1", "S"));
            defaultSizes.add(new SizeItem("2", "M"));
            defaultSizes.add(new SizeItem("3", "L"));
        }
        sizesLiveData.setValue(defaultSizes);

        // Set default selected values
        if (selectedColor.getValue() == null && !defaultColors.isEmpty()) {
            selectedColor.setValue(defaultColors.get(0).getName());
        }
        if (selectedSize.getValue() == null && !defaultSizes.isEmpty()) {
            selectedSize.setValue(defaultSizes.get(0).getName());
        }
    }

    public void loadSimilarProducts(String categoryId) {
        if (categoryId == null || categoryId.isEmpty()) {
            similarProductsLiveData.setValue(new ArrayList<>());
            return;
        }

        productRepository.getProductsByCategory(categoryId, 10, products -> {
            // Filter out current product if it's in the list
            Product currentProduct = productLiveData.getValue();
            List<Product> filteredProducts = new ArrayList<>();

            for (Product product : products) {
                if (currentProduct != null && !product.getProductId().equals(currentProduct.getProductId())) {
                    filteredProducts.add(product);
                }
            }

            // Limit to 10 products
            List<Product> limitedProducts = filteredProducts.size() > 10 ?
                    filteredProducts.subList(0, 10) :
                    filteredProducts;

            similarProductsLiveData.setValue(limitedProducts);
        });
    }

    private void loadUserDetailsForRatings(List<Rating> ratings) {
        if (ratings == null || ratings.isEmpty()) {
            userMapLiveData.setValue(new HashMap<>());
            return;
        }

        // Tạo set các UID để tránh truy vấn trùng lặp
        Set<String> uniqueUserIds = new HashSet<>();
        for (Rating rating : ratings) {
            uniqueUserIds.add(rating.getUid());
        }

        Map<String, User> userMap = new HashMap<>();
        AtomicInteger pendingRequests = new AtomicInteger(uniqueUserIds.size());

        for (String userId : uniqueUserIds) {
            userRepository.getUserById(userId, user -> {
                if (user != null) {
                    userMap.put(userId, user);
                }
                if (pendingRequests.decrementAndGet() == 0) {
                    // Tất cả requests đã hoàn thành
                    userMapLiveData.setValue(userMap);
                }
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
        if (ratings != null) {
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
            } else {
                // Nếu không có đánh giá, đặt tất cả phần trăm về 0
                Map<Integer, Integer> zeroPercentages = new HashMap<>();
                for (int i = 1; i <= 5; i++) {
                    zeroPercentages.put(i, 0);
                }
                ratingDistributionLiveData.setValue(zeroPercentages);
            }
        }
    }


    // Thêm các phương thức sắp xếp và lọc đánh giá
    public void sortRatingsByDate(boolean newestFirst) {
        List<Rating> currentRatings = ratingsLiveData.getValue();
        if (currentRatings != null && !currentRatings.isEmpty()) {
            currentRatings.sort((r1, r2) -> {
                long time1 = r1.getCreatedAtMillis();
                long time2 = r2.getCreatedAtMillis();
                return newestFirst ? Long.compare(time2, time1) : Long.compare(time1, time2);
            });
            ratingsLiveData.setValue(currentRatings);
        }
    }

    public void sortRatingsByStars(boolean highestFirst) {
        List<Rating> currentRatings = ratingsLiveData.getValue();
        if (currentRatings != null && !currentRatings.isEmpty()) {
            currentRatings.sort((r1, r2) -> {
                return highestFirst ? Integer.compare(r2.getRate(), r1.getRate()) : Integer.compare(r1.getRate(), r2.getRate());
            });
            ratingsLiveData.setValue(currentRatings);
        }
    }

    public void filterRatingsByStars(int stars) {
        if (stars == 0) { // 0 = tất cả đánh giá
            ratingsLiveData.setValue(new ArrayList<>(originalRatings));
        } else if (!originalRatings.isEmpty()) {
            List<Rating> filteredRatings = originalRatings.stream()
                    .filter(rating -> rating.getRate() == stars)
                    .collect(Collectors.toList());
            ratingsLiveData.setValue(filteredRatings);
        }
    }

    // Getter và setter
    public void setSelectedColor(String color) {
        selectedColor.setValue(color);

        // Update sizes based on new selected color
        Product product = productLiveData.getValue();
        if (product != null) {
            updateSizesForSelectedColor(product);
        }
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

    // Thêm phương thức lấy danh sách ảnh sản phẩm
    public LiveData<List<String>> getProductImages() {
        return productImagesLiveData;
    }

    // Getters cho LiveData mới
    public LiveData<List<ColorItem>> getColors() {
        return colorsLiveData;
    }

    public LiveData<List<SizeItem>> getSizes() {
        return sizesLiveData;
    }
}

