package com.project.clothingstore.viewmodel

import CartItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.clothingstore.modal.Order
import com.project.clothingstore.modal.OrderItem
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
    val shippingMethod = MutableLiveData("standard") // "standard" or "express"
    val shippingPrice = MutableLiveData(30000.0)
    val paymentMethod = MutableLiveData("cod")
    val totalPrice = MutableLiveData(0.0)
    val totalPriceProduct = MutableLiveData(0.0)
    private val _orderStatus = MutableLiveData<Boolean>()
    val orderStatus: LiveData<Boolean> get() = _orderStatus
    var orderItems: List<CartItem> = emptyList()

    // Tính toán tổng tiền
    fun calculateTotalPrice() {
        val itemsPrice = orderItems.sumOf { it.price * it.quantity }
        val discountAmount = calculateDiscount()
        val shipPrice = calculateShippingPrice()
        shippingPrice.value = shipPrice
        totalPrice.value = itemsPrice - discountAmount + shipPrice
    }

    fun calculateTotalPriceOfProducts() {
        val itemsPrice = orderItems.sumOf { it.price * it.quantity }
        totalPriceProduct.value = itemsPrice.toDouble()
    }

    // Tính toán giảm giá từ coupon (nếu có)
    private fun calculateDiscount(): Double {
        // Thêm logic tính giảm giá từ coupon ở đây
        return 0.0 // Ví dụ giả định không có giảm giá
    }

    // Tính toán phí vận chuyển
    private fun calculateShippingPrice(): Double {
        return if (shippingMethod.value == "express") {
            100000.0
        } else {
            30000.0
        }
    }


    // Tạo đối tượng Order từ thông tin hiện tại
    fun createOrder(uid: String): Order {
        val cleanedOrderItems = orderItems.map { cartItem ->
            OrderItem(
                productId = cartItem.productId,
                productName = cartItem.productName,
                variant = cartItem.variant,
                image = cartItem.image,
                quantity = cartItem.quantity,
                price = cartItem.price
            )
        }

        return Order(
            orderId = UUID.randomUUID().toString(),
            uid = uid,
            customerName = customerName.value ?: "",
            phoneCustomer = phoneCustomer.value ?: "",
            orderItems = cleanedOrderItems, // Đưa vào danh sách đã lọc
            shippingPrice = shippingPrice.value ?: 0.0,
            discount = calculateDiscount(),
            couponId = couponId.value?.takeIf { it.isNotBlank() },
            address = address.value ?: "",
            totalPrice = totalPrice.value ?: 0.0,
            province = province.value ?: "",
            district = district.value ?: "",
            ward = ward.value ?: ""
        )
    }


    // Gửi đơn hàng lên server
    fun submitOrder(uid: String) {
        val order = createOrder(uid)

        CheckoutService.submitOrder(order,
            onSuccess = { _orderStatus.postValue(true) },
            onError = { _orderStatus.postValue(false) }
        )
    }
}

