package com.project.clothingstore.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.auth.FirebaseAuth
import com.project.clothingstore.R
import com.project.clothingstore.adapter.CartAdapter
import com.project.clothingstore.service.CartService
import com.project.clothingstore.utils.helper.formatPrice
import com.project.clothingstore.viewmodel.CartViewModel
import com.project.clothingstore.viewmodel.UserViewModel

class CartActivity : AppCompatActivity() {

    private lateinit var cartViewModel: CartViewModel
    private lateinit var cartAdapter: CartAdapter
    private lateinit var rvCartItems: RecyclerView
    private lateinit var tvTotal: TextView
    private lateinit var btnCheckout: TextView
    private lateinit var tvProductPrice: TextView
    private lateinit var btnBack: ImageView
    private lateinit var shimmerLayout: View
    private var cartId: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔒 Kiểm tra đăng nhập bằng FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            val intent = Intent(this, AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            return
        }

        setContentView(R.layout.activity_cart)

        // Khởi tạo view
        rvCartItems = findViewById(R.id.rvCartItems)
        tvTotal = findViewById(R.id.tvTotal)
        btnCheckout = findViewById(R.id.btnCheckout)
        tvProductPrice = findViewById(R.id.tvProductPrice)
        btnBack = findViewById(R.id.btnBack)
        shimmerLayout = findViewById(R.id.shimmerLayout)

        btnBack.setOnClickListener {
            finish()
        }

        rvCartItems.visibility = View.GONE
        shimmerLayout.visibility = View.VISIBLE

        val cartService = CartService()
        val factory = CartViewModelFactory(cartService)
        cartViewModel = ViewModelProvider(this, factory).get(CartViewModel::class.java)
        val userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        uid?.let {
            userViewModel.fetchUserInfo(it)
        }

        userViewModel.currentUser.observe(this) { user ->
            user?.let {
                cartId = it.cartId
                cartViewModel.fetchCartItems(it.cartId)
                // 👉 Bạn có thể dùng cartId ở đây, ví dụ:
                Log.d("CHECKOUT", "Cart ID là: $cartId")
            }
        }
        cartViewModel.cartItems.observe(this, Observer { items ->
            shimmerLayout.visibility = View.GONE
            rvCartItems.visibility = View.VISIBLE

            cartId?.let { cid ->  // ✅ Đảm bảo cartId non-null
                cartAdapter = CartAdapter(
                    items.toMutableList(),
                    onQuantityChanged = { cartItem, newQuantity ->
                        cartViewModel.updateItemQuantity(cartItem, newQuantity, cid)
                    },
                    onItemRemoved = { cartItem ->
                        cartViewModel.deleteItemFromCart(cartItem, cid)
                    },
                    onSelectionChanged = { cartItem, isSelected ->
                        cartViewModel.toggleItemSelection(cartItem, isSelected)
                    }
                )

                rvCartItems.layoutManager = LinearLayoutManager(this)
                rvCartItems.adapter = cartAdapter
            } ?: run {
                Toast.makeText(this, "Không tìm thấy Cart ID", Toast.LENGTH_SHORT).show()
            }
        })


        cartViewModel.totalPrice.observe(this, Observer { total ->
            tvProductPrice.text = formatPrice(total)
            tvTotal.text = formatPrice(total)
        })

        btnCheckout.setOnClickListener {
            val selectedItems = cartViewModel.cartItems.value?.filter { it.isSelected } ?: emptyList()

            if (selectedItems.isNotEmpty()) {
                val intent = Intent(this, CheckoutActivity::class.java)
                intent.putParcelableArrayListExtra("selected_items", ArrayList(selectedItems))
                startActivity(intent)
            } else {
                Toast.makeText(this, "Vui lòng chọn sản phẩm để thanh toán", Toast.LENGTH_SHORT).show()
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
