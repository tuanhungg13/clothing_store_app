package com.project.clothingstore.modal;

import java.util.List;

public class Product {
    String productId;
    private String productName;
    private String description;
    private int price;
    private String categoryId;
    private List<String> images; // nếu ảnh là mảng 2 chiều
    private String collectionId;
    private double discount;
    private List<Variant> variants;
    private int priceBeforeDiscount;
    private String ratings;
    private String type;

    public Product(String productName, int price, String categoryId, List<String> images, int priceBeforeDiscount, String ratings) {
        this.productName = productName;
        this.price = price;
        this.categoryId = categoryId;
        this.images = images;
        this.priceBeforeDiscount = priceBeforeDiscount;
        this.ratings = ratings;
    }

    public Product(String productName, int price, List<String> images) {
        this.productName = productName;
        this.price = price;
        this.images = images;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getPriceBeforeDiscount() {
        return priceBeforeDiscount;
    }

    public void setPriceBeforeDiscount(int priceBeforeDiscount) {
        this.priceBeforeDiscount = priceBeforeDiscount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class Variant {
        private String color;
        private List<SizeQuantity> sizes;

        public static class SizeQuantity {
            private String size;
            private int quantity;

            // Getters & setters
        }

        // Getters & setters
    }

    // Getters & setters cho Product
}
