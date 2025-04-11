package com.project.clothingstore.service

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.project.clothingstore.modal.Order
import com.project.clothingstore.modal.RevenueEntry
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DashboardService {

    private val db = FirebaseFirestore.getInstance()

    // Lấy tổng số đơn hàng
    fun getTotalOrders(onResult: (Int) -> Unit) {
        db.collection("orders").get()
            .addOnSuccessListener { snapshot ->
                onResult(snapshot.size())
            }
            .addOnFailureListener {
                onResult(0)
            }
    }

    // Lấy tổng doanh thu
    fun getTotalRevenue(onResult: (Float) -> Unit) {
        db.collection("orders").get()
            .addOnSuccessListener { snapshot ->
                var total = 0f
                for (doc in snapshot) {
                    val revenue = doc.getDouble("totalPrice") ?: 0.0
                    total += revenue.toFloat()
                }
                onResult(total)
            }
            .addOnFailureListener {
                onResult(0f)
            }
    }

    // Lấy tổng số người dùng
    fun getUserCount(onResult: (Int) -> Unit) {
        db.collection("users").get()
            .addOnSuccessListener { snapshot ->
                onResult(snapshot.size())
            }
            .addOnFailureListener {
                onResult(0)
            }
    }

    // Lấy số lượng sản phẩm còn trong kho
    fun getStockProductCount(onResult: (Int) -> Unit) {
        db.collection("products").get()
            .addOnSuccessListener { snapshot ->
                var count = 0
                for (doc in snapshot) {
                    val stock = doc.getLong("stock") ?: 0L
                    if (stock > 0) count++
                }
                onResult(count)
            }
            .addOnFailureListener {
                onResult(0)
            }
    }

    // Lấy các đơn hàng gần đây (5 đơn hàng gần nhất)
    fun getRecentOrders(onResult: (List<Order>) -> Unit) {
        db.collection("orders")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(5)
            .get()
            .addOnSuccessListener { snapshot ->
                val orders = snapshot.mapNotNull { it.toObject(Order::class.java) }
                onResult(orders)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    // Lấy dữ liệu doanh thu theo tháng để vẽ biểu đồ
    fun getRevenueChartData(onResult: (List<RevenueEntry>) -> Unit) {
        db.collection("orders").get()
            .addOnSuccessListener { snapshot ->
                val map = mutableMapOf<String, Float>()
                for (doc in snapshot) {
                    val timestamp = doc.getTimestamp("createdAt")?.toDate()
                    val month =
                        SimpleDateFormat("MMM", Locale.getDefault()).format(timestamp ?: Date())
                    val revenue = doc.getDouble("totalPrice") ?: 0.0
                    map[month] = (map[month] ?: 0f) + revenue.toFloat()
                }
                val result = map.map { RevenueEntry(it.key, it.value) }
                onResult(result)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }
}

