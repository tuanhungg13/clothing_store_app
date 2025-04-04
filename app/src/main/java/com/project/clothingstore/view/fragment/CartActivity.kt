package com.project.clothingstore.view.fragment

import CartItem
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.clothingstore.R
import com.project.clothingstore.service.CartService
import com.project.clothingstore.viewmodel.CartViewModel

class CartActivity : AppCompatActivity() {

    private lateinit var cartViewModel: CartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Khởi tạo ViewModel và Service
        val cartService = CartService()
//        cartViewModel = ViewModelProvider(
//            this,
//            CartViewModelFactory(cartService)
//        ).get(CartViewModel::class.java)

        // Quan sát sự thay đổi của giỏ hàng
        cartViewModel.cartItems.observe(this, Observer { cartItems ->
            // Cập nhật UI khi giỏ hàng thay đổi
            updateCartUI(cartItems)
        })

        cartViewModel.totalPrice.observe(this, Observer { totalPrice ->
            // Cập nhật tổng tiền giỏ hàng
            updateTotalPrice(totalPrice)
        })
    }

    private fun updateCartUI(cartItems: List<CartItem>) {
        // Cập nhật giao diện giỏ hàng
    }

    private fun updateTotalPrice(totalPrice: Int) {
        // Cập nhật tổng tiền giỏ hàng
    }
}

class CartViewModelFactory(private val cartService: CartService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CartViewModel(cartService) as T
    }
}
