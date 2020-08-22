package com.mart.nagaon.categories;

public class listmodel {
    private String Name;
    private String SubID;
    private String Image;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSubID() {
        return SubID;
    }

    public void setSubID(String subID) {
        SubID = subID;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public listmodel(String name, String subID, String image) {
        Name = name;
        SubID = subID;
        Image = image;
    }

    public listmodel() {
    }
}
