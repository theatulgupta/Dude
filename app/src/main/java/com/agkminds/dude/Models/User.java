package com.agkminds.dude.Models;

public class User {
    private String uId, name, profileImage;

    public User(){
//        Empty Constructor
    }

    public User(String uId, String name, String profileImage) {
        this.uId = uId;
        this.name = name;
        this.profileImage = profileImage;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
