package com.mart.nagaon;

import java.util.List;

public class request {
    private String ID;
    private String phone;
    private String name;
    private String address;
    private String total;
    private String paymentMethod;
    private String Status;
    private String OrderDate;
    private List<OrderModel> foods;

    public request(String ID, String phone, String name, String address, String total, String paymentMethod, String status, String orderDate, List<OrderModel> foods) {
        this.ID = ID;
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.paymentMethod = paymentMethod;
        Status = status;
        OrderDate = orderDate;
        this.foods = foods;
    }

    public request() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
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
