package com.example.ken.gravitate.Utils;

//class for each individual Card
public class Card {
    int background;
    String destName;
    int profilePhoto;
    String destTime;

    //default
    public Card() {}

    //

    //constructor
    public Card(int background, String destName, int profilePhoto, String destTime) {
        this.background = background;
        this.destName = destName;
        this.destTime = destTime;
        this.profilePhoto = profilePhoto;
    }

    //getters
    public int getBackground() {
        return background;
    }

    public String getDestName() {
        return destName;
    }

    public int getProfilePhoto() {
        return profilePhoto;
    }

    public String getDestTime() {
        return destTime;
    }

    //setters
    public void setBackground(int background) {
        this.background = background;
    }

    public void setDestName(String destName) {
        this.destName = destName;
    }

    public void setProfilePhoto(int profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public void setDestTime(String destTime) {
        this.destTime = destTime;
    }
}
