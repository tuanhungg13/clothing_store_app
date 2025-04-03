package com.project.clothingstore.service

import CartItem
import Variant

class CartService {

    private val cartItems = mutableListOf<CartItem>()

    // Thêm một sản phẩm vào giỏ hàng
    fun addToCart(cartItem: CartItem) {
        val existingItem =
            cartItems.find { it.productId == cartItem.productId && it.variant == cartItem.variant }
        if (existingItem != null) {
            // Nếu sản phẩm đã có trong giỏ, tăng số lượng lên
            existingItem.quantity += cartItem.quantity
        } else {
            // Nếu sản phẩm chưa có trong giỏ, thêm mới
            cartItems.add(cartItem)
        }
    }

    // Xóa một sản phẩm khỏi giỏ hàng
    fun removeFromCart(productId: String, variant: Variant) {
        val itemToRemove = cartItems.find { it.productId == productId && it.variant == variant }
        itemToRemove?.let { cartItems.remove(it) }
    }

    // Cập nhật số lượng sản phẩm
    fun updateQuantity(productId: String, variant: Variant, quantity: Int) {
        val itemToUpdate = cartItems.find { it.productId == productId && it.variant == variant }
        itemToUpdate?.quantity = quantity
    }

    // Lấy tất cả sản phẩm trong giỏ hàng
    fun getCartItems(): List<CartItem> {
        return cartItems
    }

    // Tính tổng giá trị giỏ hàng
    fun calculateTotalPrice(): Int {
        return cartItems.sumBy { it.price * it.quantity }
    }
}
