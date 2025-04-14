package com.project.clothingstore.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.clothingstore.service.DashboardService;
import com.project.clothingstore.utils.Event;

import java.util.Date;
import java.util.Map;

public class DashboardViewModel extends ViewModel {

    private final DashboardService dashboardService = new DashboardService();

    private final MutableLiveData<Integer> orderCount = new MutableLiveData<>();
    private final MutableLiveData<Integer> newUserCount = new MutableLiveData<>();
    private final MutableLiveData<Integer> shippedCount = new MutableLiveData<>();
    private final MutableLiveData<Integer> ratedCount = new MutableLiveData<>();
    private final MutableLiveData<Map<String, Double>> revenueData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Event<String>> errorMessage = new MutableLiveData<>();

    public LiveData<Integer> getOrderCount() {
        return orderCount;
    }
    public LiveData<Map<String, Double>> getRevenueData() {
        return revenueData;
    }

    public LiveData<Integer> getNewUserCount() {
        return newUserCount;
    }

    public LiveData<Integer> getShippedCount() {
        return shippedCount;
    }

    public LiveData<Integer> getRatedCount() {
        return ratedCount;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Event<String>> getErrorMessage() {
        return errorMessage;
    }
    public interface ValueEventListener {
        void onDataLoaded(int order, int newUser, int shipped, int rating);
        default void onError(String error) {}
    }

    public void loadDashboardData(Date selectedDate) {
        isLoading.setValue(true);

        dashboardService.fetchDashboardData(selectedDate, new DashboardService.ValueEventListener() {
            @Override
            public void onDataLoaded(int order, int newUser, int shipped, int rating) {
                orderCount.postValue(order);
                newUserCount.postValue(newUser);
                shippedCount.postValue(shipped);
                ratedCount.postValue(rating);
                isLoading.postValue(false);
            }

            @Override
            public void onError(String error) {
                errorMessage.postValue(new Event<>(error));
                isLoading.postValue(false);
            }
        });
    }

    public void loadRevenue7Days() {
        isLoading.setValue(true);

        dashboardService.fetchRevenueLast7Days(new DashboardService.OnRevenueDataListener() {
            @Override
            public void onRevenueLoaded(Map<String, Double> dailyRevenue) {
                revenueData.postValue(dailyRevenue);
                isLoading.postValue(false);
            }

            @Override
            public void onError(String error) {
                errorMessage.postValue(new Event<>(error));
                isLoading.postValue(false);
            }
        });
    }

}
