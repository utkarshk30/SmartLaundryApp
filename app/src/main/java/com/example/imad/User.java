package com.example.imad;

public class User {
    private String name,mobile;

    public User(String name, String mobile) {
        this.name = name;
        this.mobile = mobile;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
