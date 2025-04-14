package com.project.clothingstore.view.activity

import CartItem
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.project.clothingstore.R
import com.project.clothingstore.adapter.CheckoutAdapter
import com.project.clothingstore.service.CartService
import com.project.clothingstore.utils.helper.formatPrice
import com.project.clothingstore.viewmodel.CartViewModel
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
    private lateinit var cartViewModel: CartViewModel
    private lateinit var tvShipping: TextView
    private lateinit var tvDiscount: TextView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        val cartService = CartService()
        // Init ViewModel
        viewModel = ViewModelProvider(this)[CheckoutViewModel::class.java]
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        val viewModelFactory = CartViewModelFactory(cartService)
        cartViewModel = ViewModelProvider(this, viewModelFactory).get(CartViewModel::class.java)
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
        tvShipping = findViewById(R.id.tvShipping) // TextView cho phí vận chuyển
        tvDiscount = findViewById(R.id.tvDiscount) // TextView cho mã giảm giá
        btnBack.setOnClickListener() {
            finish()
        }
        rgShipping.check(R.id.rbStandardShipping)
        val rootLayout: View = findViewById(R.id.root_layout) // Lấy root layout của Activity

        rootLayout.setOnTouchListener { v, event ->
            val currentFocus = currentFocus
            if (currentFocus != null && event.action == MotionEvent.ACTION_DOWN) {
                val outRect = Rect()
                currentFocus.getGlobalVisibleRect(outRect)
                // Kiểm tra nếu người dùng chạm ra ngoài các trường nhập liệu
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    hideKeyboard(currentFocus) // Đóng bàn phím khi nhấn ra ngoài
                }
            }
            false
        }
        edtAddress.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Xử lý sự kiện khi người dùng nhấn nút "Hoàn tất"
                hideKeyboard(v) // Ẩn bàn phím
                true // Trả về true để ngừng xử lý sự kiện mặc định
            } else {
                false
            }
        }
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
        var cartId: String = "";
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
                    cartId = it.cartId // Lưu cartId từ user
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
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        // Lắng nghe sự kiện khi người dùng nhấn nút Thanh toán
        btnCheckout.setOnClickListener {
            if (isInputValid()) {
                val phoneNumber = edtPhone.text.toString()

                if (isPhoneNumberValid(phoneNumber)) {
                    userId?.let {
                        viewModel.submitOrder(it)

                        // Lắng nghe kết quả submit để xóa giỏ hàng
                        viewModel.orderStatus.observe(this) { isSuccess ->
                            if (isSuccess == true) {
                                // 🧹 Xóa giỏ hàng sau khi đặt hàng thành công
                                cartViewModel.deleteMultipleItemsFromCart(
                                    viewModel.orderItems,
                                    cartId
                                )
                                Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_SHORT)
                                    .show()
                                val intent =
                                    Intent(this, CheckoutSuccessActivity::class.java).apply {
                                        flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    }
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this,
                                    "Đặt hàng thất bại. Vui lòng thử lại!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show()
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
            viewModel.calculateTotalPrice()

        }
        viewModel.shippingPrice.observe(this) { shippingPrice ->
            tvShipping.text = formatPrice(shippingPrice)
        }

        viewModel.couponDiscountAmount.observe(this) { discount ->
            tvDiscount.text = formatPrice(discount)
        }

        // Lắng nghe sự thay đổi khi nhập mã giảm giá và cập nhật tổng tiền
        btnApplyCoupon.setOnClickListener {
            val couponCode = edtCoupon.text.toString()
            if (userId != null) {
                viewModel.applyCouponDiscount(couponCode, userId)
            }
        }

        // Trong Activity hoặc Fragment của bạn
        viewModel.couponErrorMessage.observe(this, { errorMessage ->
            if (errorMessage.isNotBlank()) {
                // Hiển thị thông báo lỗi
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })


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

    fun isPhoneNumberValid(phoneNumber: String): Boolean {
        // Biểu thức chính quy cho số điện thoại hợp lệ tại Việt Nam
        val phonePattern = "^(\\+84|0)(9|8|7|3|4|5|6|1)[0-9]{8}$".toRegex()

        return phonePattern.matches(phoneNumber)
    }

    fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}


