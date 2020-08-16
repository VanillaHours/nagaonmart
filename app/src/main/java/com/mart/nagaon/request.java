package com.mart.nagaon;

import java.util.List;

public class request {
    private String phone;
    private String name;
    private String address;
    private String total;
    private String paymentMethod;
    private String paymentStatus;
    private List<OrderModel> foods;

    public request() {
    }

    public request(String phone, String name, String address, String total, String paymentMethod, String paymentStatus, List<OrderModel> foods) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.foods = foods;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<OrderModel> getFoods() {
        return foods;
    }

    public void setFoods(List<OrderModel> foods) {
        this.foods = foods;
    }
}
