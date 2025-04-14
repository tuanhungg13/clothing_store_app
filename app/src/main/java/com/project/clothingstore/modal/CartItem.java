package com.project.clothingstore.modal;

public class CartItem {
    private String productId;
    private String productName;
    private String image;
    private int quantity;
    private int price;
    private Carts.cartItem.variant variant;

    public CartItem() {
    }

    public CartItem(String productId, String productName, String image, int quantity, int price, Carts.cartItem.variant variant) {
        this.productId = productId;
        this.productName = productName;
        this.image = image;
        this.quantity = quantity;
        this.price = price;
        this.variant = variant;
    }

    public CartItem(String productId, String productName, String image, int quantity, int price) {
        this.productId = productId;
        this.productName = productName;
        this.image = image;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and Setters
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public Carts.cartItem.variant getVariant() {
        return variant;
    }

    public void setVariant(Carts.cartItem.variant variant) {
        this.variant = variant;
    }

    // Utility method
    public long getTotalPrice() {
        return (long) price * quantity;
    }

    // Inner class variant
    public static class variant {
        private String color;
        private String size;

        public variant() {
        }

        public variant(String color, String size) {
            this.color = color;
            this.size = size;
        }

        // Getters and Setters
        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }
    }
}
