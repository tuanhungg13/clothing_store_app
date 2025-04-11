package com.project.clothingstore.service

import Cart
import CartItem
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CartService {

    private val firestore = FirebaseFirestore.getInstance()

    // Lấy dữ liệu giỏ hàng từ Firebase
    suspend fun fetchCartItems(cartId: String): List<CartItem> {
        return try {
            val documentSnapshot = firestore.collection("carts")
                .document(cartId)
                .get()
                .await()

            if (!documentSnapshot.exists()) {
                Log.w("Firestore", "Cart document not found with ID: $cartId")
                return emptyList()
            }

            val cart = documentSnapshot.toObject(Cart::class.java)
            val cartItems = cart?.cartItems ?: return emptyList()

            // Duyệt từng item để lấy stock tương ứng
            cartItems.map { item ->
                val productSnapshot = firestore.collection("products")
                    .document(item.productId)
                    .get()
                    .await()

                val variants = productSnapshot["variants"] as? List<Map<String, Any>> ?: emptyList()

                val matchedVariant = variants.find {
                    it["size"] == item.variant.size && it["color"] == item.variant.color
                }

                val stock = (matchedVariant?.get("stock") as? Long)?.toInt() ?: 0
                item.stock = stock // 👈 gán stock vào item
                item
            }

        } catch (e: Exception) {
            Log.e("Firestore", "Error fetching cart items: ${e.message}", e)
            emptyList()
        }
    }


    // Cập nhật số lượng sản phẩm
    suspend fun updateItemQuantity(cartItem: CartItem, cartId: String): Boolean = runCatching {
        val cartRef = firestore.collection("carts").document(cartId)
        val currentItems =
            cartRef.get().await().get("cartItems") as? List<Map<String, Any>> ?: return false
        val updatedItems = currentItems.map { item ->
            if (
                item["productId"] == cartItem.productId &&
                (item["variant"] as? Map<*, *>)?.get("color") == cartItem.variant.color &&
                (item["variant"] as? Map<*, *>)?.get("size") == cartItem.variant.size
            ) {
                item.toMutableMap().apply {
                    this["quantity"] = cartItem.quantity
                    this.remove("stock") // ✅ Xoá thuộc tính stock nếu có
                }
            } else {
                item.toMutableMap().apply {
                    this.remove("stock") // ✅ Cẩn thận xoá luôn ở các item còn lại nếu lỡ có thêm
                }
            }
        }


        cartRef.update("cartItems", updatedItems).await()
        true
    }.getOrElse {
        it.printStackTrace()
        false
    }

    suspend fun removeItemFromCart(cartItem: CartItem, cartId: String): Boolean = runCatching {
        val cartRef = firestore.collection("carts").document(cartId)
        val currentItems =
            cartRef.get().await().get("cartItems") as? List<Map<String, Any>> ?: return false

        val updatedItems = currentItems.filterNot { item ->
            item["productId"] == cartItem.productId &&
                    (item["variant"] as? Map<*, *>)?.get("color") == cartItem.variant.color &&
                    (item["variant"] as? Map<*, *>)?.get("size") == cartItem.variant.size
        }

        cartRef.update("cartItems", updatedItems).await()
        true
    }.getOrElse {
        it.printStackTrace()
        false
    }

    suspend fun removeMultipleItemsFromCart(cartItems: List<CartItem>, cartId: String): Boolean =
        runCatching {
            val cartRef = firestore.collection("carts").document(cartId)
            val currentItems =
                cartRef.get().await().get("cartItems") as? List<Map<String, Any>> ?: return false

            val updatedItems = currentItems.filterNot { item ->
                cartItems.any { cartItem ->
                    item["productId"] == cartItem.productId &&
                            (item["variant"] as? Map<*, *>)?.get("color") == cartItem.variant.color &&
                            (item["variant"] as? Map<*, *>)?.get("size") == cartItem.variant.size
                }
            }

            cartRef.update("cartItems", updatedItems).await()
            true
        }.getOrElse {
            it.printStackTrace()
            false
        }


}
