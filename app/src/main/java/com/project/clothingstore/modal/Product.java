package com.project.clothingstore.modal;

import java.util.List;

public class Product {
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
