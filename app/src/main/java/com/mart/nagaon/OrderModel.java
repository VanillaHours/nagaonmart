package com.mart.nagaon;

public class OrderModel {

    private int ID;
    private String ProdID;
    private String ProdName;
    private Integer Price;
    private String Quantity;
    private Integer Count;
    private String Image;

    public OrderModel(String prodID, String prodName, Integer price, String quantity, Integer count, String image) {
        ProdID = prodID;
        ProdName = prodName;
        Price = price;
        Quantity = quantity;
        Count = count;
        Image = image;
    }

    public OrderModel(int ID, String prodID, String prodName, Integer price, String quantity, Integer count, String image) {
        this.ID = ID;
        ProdID = prodID;
        ProdName = prodName;
        Price = price;
        Quantity = quantity;
        Count = count;
        Image = image;
    }

    public OrderModel() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

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

    public Integer getPrice() {
        return Price;
    }

    public void setPrice(Integer price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public Integer getCount() {
        return Count;
    }

    public void setCount(Integer count) {
        Count = count;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
