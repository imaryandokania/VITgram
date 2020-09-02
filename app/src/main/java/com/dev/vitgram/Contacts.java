package com.dev.vitgram;

public class Contacts {
    public String regno,image,name;
    public Contacts()
    {

    }

    public Contacts(String regno, String name, String image) {
        this.regno = regno;
        this.name = name;
        this.image = image;
    }

    public String getRegno() {
        return regno;
    }

    public void setRegno(String regno) {
        this.regno = regno;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
