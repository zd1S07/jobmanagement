package com.example.app.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private LocalDateTime expirationDate;

    // コンストラクタ
    public PasswordResetToken() {}

    public PasswordResetToken(String token, User user) {
        this.token = token;
        this.user = user;
        // トークンの有効期限は24時間に設定
        this.expirationDate = LocalDateTime.now().plusHours(24);
    }

    // トークンが有効かどうかを確認するメソッド
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationDate);
    }

    // ゲッターとセッター
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }
}
