package com.project.clothingstore.viewmodel

import CartItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.clothingstore.modal.Order
import com.project.clothingstore.service.CheckoutService
import java.util.UUID

// ui/checkout/CheckoutViewModel.kt
class CheckoutViewModel : ViewModel() {

    val customerName = MutableLiveData("")
    val phoneCustomer = MutableLiveData("")
    val province = MutableLiveData("")
    val district = MutableLiveData("")
    val ward = MutableLiveData("")
    val address = MutableLiveData("")
    val couponId = MutableLiveData("")
    val shippingMethod = MutableLiveData("standard") // hoặc "express"
    val shippingPrice = MutableLiveData(0.0)
    val paymentMethod = MutableLiveData("cod") // Chưa dùng nhưng có thể mở rộng
    val totalPrice = MutableLiveData(0.0)

    private val _orderStatus = MutableLiveData<Boolean>()
    val orderStatus: LiveData<Boolean> get() = _orderStatus

    var orderItems: List<CartItem> = emptyList()

    fun calculateTotalPrice() {
        val itemsPrice = orderItems.sumOf { it.price * it.quantity }
        val discountAmount = 0.0 // Có thể tính theo coupon
        val shipPrice = shippingPrice.value ?: 0.0
        totalPrice.value = itemsPrice - discountAmount + shipPrice
    }

    fun submitOrder(uid: String) {
        val order = Order(
            orderId = UUID.randomUUID().toString(),
            uid = uid,
            customerName = customerName.value ?: "",
            phoneCustomer = phoneCustomer.value ?: "",
            orderItems = orderItems,
            shippingPrice = shippingPrice.value ?: 0.0,
            discount = 0.0,
            couponId = couponId.value?.takeIf { it.isNotBlank() },
            address = address.value ?: "",
            totalPrice = totalPrice.value ?: 0.0,
            province = province.value ?: "",
            district = district.value ?: "",
            ward = ward.value ?: ""
        )

        CheckoutService.submitOrder(order,
            onSuccess = { _orderStatus.postValue(true) },
            onError = { _orderStatus.postValue(false) }
        )
    }
}
