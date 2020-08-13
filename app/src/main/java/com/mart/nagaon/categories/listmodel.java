package com.mart.nagaon.categories;

public class listmodel {
    private String Name;
    private String SubID;

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

    public listmodel(String name, String subID) {
        Name = name;
        SubID = subID;
    }

    public listmodel() {
    }
}
