package com.project.clothingstore.service;

import com.project.clothingstore.R;
import com.project.clothingstore.modal.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ProductRepository {
//    public void getProductById(String productId, Consumer<Product> callback) {
//        // In a real app, this would query Firebase or another data source
//        // For now, we'll create a dummy product
//        Product product = new Product();
//        product.setProductId(productId);
//        product.setProductName("Áo phông cao cấp");
//        product.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
//        product.setPrice(80);
//        product.setTotalRating(4.9f);
//
//        callback.accept(product);
//    }

    public void getProductById(String productId, Consumer<Product> callback) {
        Product product = new Product();
        product.setProductId(productId);
        product.setProductName("Áo phông cao cấp");
        product.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
        product.setPrice(80);
        product.setTotalRating(4.9f);

        // Thêm danh sách ảnh từ drawable
        List<String> imageList = new ArrayList<>();
        imageList.add(String.valueOf(R.drawable.aophong));  // Sử dụng ID từ drawable
        imageList.add(String.valueOf(R.drawable.dothethao));  // Thêm ảnh khác nếu cần
        imageList.add(String.valueOf(R.drawable.aophong));  // Sử dụng ID từ drawable
        imageList.add(String.valueOf(R.drawable.dothethao));
        product.setImages(imageList);

        callback.accept(product);
    }

    public void getProductsByCategory(String categoryId, int limit, Consumer<List<Product>> callback) {
        // In a real app, this would query Firebase or another data source
        // For now, we'll create dummy similar products
        List<Product> similarProducts = new ArrayList<>();

        for (int i = 1; i <= limit; i++) {
            Product product = new Product();
            product.setProductId("similar_" + i);
            product.setProductName("Sản phẩm tương tự " + i);
            product.setPrice(75 + i * 5);
            product.setTotalRating(4.5f);
            similarProducts.add(product);
        }

        callback.accept(similarProducts);
    }
}

