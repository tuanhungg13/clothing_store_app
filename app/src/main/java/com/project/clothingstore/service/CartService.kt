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
            val cartItems = cart?.cartItems

            if (cartItems.isNullOrEmpty()) {
                Log.d("Firestore", "Cart is empty or cartItems is null")
                emptyList()
            } else {
                Log.d("Firestore", "Fetched cartItems: $cartItems")
                cartItems
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
                item.toMutableMap().apply { this["quantity"] = cartItem.quantity }
            } else item
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
