package com.project.clothingstore.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.project.clothingstore.modal.Coupon;
import com.project.clothingstore.service.CouponService;

import java.util.ArrayList;
import java.util.List;

public class CouponViewModel extends ViewModel {

    private final CouponService couponService = new CouponService();

    private final MutableLiveData<List<Coupon>> couponListLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Coupon> selectedCouponLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<String>();

    public LiveData<List<Coupon>> getCouponListLiveData() {
        return couponListLiveData;
    }

    public LiveData<Coupon> getSelectedCouponLiveData() {
        return selectedCouponLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    // Lấy danh sách coupon theo list ID
    public void fetchCouponsByIds(String uid,List<String> couponIds) {
        if (couponIds == null || couponIds.isEmpty()) {
            couponListLiveData.setValue(new ArrayList<>()); // Trả về danh sách rỗng nếu không có mã
            return;
        }

        // Lọc bỏ các ID rỗng hoặc null
        List<String> validIds = new ArrayList<>();
        for (String id : couponIds) {
            if (id != null && !id.trim().isEmpty()) {
                validIds.add(id.trim());
            }
        }

        if (validIds.isEmpty()) {
            couponListLiveData.setValue(new ArrayList<>());
            return;
        }

        couponService.getCouponsByIds(uid,validIds, new CouponService.CouponListCallback() {
            @Override
            public void onSuccess(List<Coupon> coupons) {
                couponListLiveData.setValue(coupons);
            }

            @Override
            public void onFailure(Exception e) {
                errorLiveData.setValue(e.getMessage());
            }
        });
    }


    // Lấy tất cả coupon (dành cho admin hoặc hiển thị store)
    public void fetchAllCoupons() {
        couponService.getAllCoupons(new CouponService.CouponListCallback() {
            @Override
            public void onSuccess(List<Coupon> coupons) {
                couponListLiveData.setValue(coupons);
            }

            @Override
            public void onFailure(Exception e) {
                errorLiveData.setValue(e.getMessage());
            }
        });
    }

    // Tìm kiếm coupon theo mã code người dùng nhập
    public void fetchCouponByCode(String code) {
        couponService.getCouponByCode(code, new CouponService.CouponCallback() {
            @Override
            public void onSuccess(Coupon coupon) {
                selectedCouponLiveData.setValue(coupon);
            }

            @Override
            public void onFailure(Exception e) {
                errorLiveData.setValue(e.getMessage());
            }
        });
    }

    // Thêm coupon vào user
    public void addCouponToUser(String uid, String couponId) {
        couponService.addCouponToUser(uid, couponId);
    }

    // Xóa coupon khỏi user
    public void removeCouponFromUser(String uid, String couponId) {
        couponService.removeCouponFromUser(uid, couponId);
    }

    // Xóa lỗi sau khi xử lý
    public void clearError() {
        errorLiveData.setValue(null);
    }
}
