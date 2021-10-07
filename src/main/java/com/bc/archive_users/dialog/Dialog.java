package com.bc.archive_users.dialog;

import com.bc.archive_users.user.User;

import javax.persistence.*;

@Entity
@Table(name = "dialogs")
public class Dialog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private User user;

    public Dialog() {}
    public Dialog(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
