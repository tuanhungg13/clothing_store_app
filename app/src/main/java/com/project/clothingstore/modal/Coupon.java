package com.project.clothingstore.modal;

import com.google.firebase.Timestamp;

public class Coupon {
    private String couponId;
    private String title;
    private String code;
    private double discount;
    private double minOrder;
    private int quantity;
    private Timestamp createdAt;
    private Timestamp expirationDate;

    public Coupon() {
    }

    public Coupon(String couponId, String title, String code, double discount, double minOrder, int quantity, Timestamp createdAt, Timestamp expirationDate) {
        this.couponId = couponId;
        this.title = title;
        this.code = code;
        this.discount = discount;
        this.minOrder = minOrder;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.expirationDate = expirationDate;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getMinOrder() {
        return minOrder;
    }

    public void setMinOrder(double minOrder) {
        this.minOrder = minOrder;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Timestamp expirationDate) {
        this.expirationDate = expirationDate;
    }
}
