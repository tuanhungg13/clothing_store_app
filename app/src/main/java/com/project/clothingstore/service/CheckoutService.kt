package com.project.clothingstore.service

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Transaction
import com.project.clothingstore.modal.Order

object CheckoutService {
    private val db = FirebaseFirestore.getInstance()

    fun submitOrder(
        order: Order,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        db.runTransaction { transaction ->

            val productRefs = mutableMapOf<String, List<Map<String, Any>>>()

            // 1. Kiểm tra từng sản phẩm trong order
            for (item in order.orderItems) {
                val productRef = db.collection("products").document(item.productId)
                val snapshot = transaction.get(productRef)

                val variants = snapshot.get("variants") as? List<Map<String, Any>> ?: throw FirebaseFirestoreException(
                    "Sản phẩm không có biến thể!", FirebaseFirestoreException.Code.ABORTED
                )

                val updatedVariants = variants.map { variant ->
                    if (variant["size"] == item.variant.size && variant["color"] == item.variant.color) {
                        val currentQty = (variant["quantity"] as? Long)?.toInt() ?: 0
                        if (currentQty < item.quantity) {
                            throw FirebaseFirestoreException(
                                "${item.productName} (${item.variant.color} - ${item.variant.size}) không đủ hàng!",
                                FirebaseFirestoreException.Code.ABORTED
                            )
                        }
                        variant.toMutableMap().apply {
                            this["quantity"] = currentQty - item.quantity
                        }
                    } else variant
                }

                // Lưu lại để sau cập nhật
                productRefs[item.productId] = updatedVariants
            }

            // 2. Cập nhật lại từng sản phẩm
            for ((productId, updatedVariants) in productRefs) {
                val productRef = db.collection("products").document(productId)
                transaction.update(productRef, "variants", updatedVariants)
            }

            // 3. Lưu đơn hàng
            val orderRef = db.collection("orders").document(order.orderId)
            transaction.set(orderRef, order)

        }.addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener { e ->
            onError(e.message ?: "Lỗi không xác định khi đặt hàng.")
        }
    }
}
