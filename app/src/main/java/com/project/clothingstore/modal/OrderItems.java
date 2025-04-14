//package com.project.clothingstore.modal;
//
//import java.io.Serializable;
//
//public class OrderItems implements Serializable { // 👈 thêm implements Serializable
//
//    String productId;
//    String productName;
//    String image;
//    int quantity;
//    int price;
//    Variant variant;
//
//    public static class Variant implements Serializable { // 👈 thêm implements Serializable cho class lồng bên trong
//        String color;
//        String size;
//
//        public Variant() {
//        }
//
//        public Variant(String color, String size) {
//            this.color = color;
//            this.size = size;
//        }
//
//        public String getColor() {
//            return color;
//        }
//        public void setColor(String color) {
//            this.color = color;
//        }
//
//        public String getSize() {
//            return size;
//        }
//        public void setSize(String size) {
//            this.size = size;
//        }
//    }
//
//    // Getters và setters
//    public String getProductId() {
//        return productId;
//    }
//    public void setProductId(String productId) {
//        this.productId = productId;
//    }
//
//    public String getProductName() {
//        return productName;
//    }
//    public void setProductName(String productName) {
//        this.productName = productName;
//    }
//
//    public String getImage() {
//        return image;
//    }
//    public void setImage(String productImage) {
//        this.image = productImage;
//    }
//
//    public int getQuantity() {
//        return quantity;
//    }
//    public void setQuantity(int quantity) {
//        this.quantity = quantity;
//    }
//
//    public int getPrice() {
//        return price;
//    }
//    public void setPrice(int price) {
//        this.price = price;
//    }
//
//    public Variant getVariant() {
//        return variant;
//    }
//    public void setVariant(Variant variant) {
//        this.variant = variant;
//    }
//
//    public String getColor() {
//        return variant.color;
//    }
//    public void setColor(String color) {
//        this.variant.color = color;
//    }
//
//    public String getSize() {
//        return variant.size;
//    }
//    public void setSize(String size) {
//        this.variant.size = size;
//    }
//
//    public OrderItems(String productId, String productName, String productImage, int quantity, int price, Variant variant) {
//        this.productId = productId;
//        this.productName = productName;
//        this.image = productImage;
//        this.quantity = quantity;
//        this.price = price;
//        this.variant = variant;
//    }
//
//    public OrderItems() {
//    }
//}

package com.project.clothingstore.modal;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class OrderItems implements Parcelable {

    String productId;
    String productName;
    String image;
    int quantity;
    int price;
    Variant variant;

    public static class Variant implements Parcelable { // 👈 thêm implements Parcelable cho class lồng bên trong

        String color;
        String size;

        public Variant() {}

        public Variant(String color, String size) {
            this.color = color;
            this.size = size;
        }

        // Getters và setters
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

        // Parcelable methods
        protected Variant(Parcel in) {
            color = in.readString();
            size = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(color);
            dest.writeString(size);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Variant> CREATOR = new Creator<Variant>() {
            @Override
            public Variant createFromParcel(Parcel in) {
                return new Variant(in);
            }

            @Override
            public Variant[] newArray(int size) {
                return new Variant[size];
            }
        };
    }

    // Getters và setters cho OrderItems
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

    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
    }

    // Constructor mặc định
    public OrderItems() {}

    // Constructor với các tham số
    public OrderItems(String productId, String productName, String image, int quantity, int price, Variant variant) {
        this.productId = productId;
        this.productName = productName;
        this.image = image;
        this.quantity = quantity;
        this.price = price;
        this.variant = variant;
    }

    // Parcelable methods
    protected OrderItems(Parcel in) {
        productId = in.readString();
        productName = in.readString();
        image = in.readString();
        quantity = in.readInt();
        price = in.readInt();
        variant = in.readParcelable(Variant.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productId);
        dest.writeString(productName);
        dest.writeString(image);
        dest.writeInt(quantity);
        dest.writeInt(price);
        dest.writeParcelable(variant, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderItems> CREATOR = new Creator<OrderItems>() {
        @Override
        public OrderItems createFromParcel(Parcel in) {
            return new OrderItems(in);
        }

        @Override
        public OrderItems[] newArray(int size) {
            return new OrderItems[size];
        }
    };
}

