package com.bc.archive_users.dialog.message;

import com.bc.archive_users.dialog.Dialog;
import com.bc.archive_users.user.User;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "dialog_id", referencedColumnName = "id")
    private Dialog dialog;
    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User user;
    private String content;
    private Instant timestamp;

    @Enumerated(EnumType.STRING)
    private EnumMessageType type;
    @Enumerated(EnumType.STRING)
    private EnumMessageStatus status;

    public Message() {}
    public Message(Dialog dialog, User user, String content, Instant timestamp, EnumMessageType type, EnumMessageStatus status) {
        this.dialog = dialog;
        this.user = user;
        this.content = content;
        this.type = type;
        this.timestamp = timestamp;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public EnumMessageType getType() {
        return type;
    }

    public void setType(EnumMessageType type) {
        this.type = type;
    }

    public EnumMessageStatus getStatus() {
        return status;
    }

    public void setStatus(EnumMessageStatus status) {
        this.status = status;
    }
}
