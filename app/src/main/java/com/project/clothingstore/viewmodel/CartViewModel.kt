package com.project.clothingstore.viewmodel

import CartItem
import android.util.Log
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

    private val _totalPrice = MutableLiveData<Int>()
    val totalPrice: LiveData<Int> get() = _totalPrice

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
            val items =
                cartService.fetchCartItems(cartId)  // Lấy giỏ hàng theo `cartId` thay vì `userId`
            _cartItems.value = items
            _totalPrice.value = calculateTotalPrice(items)
            Log.d("CartViewModel", "Fetched items: $items")
        }
    }


    // Tính tổng tiền giỏ hàng
    private fun calculateTotalPrice(items: List<CartItem>): Int {
        return items.sumBy { it.price * it.quantity }
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
                // Xử lý lỗi nếu cần
            }
        }
    }

    fun getSelectedItems(): List<CartItem> = selectedItems.toList()

    fun toggleItemSelection(cartItem: CartItem, isSelected: Boolean) {
        // Ví dụ: quản lý danh sách các item được chọn
        if (isSelected) {
            selectedItems.add(cartItem)
        } else {
            selectedItems.remove(cartItem)
        }
        // Có thể cập nhật LiveData nếu cần
    }


    fun deleteItemFromCart(cartItem: CartItem, cartId: String) {
        viewModelScope.launch {
            val success = cartService.removeItemFromCart(cartItem, cartId)
            if (success) {
                fetchCartItems(cartId) // cập nhật lại danh sách
            }
        }
    }


    fun deleteMultipleItemsFromCart(cartItems: List<CartItem>, cartId: String) {
        viewModelScope.launch {
            val success = cartService.removeMultipleItemsFromCart(cartItems, cartId)
            if (success) {
                // Xử lý nếu xóa nhiều sản phẩm thành công
            }
        }
    }
}

