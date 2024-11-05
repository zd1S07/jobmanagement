package com.example.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.app.model.FavoriteJob;

public interface FavoriteJobRepository extends JpaRepository<FavoriteJob, Long> {
    List<FavoriteJob> findByUserId(Long userId);
}
