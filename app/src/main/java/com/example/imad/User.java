package com.example.imad;

public class User {
    private String name,mobile;
    private boolean admin;

    public User() {
    }

    public User(String name, String mobile, boolean admin) {
        this.name = name;
        this.mobile = mobile;
        this.admin = admin;
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

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
