package com.project.clothingstore.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.clothingstore.modal.Order
import com.project.clothingstore.modal.RevenueEntry
import com.project.clothingstore.service.DashboardService

class DashboardViewModel : ViewModel() {

    private val dashboardService = DashboardService()

    private val _totalOrders = MutableLiveData<Int>()
    val totalOrders: LiveData<Int> get() = _totalOrders

    private val _totalRevenue = MutableLiveData<Float>()
    val totalRevenue: LiveData<Float> get() = _totalRevenue

    private val _recentOrders = MutableLiveData<List<Order>>()
    val recentOrders: LiveData<List<Order>> get() = _recentOrders

    private val _revenueChartData = MutableLiveData<List<RevenueEntry>>()
    val revenueChartData: LiveData<List<RevenueEntry>> get() = _revenueChartData

    fun fetchDashboardData() {
        dashboardService.getTotalOrders { count ->
            _totalOrders.postValue(count)
        }

        dashboardService.getTotalRevenue { revenue ->
            _totalRevenue.postValue(revenue)
        }

        dashboardService.getRecentOrders { orders ->
            _recentOrders.postValue(orders)
        }

        dashboardService.getRevenueChartData { revenueEntries ->
            _revenueChartData.postValue(revenueEntries)
        }
    }
}
