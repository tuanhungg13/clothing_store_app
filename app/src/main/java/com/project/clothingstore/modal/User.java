package com.project.clothingstore.modal;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Map;

public class User {
    private String uid;
    private String email;
    private String phoneNumber;
    private String fullName;
    private String role;
    @ServerTimestamp
    private Timestamp createdAt;
    private Map<String, String> address;
    private String cartId;

    // Constructors
    public User() {}

    public User(String uid, String email, String phoneNumber, String fullName, String role, Timestamp createdAt, Map<String, String> address, String cartId) {
        this.uid = uid;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.role = role;
        this.createdAt = createdAt;
        this.address = address;
        this.cartId = cartId;
    }

    // Getters & Setters
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Map<String, String> getAddress() {
        return address;
    }

    public void setAddress(Map<String, String> address) {
        this.address = address;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }
}