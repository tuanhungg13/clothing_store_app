

package com.project.clothingstore.modal;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.util.List;

public class Orders implements Parcelable {

    String orderId;
    String uid;
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

    public Orders() {}

    public Orders(String userId, String customerName, String customerPhone, String address, String district, String province, String ward, String couponId, int discount, List<OrderItems> orderItems, int shippingPrice, String status) {
        this.uid = userId;
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
    // Khi check đã cmt vào sản phẩm chưa
    public Orders(Orders order) {
        this.orderId = order.orderId;
        this.uid = order.uid;
        this.customerName = order.customerName;
        this.customerPhone = order.customerPhone;
        this.address = order.address;
        this.district = order.district;
        this.province = order.province;
        this.ward = order.ward;
        this.couponId = order.couponId;
        this.orderDate = order.orderDate; // Timestamp là immutable nên gán trực tiếp được
        this.discount = order.discount;

        // Tạo bản sao mới cho danh sách OrderItems nếu cần
        this.orderItems = (order.orderItems != null) ?
                new java.util.ArrayList<>(order.orderItems) : null;

        this.shippingPrice = order.shippingPrice;
        this.status = order.status;
        this.price = order.price;
    }
    // - end


    // Getters and setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return uid;
    }

    public void setUserId(String userId) {
        this.uid = userId;
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

    // Tính tổng giá trị đơn hàng
    public int calculateTotalPrice() {
        int totalProductPrice = 0;
        totalProductPrice = orderItems.get(0).getPrice() * orderItems.get(0).getQuantity() + shippingPrice - discount;
        return totalProductPrice;
    }

    // Tính tổng giá trị sản phẩm trước khi giảm giá
    public int calculateSubPrice() {
        int subTotalPrice = 0;
        for (OrderItems item : orderItems) {
            subTotalPrice += item.getPrice() * item.getQuantity();
        }
        return subTotalPrice;
    }

    // Parcelable methods
    public Orders(Parcel in) {
        orderId = in.readString();
        uid = in.readString();
        customerName = in.readString();
        customerPhone = in.readString();
        address = in.readString();
        district = in.readString();
        province = in.readString();
        ward = in.readString();
        couponId = in.readString();
        orderDate = in.readParcelable(Timestamp.class.getClassLoader());
        discount = in.readInt();
        orderItems = in.createTypedArrayList(OrderItems.CREATOR);
        shippingPrice = in.readInt();
        status = in.readString();
        price = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderId);
        dest.writeString(uid);
        dest.writeString(customerName);
        dest.writeString(customerPhone);
        dest.writeString(address);
        dest.writeString(district);
        dest.writeString(province);
        dest.writeString(ward);
        dest.writeString(couponId);
        dest.writeParcelable(orderDate, flags);
        dest.writeInt(discount);
        dest.writeTypedList(orderItems);
        dest.writeInt(shippingPrice);
        dest.writeString(status);
        dest.writeInt(price);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Orders> CREATOR = new Creator<Orders>() {
        @Override
        public Orders createFromParcel(Parcel in) {
            return new Orders(in);
        }

        @Override
        public Orders[] newArray(int size) {
            return new Orders[size];
        }
    };
}

