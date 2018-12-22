package com.example.ken.gravitate.Models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

// Model class to hold all user information
public class User {

    private String uid, display_name, membership, phone_number, photo_url;

    public User() {}

    public User( Map<String,String> params ) {
        this.uid = params.get("uid");
        this.display_name = params.get("display_name");
        this.membership = params.get("membership");
        this.phone_number = params.get("phone_number");
        this.photo_url = params.get("photo_url");
    }

    public User( JSONObject userInfo ) throws JSONException {
        this.uid = userInfo.getString("uid");
        this.display_name = userInfo.getString("display_name");
        this.membership = userInfo.getString("membership");
        this.phone_number = userInfo.getString("phone_number");
        this.photo_url = userInfo.getString("photo_url");
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }
}
