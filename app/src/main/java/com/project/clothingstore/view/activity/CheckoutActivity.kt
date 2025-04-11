package com.project.clothingstore.view.activity

import CartItem
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.clothingstore.R
import com.project.clothingstore.adapter.CheckoutAdapter
import com.project.clothingstore.viewmodel.CheckoutViewModel

class CheckoutActivity : AppCompatActivity() {
    private lateinit var viewModel: CheckoutViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        viewModel = ViewModelProvider(this)[CheckoutViewModel::class.java]

        val selectedItems = intent.getParcelableArrayListExtra<CartItem>("selected_items")
        selectedItems?.let {
            viewModel.orderItems = it
            viewModel.calculateTotalPrice()
        }

        // Khởi tạo RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.rvOrderItems)
        recyclerView.layoutManager = LinearLayoutManager(this) // thêm dòng này
        recyclerView.adapter = CheckoutAdapter(this, viewModel.orderItems)

    }
}
