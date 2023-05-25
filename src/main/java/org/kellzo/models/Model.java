package org.kellzo.models;

import java.time.LocalDateTime;

public class Model {
    protected int id;
    protected String created;

    public Model() {
    }

    public Model(int id, String created) {
        this.id = id;
        this.created = created;
    }

    public Model(String created) {
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String  getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
