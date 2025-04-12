package com.project.clothingstore.service

import CartItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Transaction
import com.project.clothingstore.modal.Order
import android.util.Log

object CheckoutService {
    private val db = FirebaseFirestore.getInstance()

    fun submitOrder(
        order: Order,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        db.runTransaction { transaction ->

            val productRefs = mutableMapOf<String, List<Map<String, Any>>>()
            val cleanedOrderItems = mutableListOf<CartItem>()

            // 1️⃣ Đọc tất cả sản phẩm
            order.orderItems.forEach { item ->
                val productRef = db.collection("products").document(item.productId)
                val snapshot = transaction.get(productRef)

                val variants = snapshot.get("variants") as? List<Map<String, Any>>
                    ?: throw FirebaseFirestoreException("Sản phẩm không có biến thể!", FirebaseFirestoreException.Code.ABORTED)

                val updatedVariants = variants.map { variant ->
                    val color = variant["color"] as? String
                    val sizes = variant["sizes"] as? List<Map<String, Any>> ?: emptyList()

                    if (color == item.variant.color) {
                        val updatedSizes = sizes.map { sizeMap ->
                            val sizeName = sizeMap["size"] as? String
                            if (sizeName == item.variant.size) {
                                val currentQty = (sizeMap["quantity"] as? Long)?.toInt() ?: 0
                                if (currentQty < item.quantity) {
                                    throw FirebaseFirestoreException(
                                        "${item.productName} (${item.variant.color} - ${item.variant.size}) không đủ hàng!",
                                        FirebaseFirestoreException.Code.ABORTED
                                    )
                                }
                                val newQty = currentQty - item.quantity
                                sizeMap.toMutableMap().apply {
                                    this["quantity"] = newQty.toLong()
                                }
                            } else sizeMap
                        }

                        variant.toMutableMap().apply {
                            this["sizes"] = updatedSizes
                        }
                    } else variant
                }

                productRefs[item.productId] = updatedVariants
                cleanedOrderItems.add(item)
            }

            // 2️⃣ Đọc giỏ hàng TRƯỚC khi thực hiện bất kỳ thao tác ghi nào
            val cartRef = db.collection("carts").document(order.uid)
            val cartSnap = transaction.get(cartRef)
            val cartItems = cartSnap.get("items") as? List<Map<String, Any>> ?: emptyList()

            // 3️⃣ Thực hiện các ghi (update + set)
            productRefs.forEach { (productId, updatedVariants) ->
                val productRef = db.collection("products").document(productId)
                transaction.update(productRef, "variants", updatedVariants)
            }

            val orderRef = db.collection("orders").document(order.orderId)
            val cleanedOrder = order.copy(orderItems = cleanedOrderItems)
            transaction.set(orderRef, cleanedOrder)

            val updatedCartItems = cartItems.mapNotNull { cartItem ->
                val productId = cartItem["productId"] as? String ?: return@mapNotNull null
                val size = (cartItem["variant"] as? Map<String, Any>)?.get("size") as? String
                val color = (cartItem["variant"] as? Map<String, Any>)?.get("color") as? String

                val matchingOrderItem = cleanedOrderItems.find {
                    it.productId == productId && it.variant.size == size && it.variant.color == color
                }

                if (matchingOrderItem != null) {
                    val cartQty = (cartItem["quantity"] as? Long)?.toInt() ?: 0
                    val newQty = cartQty - matchingOrderItem.quantity

                    if (newQty > 0) {
                        cartItem.toMutableMap().apply {
                            this["quantity"] = newQty
                        }
                    } else null
                } else cartItem
            }

            transaction.update(cartRef, "cartItems", updatedCartItems)

        }.addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener { e ->
            Log.e("TAG", "Lỗi khi đặt đơn hàng: ${e.message}")
            onError(e.message ?: "Lỗi không xác định khi đặt hàng.")
        }
    }

}
