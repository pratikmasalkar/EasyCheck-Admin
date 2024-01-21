package com.example.easycheck_admin;

public class Admin {
    private String name;
    private String email;

    public Admin() {

    }

    public Admin(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}

