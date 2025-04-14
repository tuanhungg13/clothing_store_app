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

