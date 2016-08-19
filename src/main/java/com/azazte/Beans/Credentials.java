package com.azazte.Beans;

/**
 * Created by home on 30/06/16.
 */
public class Credentials {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void validate() {
        if (this.username == null || this.password == null) {
            throw new RuntimeException("username or password cannot be empty");
        }
    }
}
