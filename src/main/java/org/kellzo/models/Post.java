package org.kellzo.models;

import java.util.List;

public class Post {
    private int id;
    private String post;
    private String created;
    private int users_id;
    private boolean visible;

    private List<Comment> comments;

    public Post(int id, String post, String created, int users_id, boolean visible, List<Comment> comments) {
        this.id = id;
        this.post = post;
        this.created = created;
        this.users_id = users_id;
        this.visible = visible;
        this.comments = comments;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
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
