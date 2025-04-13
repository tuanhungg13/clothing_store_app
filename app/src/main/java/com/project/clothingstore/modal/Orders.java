package com.project.clothingstore.modal;

import com.google.firebase.Timestamp;

import java.util.List;

public class Orders {

    String orderId;
    String userId;
    String customerName;
    String customerPhone;
    String address;
    String district;
    String province;
    String ward;
    String couponId;
    private Timestamp orderDate;
    int discount;
    List<OrderItems> orderItems;
    int shippingPrice; // Shipping fee
    String status; // Order status (e.g., "PENDING", "SUCCESS", "CANCEL")
    int price; // Total price after discount and shipping fee

    public Orders() {
    }

    public Orders(String userId, String customerName, String customerPhone, String address, String district, String province, String ward, String couponId, int discount, List<OrderItems> orderItems, int shippingPrice, String status) {
        this.userId = userId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.address = address;
        this.district = district;
        this.province = province;
        this.ward = ward;
        this.couponId = couponId;
        this.discount = discount;
        this.orderItems = orderItems;
        this.shippingPrice = shippingPrice;
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public List<OrderItems> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItems> orderItems) {
        this.orderItems = orderItems;
    }

    public int getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(int shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPrice() {
        return price;
    }

    public void setTotalPrice(int price) {
        this.price = price;
    }

    public int calculateTotalPrice() {
        int totalProductPrice = 0;
        totalProductPrice = orderItems.get(0).getPrice() * orderItems.get(0).getQuantity() + shippingPrice - discount;
//        for (OrderItems item : orderItems) {
//            totalProductPrice += item.getPrice() * item.getQuantity();
//        }
//        this.totalPrice = totalProductPrice - discount + shippingPrice;
        return totalProductPrice;
    }


}
