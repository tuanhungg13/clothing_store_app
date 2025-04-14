package com.project.clothingstore.viewmodel

import CartItem
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.clothingstore.service.CartService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CartViewModel(private val cartService: CartService) : ViewModel() {
    private var updateJob: Job? = null
    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> get() = _cartItems
    private val selectedItems = mutableSetOf<CartItem>()
    private val _shippingFee = MutableLiveData<Int>()
    private val _totalPriceProduct = MutableLiveData<Int>()
    private val _totalPrice = MutableLiveData<Int>()
    val totalPrice: LiveData<Int> get() = _totalPrice
    val totalPriceProduct: LiveData<Int> get() = _totalPriceProduct
    val shippingFee: LiveData<Int> get() = _shippingFee
    // Lấy dữ liệu giỏ hàng từ Firebase
//    fun fetchCartItems(userId: String) {
//        viewModelScope.launch {
//            val items = cartService.fetchCartItems(userId)
//            _cartItems.value = items
//            _totalPrice.value = calculateTotalPrice(items)
//        }
//    }

    fun fetchCartItems(cartId: String) {
        viewModelScope.launch {
            val items = cartService.fetchCartItems(cartId)

            // Giữ lại trạng thái isSelected nếu đã được chọn trước đó
            val updatedItems = items.map { newItem ->
                val isSelected = selectedItems.any {
                    it.productId == newItem.productId && it.variant == newItem.variant
                }
                newItem.copy(isSelected = isSelected)
            }

            _cartItems.value = updatedItems

            // Cập nhật lại selectedItems (dựa theo danh sách mới)
            selectedItems.clear()
            selectedItems.addAll(updatedItems.filter { it.isSelected })

            // Tính lại tổng tiền và phí ship
            _totalPriceProduct.value = calculateSelectedItemsTotalPrice()
            _totalPrice.value = calculateTotalPrice()
            _shippingFee.value = if (selectedItems.isNotEmpty()) 30000 else 0

            Log.d("CartViewModel", "Fetched items: $updatedItems")
        }
    }


    fun calculateSelectedItemsTotalPrice(): Int {
        val selectedItemsList = selectedItems.toList()
        return selectedItemsList.sumBy { it.price * it.quantity }
    }


    // Tính tổng tiền giỏ hàng
    private fun calculateTotalPrice(): Int {
        val total = calculateSelectedItemsTotalPrice()
        return if (selectedItems.isNotEmpty()) total + 30000 else total
    }


    // Cập nhật số lượng sản phẩm
    fun updateItemQuantity(cartItem: CartItem, newQuantity: Int, cartId: String) {
        cartItem.quantity = newQuantity

        updateJob?.cancel()  // Hủy job cũ nếu người dùng thao tác lại

        updateJob = viewModelScope.launch {
            delay(500) // Chờ 500ms sau lần cuối thao tác

            val success = cartService.updateItemQuantity(cartItem, cartId)
            if (success) {
                fetchCartItems(cartId)
            } else {
                Toast.makeText(
                    null,
                    "Cập nhật số lượng thất bại!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun getSelectedItems(): List<CartItem> = selectedItems.toList()

    fun toggleItemSelection(cartItem: CartItem, isSelected: Boolean) {
        cartItem.isSelected = isSelected // Thêm dòng này

        if (isSelected) {
            if (!selectedItems.any { it.productId == cartItem.productId && it.variant == cartItem.variant }) {
                selectedItems.add(cartItem)
            }
        } else {
            selectedItems.removeIf {
                it.productId == cartItem.productId && it.variant == cartItem.variant
            }
        }
        Log.d("CartViewModel", "Selected items: $selectedItems")
        val totalProduct = calculateSelectedItemsTotalPrice()
        val totalPrice = calculateTotalPrice()
        _totalPriceProduct.value = totalProduct
        _totalPrice.value = totalPrice
        _shippingFee.value = if (selectedItems.isNotEmpty()) 30000 else 0
    }


    fun deleteItemFromCart(cartItem: CartItem, cartId: String) {
        viewModelScope.launch {
            val success = cartService.removeItemFromCart(cartItem, cartId)
            if (success) {
                fetchCartItems(cartId) // cập nhật lại danh sách
            } else {
                Toast.makeText(
                    null,
                    "Xóa sản phẩm thất bại!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    fun deleteMultipleItemsFromCart(cartItems: List<CartItem>, cartId: String) {
        viewModelScope.launch {
            val success = cartService.removeMultipleItemsFromCart(cartItems, cartId)
            if (success) {
                fetchCartItems(cartId)
            }
        }
    }
}

