package com.project.clothingstore.model;

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
