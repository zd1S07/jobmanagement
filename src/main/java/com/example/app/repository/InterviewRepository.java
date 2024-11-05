package com.example.app.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.app.model.Interview;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findByCompanyId(Long companyId);

    // と日付から間近の面接を取得するメソッド
    @Query("SELECT i FROM Interview i WHERE i.company.id = :companyId AND i.interviewDate BETWEEN CURRENT_DATE AND :endDate")
    List<Interview> findUpcomingInterviews(@Param("companyId") Long companyId, @Param("endDate") LocalDate endDate);
}
