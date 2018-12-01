package com.example.ken.gravitate;

public class Rider {
    private int profile_photo;
    private String fullname;
    private String email;

    public Rider(){ }

    public Rider(int profile_photo, String fullname, String email) {
        this.profile_photo = profile_photo;
        this.fullname = fullname;
        this.email = email;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getProfile_photo() {
        return this.profile_photo;
    }

    public void setProfile_photo(int profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
