package com.project.clothingstore.service

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.project.clothingstore.modal.Order
import com.project.clothingstore.modal.OrderItem

object CheckoutService {
    private val db = FirebaseFirestore.getInstance()

    fun submitOrder(
        order: Order,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        Log.d("TAG", "Đang xử lý đơn hàng: ${order}")
        db.runTransaction { transaction ->

            val productRefs = mutableMapOf<String, List<Map<String, Any>>>()
            val cleanedOrderItems = mutableListOf<OrderItem>()

            // 1️⃣ Kiểm tra tồn kho và cập nhật variants
            order.orderItems.forEach { item ->
                val productRef = db.collection("products").document(item.productId)
                val snapshot = transaction.get(productRef)
                Log.d("TAG", "Đang kiểm tra tồn kho cho sản phẩm: ${item.variant}")
                val variants = snapshot.get("variants") as? List<Map<String, Any>>
                    ?: throw FirebaseFirestoreException(
                        "Sản phẩm không có biến thể!",
                        FirebaseFirestoreException.Code.ABORTED
                    )

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

                                sizeMap.toMutableMap().apply {
                                    this["quantity"] = (currentQty - item.quantity).toLong()
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

            // 2️⃣ Ghi các thay đổi vào Firestore
            productRefs.forEach { (productId, updatedVariants) ->
                val productRef = db.collection("products").document(productId)
                transaction.update(productRef, "variants", updatedVariants)
            }

            // 3️⃣ Tạo đơn hàng
            val orderRef = db.collection("orders").document(order.orderId)
            val cleanedOrder = order.copy(orderItems = cleanedOrderItems)
            transaction.set(orderRef, cleanedOrder)

        }.addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener { e ->
            Log.e("TAG", "Lỗi khi đặt đơn hàng: ${e.message}")
            onError("Lỗi khi đặt đơn hàng: ${e.message ?: "Lỗi không xác định"}")
        }
    }


}
