package com.mart.nagaon.categories;

import java.util.ArrayList;

public class model {

    private String CatID;
    private String Name;
    private String Image;

    public model(String catID, String name, String image) {
        CatID = catID;
        Name = name;
        Image = image;
    }

    public String getCatID() {
        return CatID;
    }

    public void setCatID(String catID) {
        CatID = catID;
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

    public model() {
    }
}
