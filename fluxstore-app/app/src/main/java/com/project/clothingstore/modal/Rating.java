package com.project.clothingstore.modal;

import com.google.firebase.Timestamp;

public class Rating {

    private String ratingId;
    private String uid;
    private String productId;
    private int rate;
    private String comment;
    private Timestamp createdAt; // Thay đổi từ long sang Timestamp

    // Constructors
    public Rating() {
    }

    public Rating(String uid, String productId, int rate, String comment) {
        this.uid = uid;
        this.productId = productId;
        this.rate = rate;
        this.comment = comment;
        this.createdAt = Timestamp.now(); // Sử dụng Timestamp.now() thay vì System.currentTimeMillis()
    }

    // Getters & Setters
    public String getRatingId() {
        return ratingId;
    }

    public void setRatingId(String ratingId) {
        this.ratingId = ratingId;
    }

    public String getUid() {
        return uid;
    }

    public void setUidId(String uid) {
        this.uid = uid;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    // Thêm phương thức để lấy giá trị long từ Timestamp
    public long getCreatedAtMillis() {
        return createdAt != null ? createdAt.toDate().getTime() : 0;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }
}
