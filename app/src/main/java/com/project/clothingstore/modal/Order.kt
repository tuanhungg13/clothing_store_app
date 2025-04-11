package com.project.clothingstore.modal

data class Order(
    val orderId: String = "",
    val uid: String = "",
    val customerName: String = "",
    val phoneCustomer: String = "",
    val orderDate: Long = 0L,
    val status: String = "",
    val discount: Double = 0.0,
    val orderItems: List<OrderItem> = emptyList(),
    val paymentId: String = "",
    val couponId: String? = null
)

data class OrderItem(
    val productId: String = "",
    val productName: String = "",
    val quantity: Int = 0,
    val price: Double = 0.0,
    val variant: Variant = Variant()
)

data class Variant(
    val size: String = "",
    val color: String = ""
)
