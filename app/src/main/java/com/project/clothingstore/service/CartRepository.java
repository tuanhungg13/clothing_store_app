package com.project.clothingstore.service;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.clothingstore.modal.CartItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartRepository {
    private final FirebaseFirestore db;

    public CartRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    public interface CartOperationCallback {
        void onSuccess();
        void onFailure(Exception e);
    }

    public void addToCart(String cartId, CartItem newItem, CartOperationCallback callback) {
        // Tham chiếu đến document cartId trong collection carts
        DocumentReference cartRef = db.collection("carts").document(cartId);

        // Lấy dữ liệu giỏ hàng hiện tại
        cartRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();

                if (document.exists()) {
                    // Giỏ hàng đã tồn tại, cập nhật giỏ hàng
                    updateCart(cartRef, document, newItem, callback);
                } else {
                    // Giỏ hàng chưa tồn tại, tạo mới giỏ hàng
                    createNewCart(cartRef, newItem, callback);
                }
            } else {
                callback.onFailure(task.getException());
            }
        });
    }

    private void updateCart(DocumentReference cartRef, DocumentSnapshot document, CartItem newItem, CartOperationCallback callback) {
        // Lấy danh sách cartItems hiện tại
        List<Map<String, Object>> cartItems = (List<Map<String, Object>>) document.get("cartItems");

        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        boolean itemFound = false;
        int itemIndex = -1;

        // Kiểm tra xem sản phẩm đã tồn tại trong giỏ hàng chưa (cùng productId và variant)
        for (int i = 0; i < cartItems.size(); i++) {
            Map<String, Object> item = cartItems.get(i);
            String productId = (String) item.get("productId");
            Log.d("CartRepository pid", "Kiểm tra sản phẩm: " + newItem.getProductId());
            if (productId.equals(newItem.getProductId())) {
                // Kiểm tra variant
                Map<String, Object> variant = (Map<String, Object>) item.get("variant");

                if (variant != null &&
                        variant.get("color").equals(newItem.getVariant().getColor()) &&
                        variant.get("size").equals(newItem.getVariant().getSize())) {

                    // Sản phẩm đã tồn tại với cùng variant, cập nhật số lượng
                    itemFound = true;
                    itemIndex = i;
                    break;
                }
            }
        }

        if (itemFound) {
            // Cập nhật số lượng cho sản phẩm đã tồn tại
            Map<String, Object> existingItem = cartItems.get(itemIndex);
            long currentQuantity = (long) existingItem.get("quantity");
            existingItem.put("quantity", currentQuantity + newItem.getQuantity());
        } else {
            // Thêm sản phẩm mới vào giỏ hàng
            Map<String, Object> newItemMap = convertCartItemToMap(newItem);
            cartItems.add(newItemMap);
        }

        // Cập nhật giỏ hàng trong Firestore
        cartRef.update("cartItems", cartItems)
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(callback::onFailure);
    }

    private void createNewCart(DocumentReference cartRef, CartItem newItem, CartOperationCallback callback) {
        // Tạo danh sách sản phẩm mới với sản phẩm đầu tiên
        List<Map<String, Object>> cartItems = new ArrayList<>();
        cartItems.add(convertCartItemToMap(newItem));

        // Tạo dữ liệu giỏ hàng
        Map<String, Object> cartData = new HashMap<>();
        cartData.put("cartItems", cartItems);

        // Lưu giỏ hàng mới vào Firestore
        cartRef.set(cartData)
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(callback::onFailure);
    }

    private Map<String, Object> convertCartItemToMap(CartItem item) {
        Map<String, Object> itemMap = new HashMap<>();
        itemMap.put("productId", item.getProductId());
        itemMap.put("productName", item.getProductName());
        itemMap.put("image", item.getImage());
        itemMap.put("price", item.getPrice());
        itemMap.put("quantity", item.getQuantity());

        // Chuyển đổi variant
        Map<String, Object> variantMap = new HashMap<>();
        variantMap.put("color", item.getVariant().getColor());
        variantMap.put("size", item.getVariant().getSize());
        itemMap.put("variant", variantMap);

        return itemMap;
    }
}
