package com.project.clothingstore.modal;

public class Rating {

    private String ratingId;
    private String uidId;
    private String productId;
    private int rate;
    private String comment;
    private long createdAt;


    // Constructors
    public Rating() {
    }

    public Rating(String uidId, String productId, int rate, String comment) {
        this.uidId = uidId;
        this.productId = productId;
        this.rate = rate;
        this.comment = comment;
        this.createdAt = System.currentTimeMillis(); // Lưu timestamp
    }

    // Getters & Setters
    public String getRatingId() {
        return ratingId;
    }

    public void setRatingId(String ratingId) {
        this.ratingId = ratingId;
    }

    public String getUidId() {
        return uidId;
    }

    public void setUidId(String uidId) {
        this.uidId = uidId;
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

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public void setProductId(String productId) {

    }
}