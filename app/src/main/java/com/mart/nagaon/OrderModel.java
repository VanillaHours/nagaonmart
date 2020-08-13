package com.mart.nagaon;

public class OrderModel {

    private String ProdID;
    private String ProdName;
    private String Price;
    private String Quantity;
    private String Count;
    private String Image;

    public String getProdID() {
        return ProdID;
    }

    public void setProdID(String prodID) {
        ProdID = prodID;
    }

    public String getProdName() {
        return ProdName;
    }

    public void setProdName(String prodName) {
        ProdName = prodName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public OrderModel(String prodID, String prodName, String price, String quantity, String count, String image) {
        ProdID = prodID;
        ProdName = prodName;
        Price = price;
        Quantity = quantity;
        Count = count;
        Image = image;
    }

    public OrderModel() {
    }
}
