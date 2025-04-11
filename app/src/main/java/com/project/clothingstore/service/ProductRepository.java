//package com.project.clothingstore.service;
//
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.project.clothingstore.R;
//import com.project.clothingstore.modal.Product;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.Consumer;
//
//public class ProductRepository {
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
//
////    public void getProductById(String productId, Consumer<Product> callback) {
////        Product product = new Product();
////        product.setProductId(productId);
////        product.setProductName("Áo phông cao cấp");
////        product.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
////        product.setPrice(80);
////        product.setTotalRating(4.9f);
////
////        // Thêm danh sách ảnh từ drawable
////        List<String> imageList = new ArrayList<>();
////        imageList.add(String.valueOf(R.drawable.aophong));  // Sử dụng ID từ drawable
////        imageList.add(String.valueOf(R.drawable.dothethao));  // Thêm ảnh khác nếu cần
////        imageList.add(String.valueOf(R.drawable.aophong));  // Sử dụng ID từ drawable
////        imageList.add(String.valueOf(R.drawable.dothethao));
////        product.setImages(imageList);
////
////        callback.accept(product);
////    }
//
//    public void getProductsByCategory(String categoryId, int limit, Consumer<List<Product>> callback) {
//        // In a real app, this would query Firebase or another data source
//        // For now, we'll create dummy similar products
//        List<Product> similarProducts = new ArrayList<>();
//
//        for (int i = 1; i <= limit; i++) {
//            Product product = new Product();
//            product.setProductId("similar_" + i);
//            product.setProductName("Sản phẩm tương tự " + i);
//            product.setPrice(75 + i * 5);
//            product.setTotalRating(4.5f);
//            similarProducts.add(product);
//        }
//
//        callback.accept(similarProducts);
//    }
//}


package com.project.clothingstore.service;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.clothingstore.modal.ColorItem;
import com.project.clothingstore.modal.Product;
import com.project.clothingstore.modal.SizeItem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ProductRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void getProductById(String productId, Consumer<Product> callback) {
        DocumentReference productRef = db.collection("products").document(productId);
        productRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Product product = documentSnapshot.toObject(Product.class);
                if (product != null) {
                    callback.accept(product);
                }
            }
        }).addOnFailureListener(e -> {
            e.printStackTrace();
            callback.accept(null);
        });
    }

    public void getProductsByCategory(String categoryId, int limit, Consumer<List<Product>> callback) {
        if (categoryId == null || categoryId.isEmpty()) {
            callback.accept(new ArrayList<>()); // Return empty list if categoryId is invalid
            return;
        }

        db.collection("products")
                .whereEqualTo("categoryId", categoryId)
                .limit(10) // Lấy tối đa 10 sản phẩm
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Product> products = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Product product = document.toObject(Product.class);
                        if (product != null) {
                            product.setProductId(document.getId());
                            products.add(product);
                        }
                    }
                    callback.accept(products);
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    callback.accept(new ArrayList<>()); // Return empty list on failure
                });
    }

    public void getProductColors(String productId, Consumer<List<ColorItem>> callback) {
        db.collection("products").document(productId)
                .collection("colors")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<ColorItem> colors = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String id = document.getId();
                        String name = document.getString("name");
                        String hexCode = document.getString("hexCode");
                        colors.add(new ColorItem(id, name, hexCode));
                    }
                    callback.accept(colors);
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    callback.accept(new ArrayList<>()); // Return empty list on failure
                });
    }

    public void getProductSizes(String productId, Consumer<List<SizeItem>> callback) {
        db.collection("products").document(productId)
                .collection("sizes")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<SizeItem> sizes = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String id = document.getId();
                        String name = document.getString("name");
                        sizes.add(new SizeItem(id, name));
                    }
                    callback.accept(sizes);
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    callback.accept(new ArrayList<>()); // Return empty list on failure
                });
    }


}

