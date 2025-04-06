package com.project.clothingstore.modal;

public class ProductCategories {
    String id, name, type;
    int quantity;

    public ProductCategories(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public ProductCategories() {
    }

    public ProductCategories(String name, String type, int quantity) {
        this.name = name;
        this.type = type;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
