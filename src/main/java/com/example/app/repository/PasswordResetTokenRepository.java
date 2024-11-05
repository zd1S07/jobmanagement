package com.example.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.app.model.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    // トークンで検索するためのメソッド
    PasswordResetToken findByToken(String token);
}
