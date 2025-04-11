package com.project.clothingstore.view.activity

import CartItem
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.project.clothingstore.R
import com.project.clothingstore.adapter.CheckoutAdapter
import com.project.clothingstore.viewmodel.CheckoutViewModel
import com.project.clothingstore.viewmodel.UserViewModel

class CheckoutActivity : AppCompatActivity() {

    // ViewModel
    private lateinit var viewModel: CheckoutViewModel
    private lateinit var userViewModel: UserViewModel

    // UI Components
    private lateinit var edtCustomerName: EditText
    private lateinit var edtPhone: EditText
    private lateinit var edtProvince: EditText
    private lateinit var edtDistrict: EditText
    private lateinit var edtWard: EditText
    private lateinit var edtAddress: EditText
    private lateinit var edtCoupon: EditText
    private lateinit var btnApplyCoupon: Button
    private lateinit var rgShipping: RadioGroup
    private lateinit var rgPayment: RadioGroup
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnCheckout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        // Init ViewModel
        viewModel = ViewModelProvider(this)[CheckoutViewModel::class.java]
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        // Init UI Components
        edtCustomerName = findViewById(R.id.edtCustomerName)
        edtPhone = findViewById(R.id.edtPhone)
        edtProvince = findViewById(R.id.edtProvince)
        edtDistrict = findViewById(R.id.edtDistrict)
        edtWard = findViewById(R.id.edtWard)
        edtAddress = findViewById(R.id.edtAddress)
        edtCoupon = findViewById(R.id.edtCoupon)
        btnApplyCoupon = findViewById(R.id.btnApplyCoupon)
        rgShipping = findViewById(R.id.rgShipping)
        rgPayment = findViewById(R.id.rgPayment)
        btnCheckout = findViewById(R.id.btnCheckout)
        recyclerView = findViewById(R.id.rvOrderItems)

        // Nhận danh sách sản phẩm được chọn từ Intent
        val selectedItems = intent.getParcelableArrayListExtra<CartItem>("selected_items")
        selectedItems?.let {
            viewModel.orderItems = it
            viewModel.calculateTotalPrice()
        }

        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CheckoutAdapter(this, viewModel.orderItems)
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        uid?.let {
            userViewModel.fetchUserInfo(it) // Gọi hàm load dữ liệu trước

            // Lắng nghe khi dữ liệu currentUser được cập nhật
            userViewModel.getCurrentUser().observe(this) { user ->
                user?.let {
                    edtCustomerName.setText(it.fullName)
                    edtPhone.setText(it.phoneNumber)
                    edtProvince.setText(it.address.province)
                    edtDistrict.setText(it.address.district)
                    edtWard.setText(it.address.ward)
                    edtAddress.setText(it.address.street)
                }
            }
        }

    }
}
