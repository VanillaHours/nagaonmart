package com.mart.nagaon;

public class userModel {
    String userID, name, phone, email, address;

    public userModel() {
    }

    public userModel(String userID, String name, String phone, String email, String address) {
        this.userID = userID;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
