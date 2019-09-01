package com.mylightshare.main.com.mylightshare.main.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="confirmation_token")
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="token_id")
    private long tokenId;

    @Column(name="confirmation_token")
    private String confirmationToken;

    @Column(name="created_date")
    private LocalDateTime createdDate;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(nullable = false, name = "username")
    private User user;

    public ConfirmationToken() {
        createdDate = LocalDateTime.now();
        confirmationToken = UUID.randomUUID().toString();
    }

    public ConfirmationToken(User user) {
        this.user = user;
        createdDate = LocalDateTime.now();
        confirmationToken = UUID.randomUUID().toString();
    }

    public long getTokenId() {
        return tokenId;
    }

    public void setTokenId(long tokenId) {
        this.tokenId = tokenId;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
