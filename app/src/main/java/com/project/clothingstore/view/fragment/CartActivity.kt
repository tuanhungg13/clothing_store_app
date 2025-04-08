package com.project.clothingstore.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.clothingstore.R
import com.project.clothingstore.adapter.CartAdapter
import com.project.clothingstore.service.CartService
import com.project.clothingstore.utils.helper.formatPrice
import com.project.clothingstore.viewmodel.CartViewModel

class CartActivity : AppCompatActivity() {

    private lateinit var cartViewModel: CartViewModel
    private lateinit var cartAdapter: CartAdapter
    private lateinit var rvCartItems: RecyclerView
    private lateinit var tvTotal: TextView
    private lateinit var btnCheckout: TextView
    private lateinit var tvProductPrice: TextView
    private lateinit var btnBack: ImageView
    private lateinit var shimmerLayout: View

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Khởi tạo các view
        rvCartItems = findViewById(R.id.rvCartItems)
        tvTotal = findViewById(R.id.tvTotal)
        btnCheckout = findViewById(R.id.btnCheckout)
        tvProductPrice = findViewById(R.id.tvProductPrice)
        btnBack = findViewById(R.id.btnBack)
        shimmerLayout = findViewById(R.id.shimmerLayout)

        btnBack.setOnClickListener {
            finish() // 👉 quay về Activity trước đó
        }
// Ẩn RV khi chưa có data
        rvCartItems.visibility = View.GONE
        shimmerLayout.visibility = View.VISIBLE
        // Khởi tạo CartService
        val cartService = CartService() // Hoặc cung cấp dịch vụ khác tùy vào cách bạn inject

        // Khởi tạo ViewModel bằng factory
        val factory = CartViewModelFactory(cartService)
        cartViewModel = ViewModelProvider(this, factory).get(CartViewModel::class.java)

        // Lấy dữ liệu giỏ hàng
        val cartId = "8BZkdJaHU7SS0VH72mnz"
        cartViewModel.fetchCartItems(cartId)

        // Cập nhật UI khi có thay đổi
        cartViewModel.cartItems.observe(this, Observer { items ->
            // Cập nhật danh sách sản phẩm
            shimmerLayout.visibility = View.GONE
            rvCartItems.visibility = View.VISIBLE
            cartAdapter = CartAdapter(
                items.toMutableList(),
                onQuantityChanged = { cartItem, newQuantity ->
                    cartViewModel.updateItemQuantity(cartItem, newQuantity, cartId)
                },
                onItemRemoved = { cartItem ->
                    cartViewModel.deleteItemFromCart(cartItem, cartId)
                },
                onSelectionChanged = { cartItem, isSelected ->
                    cartViewModel.toggleItemSelection(cartItem, isSelected)
                }

            )
            rvCartItems.layoutManager = LinearLayoutManager(this)
            rvCartItems.adapter = cartAdapter
        })

        // Cập nhật tổng tiền khi thay đổi
        cartViewModel.totalPrice.observe(this, Observer { total ->
            tvProductPrice.text = formatPrice(total)
            tvTotal.text = formatPrice(total)
        })

        // Lắng nghe sự kiện nút thanh toán
        btnCheckout.setOnClickListener {
            val selectedItems =
                cartViewModel.cartItems.value?.filter { it.isSelected } ?: emptyList()

            if (selectedItems.isNotEmpty()) {
//                val intent = Intent(this, CheckoutActivity::class.java)
//                intent.putParcelableArrayListExtra("selected_items", ArrayList(selectedItems))
//                startActivity(intent)
            } else {
                Toast.makeText(this, "Vui lòng chọn sản phẩm để thanh toán", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}


class CartViewModelFactory(private val cartService: CartService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CartViewModel(cartService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

