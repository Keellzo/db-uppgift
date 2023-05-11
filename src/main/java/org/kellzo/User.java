package org.kellzo;

public class User {
    private int users_id;
    private String name;
    private String email;
    private String phone;
    private String adress;
    private String created;
    private boolean online;

    public User(int users_id, String name, String email, String phone, String adress, String created, boolean online) {
        this.users_id = users_id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.adress = adress;
        this.created = created;
        this.online = online;
    }

    public User() {
    }

    public int getUsers_id() {
        return users_id;
    }

    public void setUsers_id(int users_id) {
        this.users_id = users_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
