package com.project.clothingstore.modal;

import java.util.List;
import java.util.ArrayList;

public class Carts {

    String cartId;
    private List<cartItem> cartItems;

    // Constructors
    public Carts() {
        this.cartItems = new ArrayList<>();
    }

    public Carts(String cartId) {
        this.cartId = cartId;
        this.cartItems = new ArrayList<>();
    }

    public Carts(String cartId, List<cartItem> cartItems) {
        this.cartId = cartId;
        this.cartItems = cartItems;
    }

    // Getters and Setters
    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public List<cartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<cartItem> cartItems) {
        this.cartItems = cartItems;
    }

    // Utility methods
    public boolean addItem(cartItem item) {
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        // Tìm kiếm sản phẩm đã tồn tại với cùng productId, color và size
        for (cartItem existingItem : cartItems) {
            if (existingItem.getProductId().equals(item.getProductId()) &&
                    existingItem.getVariant().getColor().equals(item.getVariant().getColor()) &&
                    existingItem.getVariant().getSize().equals(item.getVariant().getSize())) {
                // Sản phẩm đã tồn tại, cập nhật số lượng
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                return true; // Đã cập nhật
            }
        }

        // Sản phẩm chưa tồn tại, thêm mới
        cartItems.add(item);
        return false; // Đã thêm mới
    }

    // Sửa phương thức removeItem để sử dụng đúng kiểu dữ liệu
    public boolean removeItem(String productId, cartItem.variant variant) {
        for (int i = 0; i < cartItems.size(); i++) {
            cartItem item = cartItems.get(i);
            if (item.getProductId().equals(productId) &&
                    item.getVariant().getColor().equals(variant.getColor()) &&
                    item.getVariant().getSize().equals(variant.getSize())) {
                cartItems.remove(i);
                return true;
            }
        }
        return false;
    }

    // Sửa phương thức updateItemQuantity để sử dụng đúng kiểu dữ liệu
    public boolean updateItemQuantity(String productId, cartItem.variant variant, int newQuantity) {
        for (cartItem item : cartItems) {
            if (item.getProductId().equals(productId) &&
                    item.getVariant().getColor().equals(variant.getColor()) &&
                    item.getVariant().getSize().equals(variant.getSize())) {
                item.setQuantity(newQuantity);
                return true;
            }
        }
        return false;
    }

    public long getTotalPrice() {
        long total = 0;
        for (cartItem item : cartItems) {
            total += (long) item.getPrice() * item.getQuantity();
        }
        return total;
    }

    public int getTotalItems() {
        int count = 0;
        for (cartItem item : cartItems) {
            count += item.getQuantity();
        }
        return count;
    }

    public boolean isEmpty() {
        return cartItems.isEmpty();
    }

    public void clear() {
        cartItems.clear();
    }

    // Inner class cartItem
    public static class cartItem {
        private String productId;
        private String productName;
        private String image;
        private int quantity;
        private int price;
        private variant variant;

        public cartItem() {
        }

        public cartItem(String productId, String productName, String image, int quantity, int price, variant variant) {
            this.productId = productId;
            this.productName = productName;
            this.image = image;
            this.quantity = quantity;
            this.price = price;
            this.variant = variant;
        }

        public cartItem(String productId, String productName, String image, int quantity, int price) {
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

        public variant getVariant() {
            return variant;
        }

        public void setVariant(variant variant) {
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
}
