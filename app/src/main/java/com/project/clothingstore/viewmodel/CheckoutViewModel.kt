package com.project.clothingstore.viewmodel

import CartItem
import android.util.Log
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
    val shippingMethod = MutableLiveData("standard") // "standard" or "express"
    val shippingPrice = MutableLiveData(0.0)
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
            shippingPrice.value ?: 0.0 + 100000.0 // Phí giao hàng hỏa tốc
        } else {
            shippingPrice.value ?: 0.0 +30000.0// Phí giao hàng tiêu chuẩn
        }
    }


    // Tạo đối tượng Order từ thông tin hiện tại
    fun createOrder(uid: String): Order {
        // Lọc lại orderItems và loại bỏ `isSelected` và `stock` trước khi tạo đơn hàng
        val cleanedOrderItems = orderItems.map { cartItem ->
            // Tạo bản sao CartItem mới, không đưa isSelected và stock vào Order
            cartItem.copy(
                isSelected = false,  // Chỉ để false trong trường hợp này, nếu bạn muốn
                stock = 0  // Cập nhật lại giá trị stock cho Order (không sử dụng trong đơn hàng)
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
        Log.d("CheckoutViewModel", "submitOrder: $uid")
        val order = createOrder(uid)

        CheckoutService.submitOrder(order,
            onSuccess = { _orderStatus.postValue(true) },
            onError = { _orderStatus.postValue(false) }
        )
    }
}

