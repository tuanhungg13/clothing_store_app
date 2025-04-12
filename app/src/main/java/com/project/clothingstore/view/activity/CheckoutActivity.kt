package com.project.clothingstore.view.activity

import CartItem
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.project.clothingstore.R
import com.project.clothingstore.adapter.CheckoutAdapter
import com.project.clothingstore.utils.helper.formatPrice
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
    private lateinit var tvTotalPrice: TextView
    private lateinit var btnBack: ImageView
private lateinit var tvProductPrice: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        // Init ViewModel
        viewModel = ViewModelProvider(this)[CheckoutViewModel::class.java]
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        // Init UI Components
        edtCustomerName = findViewById(R.id.edtCustomerName)
        edtPhone = findViewById(R.id.edtPhoneNumber)
        edtProvince = findViewById(R.id.edtProvince)
        edtDistrict = findViewById(R.id.edtDistrict)
        edtWard = findViewById(R.id.edtWard)
        edtAddress = findViewById(R.id.edtAddressDetail)
        edtCoupon = findViewById(R.id.edtCoupon)
        btnApplyCoupon = findViewById(R.id.btnApplyCoupon)
        rgShipping = findViewById(R.id.rgShipping)
        rgPayment = findViewById(R.id.rgPayment)
        btnCheckout = findViewById(R.id.btnCheckout)
        recyclerView = findViewById(R.id.rvOrderItems)
        tvTotalPrice = findViewById(R.id.tvTotal) // TextView cho tổng tiền
        btnBack = findViewById(R.id.btnBack)
        tvProductPrice = findViewById(R.id.tvProductPrice) // TextView cho tổng tiền sản phẩm
        btnBack.setOnClickListener() {
            finish()
        }
        rgShipping.check(R.id.rbStandardShipping)
        // Nhận danh sách sản phẩm được chọn từ Intent
        val selectedItems = intent.getParcelableArrayListExtra<CartItem>("selected_items")
        selectedItems?.let {
            viewModel.orderItems = it
            viewModel.calculateTotalPrice()
            viewModel.calculateTotalPriceOfProducts()
        }

        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CheckoutAdapter(this, viewModel.orderItems)
        viewModel.totalPriceProduct.observe(this) { totalPriceProduct ->
            tvProductPrice.text = "Tổng tiền: ${formatPrice(totalPriceProduct)}"
        }
        // Lắng nghe thay đổi totalPrice từ ViewModel và cập nhật UI
        viewModel.totalPrice.observe(this) { totalPrice ->
            tvTotalPrice.text = "Tổng tiền: ${formatPrice(totalPrice)}"
        }

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
        edtCustomerName.addTextChangedListener { editable ->
            viewModel.customerName.value = editable.toString().trim()
        }

        edtPhone.addTextChangedListener { editable ->
            viewModel.phoneCustomer.value = editable.toString().trim()
        }

        edtProvince.addTextChangedListener { editable ->
            viewModel.province.value = editable.toString().trim()
        }

        edtDistrict.addTextChangedListener { editable ->
            viewModel.district.value = editable.toString().trim()
        }

        edtWard.addTextChangedListener { editable ->
            viewModel.ward.value = editable.toString().trim()
        }

        edtAddress.addTextChangedListener { editable ->
            viewModel.address.value = editable.toString().trim()
        }


        // Nếu chỉ có 1 phương thức thanh toán thì mặc định chọn phương thức đó
        if (rgPayment.childCount == 1) {
            rgPayment.check(rgPayment.getChildAt(0).id)
        }

        // Lắng nghe sự kiện khi người dùng nhấn nút Thanh toán
        btnCheckout.setOnClickListener {
            if (isInputValid()) {
                // Gửi đơn hàng lên server
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                userId?.let {
                    viewModel.submitOrder(it)
                }
            } else {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            }
        }

        // Lắng nghe sự thay đổi của phương thức vận chuyển và cập nhật tổng tiền
        rgShipping.setOnCheckedChangeListener { _, checkedId ->
            // Kiểm tra ID của radio button được chọn
            when (checkedId) {
                R.id.rbStandardShipping -> {
                    viewModel.shippingMethod.value = "standard"  // Nếu chọn "Giao hàng nhanh"
                }
                R.id.rbExpressShipping -> {
                    viewModel.shippingMethod.value = "express"  // Nếu chọn "Giao hàng hỏa tốc"
                }
            }

            // Tính lại tổng tiền
            viewModel.calculateTotalPrice()
        }


        // Lắng nghe sự thay đổi khi nhập mã giảm giá và cập nhật tổng tiền
        btnApplyCoupon.setOnClickListener {
            val couponCode = edtCoupon.text.toString()
            viewModel.couponId.value = couponCode
            viewModel.calculateTotalPrice()
        }
    }

    // Kiểm tra các trường nhập liệu có hợp lệ không
    private fun isInputValid(): Boolean {
        return edtCustomerName.text.isNotEmpty() &&
                edtPhone.text.isNotEmpty() &&
                edtProvince.text.isNotEmpty() &&
                edtDistrict.text.isNotEmpty() &&
                edtWard.text.isNotEmpty() &&
                edtAddress.text.isNotEmpty()
    }
}


