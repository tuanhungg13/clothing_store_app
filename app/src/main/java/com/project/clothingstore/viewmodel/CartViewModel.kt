package com.project.clothingstore.viewmodel

import CartItem
import Variant
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.clothingstore.service.CartService

class CartViewModel(private val cartService: CartService) {

    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> = _cartItems

    private val _totalPrice = MutableLiveData<Int>()
    val totalPrice: LiveData<Int> = _totalPrice

    init {
        loadCartItems()
    }

    // Lấy tất cả các sản phẩm trong giỏ hàng
    fun loadCartItems() {
        _cartItems.value = cartService.getCartItems()
        _totalPrice.value = cartService.calculateTotalPrice()
    }

    // Thêm sản phẩm vào giỏ hàng
    fun addProductToCart(cartItem: CartItem) {
        cartService.addToCart(cartItem)
        loadCartItems()
    }

    // Xóa sản phẩm khỏi giỏ hàng
    fun removeProductFromCart(productId: String, variant: Variant) {
        cartService.removeFromCart(productId, variant)
        loadCartItems()
    }

    // Cập nhật số lượng sản phẩm
    fun updateProductQuantity(productId: String, variant: Variant, quantity: Int) {
        cartService.updateQuantity(productId, variant, quantity)
        loadCartItems()
    }
}
