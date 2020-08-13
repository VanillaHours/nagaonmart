package com.mart.nagaon.product;

public class prodmodel {
    String Name, Image, About, Quantity;
    Integer Discount,Price, MRP;

    public prodmodel(String name, String image, String about, String quantity, Integer discount, Integer price, Integer MRP) {
        Name = name;
        Image = image;
        About = about;
        Quantity = quantity;
        Discount = discount;
        Price = price;
        this.MRP = MRP;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getAbout() {
        return About;
    }

    public void setAbout(String about) {
        About = about;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public Integer getDiscount() {
        return Discount;
    }

    public void setDiscount(Integer discount) {
        Discount = discount;
    }

    public Integer getPrice() {
        return Price;
    }

    public void setPrice(Integer price) {
        Price = price;
    }

    public Integer getMRP() {
        return MRP;
    }

    public void setMRP(Integer MRP) {
        this.MRP = MRP;
    }

    public prodmodel() {
    }


}
