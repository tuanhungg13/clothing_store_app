package com.project.clothingstore.viewmodel

import CartItem
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.project.clothingstore.modal.Coupon
import com.project.clothingstore.modal.Order
import com.project.clothingstore.modal.OrderItem
import com.project.clothingstore.service.CheckoutService
import java.util.UUID

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

    private val _couponDiscountAmount = MutableLiveData(0.0)
    val couponDiscountAmount: LiveData<Double> get() = _couponDiscountAmount

    private val _couponErrorMessage = MutableLiveData<String>()
    val couponErrorMessage: LiveData<String> get() = _couponErrorMessage

    var orderItems: List<CartItem> = emptyList()

    init {
        // Observer cho _couponDiscountAmount
        couponDiscountAmount.observeForever {
            calculateTotalPrice()
        }
    }

    // Tính toán tổng tiền
    fun calculateTotalPrice() {
        val itemsPrice = orderItems.sumOf { it.price * it.quantity }
        val discountAmount = _couponDiscountAmount.value ?: 0.0
        Log.d("TAG", "Tổng tiền sản phẩm: $itemsPrice, Giảm giá: $discountAmount")
        val shipPrice = calculateShippingPrice()
        shippingPrice.value = shipPrice
        totalPrice.value = itemsPrice - discountAmount + shipPrice
    }

    fun calculateTotalPriceOfProducts() {
        val itemsPrice = orderItems.sumOf { it.price * it.quantity }
        totalPriceProduct.value = itemsPrice.toDouble()
    }

    // Áp dụng mã giảm giá
    fun applyCouponDiscount(code: String, userId: String) {
        if (code.isNotBlank()) {
            CheckoutService.getCouponByCode(code,
                onSuccess = { coupon ->
                    val currentTimestamp = Timestamp.now()

                    if (coupon.expirationDate.toDate().before(currentTimestamp.toDate())) {
                        // ❌ Mã giảm giá đã hết hạn
                        _couponErrorMessage.postValue("Mã giảm giá đã hết hạn.")
                        couponId.value = null
                        _couponDiscountAmount.postValue(0.0)
                        Log.e("TAG", "Mã giảm giá đã hết hạn: $code")
                        return@getCouponByCode
                    }
                    // 🔽 Lấy danh sách couponId của user
                    CheckoutService.getCouponIdsOfUser(userId,
                        onResult = { userCoupons ->
                            if (userCoupons.contains(coupon.couponId)) {
                                // ✅ User có mã này → áp dụng
                                couponId.value = coupon.couponId
                                val discountAmount = calculateDiscountAmount(coupon)
                                _couponDiscountAmount.postValue(discountAmount)
                                _couponErrorMessage.postValue("Áp dụng thành công.")
                                Log.d(
                                    "TAG",
                                    "Mã giảm giá đã được áp dụng: $code, Giảm giá: $discountAmount"
                                )
                            } else {
                                // ❌ User không có mã này
                                Log.e("TAG", "User không có mã giảm giá này: $code")
                                _couponErrorMessage.postValue("Mã giảm giá không hợp lệ hoặc không thuộc về bạn.")
                                couponId.value = null
                                _couponDiscountAmount.postValue(0.0)
                            }
                        },
                        onError = { e ->
                            Log.e("TAG", "Lỗi khi kiểm tra coupon của user: ${e.message}")
                            _couponErrorMessage.postValue("Lỗi khi kiểm tra mã giảm giá.")
                            couponId.value = null
                            _couponDiscountAmount.postValue(0.0)
                        }
                    )
                },
                onError = {
                    Log.e("TAG", "Mã giảm giá không hợp lệ: $code")
                    _couponErrorMessage.postValue("Mã giảm giá không tồn tại.")
                    couponId.value = null
                    _couponDiscountAmount.postValue(0.0)
                }
            )
        } else {
            _couponDiscountAmount.postValue(0.0)
            calculateTotalPrice()
        }
    }

    // Tính toán giảm giá từ coupon
    private fun calculateDiscountAmount(coupon: Coupon): Double {
        val itemsPrice = orderItems.sumOf { it.price * it.quantity } + (shippingPrice.value ?: 0.0)
        return if (itemsPrice >= coupon.minOrder) {
            coupon.discount
        } else {
            couponId.value = null
            0.0
        }
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
            orderItems = cleanedOrderItems,
            shippingPrice = shippingPrice.value ?: 0.0,
            discount = _couponDiscountAmount.value ?: 0.0,
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
