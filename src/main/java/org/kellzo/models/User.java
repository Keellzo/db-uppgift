package org.kellzo.models;

public class User extends Model {
    private String username;
    private String password;
    private String socialSecurityNumber;

    public User(int id, String created, String username, String password, String socialSecurityNumber) {
        super(id, created);
        this.username = username;
        this.password = password;
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public User() {
    }


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

    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }
}