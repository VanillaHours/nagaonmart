package com.mart.nagaon.home;

public class categoryModel1 {
    private String Name;
    private String Image;
    private String Desc;

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public categoryModel1() {
    }

    public categoryModel1(String name, String image, String desc) {
        Name = name;
        Image = image;
        Desc = desc;
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
}
