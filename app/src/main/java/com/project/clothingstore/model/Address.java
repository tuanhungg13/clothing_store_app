package com.project.clothingstore.model;

public class Address {
    private String province;
    private String district;
    private String ward;
    private String street;

    public Address() {}

    public Address(String province, String district, String ward, String street) {
        this.province = province;
        this.district = district;
        this.ward = ward;
        this.street = street;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}

