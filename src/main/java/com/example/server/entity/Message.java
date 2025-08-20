package com.example.server.entity;

import java.math.BigInteger;
import java.time.LocalDateTime;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "messages")
public class Message {
    @Id
    private BigInteger id;
    @ManyToOne
    @JoinColumn(name = "sender", referencedColumnName = "username")
    @Nonnull
    private User sender;
    @ManyToOne
    @JoinColumn(name = "receiver", referencedColumnName = "username")
    @Nonnull
    private User receiver;
    private String text;
    private String status;
    private LocalDateTime time;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
