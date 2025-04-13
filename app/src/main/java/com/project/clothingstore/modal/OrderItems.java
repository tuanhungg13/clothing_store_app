package com.project.clothingstore.modal;

public class OrderItems {
    String productId;
    String productName;
    String productImage;
    int quantity;
    int price;
    Variant variant;

    public static class Variant {
        String color;
        String size;

        public Variant() {
        }

        public Variant(String color, String size) {
            this.color = color;
            this.size = size;
        }

        public String getColor() {
            return color;
        }
        public void setColor(String color) {
            this.color = color;
        }
    }

    // getters and setters
    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getProductImage() {
        return productImage;
    }
    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public Variant getVariant() {
        return variant;
    }
    public void setVariant(Variant variant) {
        this.variant = variant;
    }
    public String getColor() {
        return variant.color;
    }
    public void setColor(String color) {
        this.variant.color = color;
    }
    public String getSize() {
        return variant.size;
    }
    public void setSize(String size) {
        this.variant.size = size;
    }
    public OrderItems(String productId, String productName, String productImage, int quantity, int price, Variant variant) {
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.quantity = quantity;
        this.price = price;
        this.variant = variant;
    }
    public OrderItems() {
    }

}
