package com.mart.nagaon;

import java.util.List;

public class request {
    private String phone;
    private String name;
    private String address;
    private String total;
    private List<OrderModel> foods;

    public request() {
    }

    public request(String phone, String name, String address, String total, List<OrderModel> foods) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.foods = foods;
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
