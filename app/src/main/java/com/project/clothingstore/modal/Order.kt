package com.project.clothingstore.modal

import CartItem
import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.android.parcel.Parcelize
import java.util.Date

data class Order(
    val orderId: String = "",
    val uid: String = "",
    val customerName: String = "",
    val phoneCustomer: String = "",
    @ServerTimestamp
    val orderDate: Date? = null  ,
    val status: String = "PENDING",
    val discount: Double = 0.0,
    val orderItems: List<CartItem> = emptyList(),
    val shippingPrice: Double = 0.0,
    val couponId: String? = null,
    val address: String = "",
    val totalPrice: Double = 0.0,
    val province: String = "",
    val district: String = "",
    val ward: String = "",
)

