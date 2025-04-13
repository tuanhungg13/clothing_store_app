package com.project.clothingstore.modal

import Variant
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Order(
    val orderId: String = "",
    val uid: String = "",
    val customerName: String = "",
    val phoneCustomer: String = "",
    @ServerTimestamp
    val orderDate: Date? = null,
    val status: String = "PENDING",
    val discount: Double = 0.0,
    val orderItems: List<OrderItem> = emptyList(),
    val shippingPrice: Double = 0.0,
    val couponId: String? = null,
    val address: String = "",
    val totalPrice: Double = 0.0,
    val province: String = "",
    val district: String = "",
    val ward: String = "",
)

data class OrderItem(
    val productId: String = "",
    val productName: String = "",
    val variant: Variant = Variant(),
    val image: String = "",
    var quantity: Int = 0,
    val price: Int = 0,
)



