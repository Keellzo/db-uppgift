package org.kellzo;

public class Post {
    private int id;
    private String post;
    private String created;
    private int users_id;

    public Post(int id, String post, String created, int users_id) {
        this.id = id;
        this.post = post;
        this.created = created;
        this.users_id = users_id;
    }


    public Post() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getUsers_id() {
        return users_id;
    }

    public void setUsers_id(int users_id) {
        this.users_id = users_id;
    }
}
