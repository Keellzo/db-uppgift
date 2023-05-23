package org.kellzo.models;

public class Comment {
    private int id;
    private String comment;
    private String created;
    private int postId;
    private int userId;

    private int visible;


    public Comment(int id, String comment, String created, int postId, int userId, int visible) {
        this.id = id;
        this.comment = comment;
        this.created = created;
        this.postId = postId;
        this.userId = userId;
    }

    public Comment() {
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
