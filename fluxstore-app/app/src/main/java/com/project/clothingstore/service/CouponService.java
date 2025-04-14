package com.project.clothingstore.service;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.project.clothingstore.modal.Coupon;
import com.project.clothingstore.utils.helper.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

public class CouponService {
    private static final CollectionReference userRef = FirebaseHelper.getUserCollection();
    private static final CollectionReference couponRef = FirebaseHelper.getCouponCollection();
    public interface CouponListCallback {
        void onSuccess(List<Coupon> coupons);
        void onFailure(Exception e);
    }
    //Lấy về danh sách các đối tượng Coupon có trong danh sách coupons của user
    public void getCouponsByIds(String uid, List<String> couponIds, CouponListCallback callback) {
        if (couponIds == null || couponIds.isEmpty()) {
            callback.onSuccess(new ArrayList<>());
            return;
        }

        couponRef.whereIn(FieldPath.documentId(), couponIds)
                .get()
                .addOnSuccessListener(snapshot -> {
                    List<Coupon> validCoupons = new ArrayList<>();
                    List<String> expiredCouponIds = new ArrayList<>();

                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        Coupon coupon = doc.toObject(Coupon.class);
                        if (coupon != null) {
                            coupon.setCouponId(doc.getId());

                            // Kiểm tra hạn sử dụng
                            if (coupon.getExpirationDate() != null &&
                                    coupon.getExpirationDate().toDate().getTime() < System.currentTimeMillis()) {
                                expiredCouponIds.add(coupon.getCouponId());
                            } else {
                                validCoupons.add(coupon);
                            }
                        }
                    }

                    // Xóa các coupon đã hết hạn khỏi user
                    if (!expiredCouponIds.isEmpty()) {
                        Log.d("DEBUG", "ExpiredCouponIds: " + expiredCouponIds.toString());

                        userRef.document(uid).get()
                                .addOnSuccessListener(userSnapshot -> {
                                    if (userSnapshot.exists()) {
                                        Object couponField = userSnapshot.get("coupons");

                                        if (couponField instanceof List<?>) {
                                            List<?> rawList = (List<?>) couponField;
                                            List<String> currentCoupons = new ArrayList<>();

                                            for (Object o : rawList) {
                                                if (o instanceof String) currentCoupons.add((String) o);
                                            }
                                            currentCoupons.removeAll(expiredCouponIds);

                                            userRef.document(uid).update("coupons", currentCoupons)
                                                    .addOnSuccessListener(unused ->
                                                            Log.d("DEBUG", "Successfully updated coupon list"))
                                                    .addOnFailureListener(e ->
                                                            Log.e("DEBUG", "Error updating coupon list", e));
                                        } else {
                                            Log.e("DEBUG", "'coupon' field is not a List<?>");
                                        }
                                    } else {
                                        Log.e("DEBUG", "User document not found");
                                    }
                                });

                    }

                    callback.onSuccess(validCoupons);
                })
                .addOnFailureListener(callback::onFailure);
    }


    //Lấy tất cả bản ghi trong conpons
    public void getAllCoupons(CouponListCallback callback) {
        couponRef.get()
                .addOnSuccessListener(snapshot -> {
                    List<Coupon> coupons = new ArrayList<>();
                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        Coupon coupon = doc.toObject(Coupon.class);
                        if (coupon != null) {
                            coupon.setCouponId(doc.getId());
                            coupons.add(coupon);
                        }
                    }
                    callback.onSuccess(coupons);
                })
                .addOnFailureListener(callback::onFailure);
    }
    // Tìm coupon theo code (người dùng nhập để áp dụng)
    public interface CouponCallback {
        void onSuccess(Coupon coupon);
        void onFailure(Exception e);
    }
    public void getCouponByCode(String code, CouponCallback callback) {
        couponRef.whereEqualTo("code", code)
                .limit(1)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.isEmpty()) {
                        DocumentSnapshot doc = snapshot.getDocuments().get(0);
                        Coupon coupon = doc.toObject(Coupon.class);
                        if (coupon != null) {
                            coupon.setCouponId(doc.getId());
                            callback.onSuccess(coupon);
                        } else {
                            callback.onFailure(new Exception("Coupon is null"));
                        }
                    } else {
                        callback.onFailure(new Exception("Coupon not found"));
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }
    // Thêm couponId vào user
    public void addCouponToUser(String uid, String couponId) {
        userRef.document(uid).get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        List<String> currentCoupons = (List<String>) snapshot.get("coupon");
                        if (currentCoupons == null) currentCoupons = new ArrayList<>();
                        if (!currentCoupons.contains(couponId)) {
                            currentCoupons.add(couponId);
                            userRef.document(uid).update("coupon", currentCoupons);
                        }
                    }
                });
    }

    // Xóa couponId khỏi user
    public void removeCouponFromUser(String uid, String couponId) {
        userRef.document(uid).get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        List<String> currentCoupons = (List<String>) snapshot.get("coupon");
                        if (currentCoupons != null && currentCoupons.contains(couponId)) {
                            currentCoupons.remove(couponId);
                            userRef.document(uid).update("coupon", currentCoupons);
                        }
                    }
                });
    }
}
