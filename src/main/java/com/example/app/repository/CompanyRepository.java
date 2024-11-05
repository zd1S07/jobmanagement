package com.example.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.app.model.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findByUserId(Long userId);
    boolean existsByName(String companyName);
   
}

