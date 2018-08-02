package com.finalproject.intruderdetection.models;

/**
 * Created by TKPC on 1/16/2018.
 */

public class User {
    public String username;
    public String phoneNumber;
    public String userPhoto;
    public String id;
    public Boolean blacklisted;
    public String email;
    public String address;
    public String avatarId;


    public User() {
    }

    public User(String username, String phoneNumber, String userPhoto) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.userPhoto = userPhoto;
    }

    public User(String username, String phoneNumber, String userPhoto, String id, Boolean blacklisted) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.userPhoto = userPhoto;
        this.id = id;
        this.blacklisted = blacklisted;
    }

    public User(String username, String phoneNumber, String userPhoto, String id, Boolean blacklisted, String email, String address, String avatarId) {
        this.username = username;
        this.avatarId = avatarId;
        this.phoneNumber = phoneNumber;
        this.userPhoto = userPhoto;
        this.id = id;
        this.blacklisted = blacklisted;
        this.email = email;
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getBlacklisted() {
        return blacklisted;
    }

    public void setBlacklisted(Boolean blacklisted) {
        this.blacklisted = blacklisted;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public String getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(String avatarId) {
        this.avatarId = avatarId;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
