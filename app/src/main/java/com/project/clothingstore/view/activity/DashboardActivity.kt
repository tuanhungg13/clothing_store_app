package com.project.clothingstore.view.activity

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.project.clothingstore.databinding.ActivityDashboardBinding
import com.project.clothingstore.modal.RevenueEntry
import com.project.clothingstore.viewmodel.DashboardViewModel

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Gọi phương thức fetchDashboardData để lấy dữ liệu
        viewModel.fetchDashboardData()

        setupObservers()
    }

    private fun setupObservers() {
        // Quan sát dữ liệu doanh thu để vẽ biểu đồ khi có dữ liệu
        viewModel.revenueChartData.observe(this) { data ->
            if (data.isNotEmpty()) {
                setUpBarChart(data)
            }
        }
    }

    private fun setUpBarChart(data: List<RevenueEntry>) {
        // Chuyển đổi dữ liệu thành danh sách các entry cho biểu đồ
        val entries = data.mapIndexed { index, item ->
            BarEntry(index.toFloat(), item.revenue)
        }

        // Tạo dataset cho biểu đồ
        val dataSet = BarDataSet(entries, "Doanh thu theo tháng")
        dataSet.color = Color.parseColor("#FF6200EE")
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 12f

        // Tạo dữ liệu cho biểu đồ và thiết lập
        val barData = BarData(dataSet)
        binding.barChart.data = barData

        // Thiết lập trục X cho biểu đồ
        val xAxis = binding.barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(data.map { it.month })
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)

        // Ẩn trục Y bên phải và mô tả của biểu đồ
        binding.barChart.axisRight.isEnabled = false
        binding.barChart.description.isEnabled = false

        // Làm mới biểu đồ để hiển thị dữ liệu mới
        binding.barChart.invalidate()
    }
}
