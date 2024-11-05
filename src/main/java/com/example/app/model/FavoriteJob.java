package com.example.app.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "favorite_jobs")
public class FavoriteJob {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String jobLink;

    private LocalDate deadline;

    // コンストラクタ、ゲッター、セッター
    public FavoriteJob() {}

    public FavoriteJob(Long userId, String companyName, String jobLink, LocalDate deadline) {
        this.userId = userId;
        this.companyName = companyName;
        this.jobLink = jobLink;
        this.deadline = deadline;
    }

	public Long getId() {
		return id;
	}

	public Long getUserId() {
		return userId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getJobLink() {
		return jobLink;
	}

	public LocalDate getDeadline() {
		return deadline;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public void setJobLink(String jobLink) {
		this.jobLink = jobLink;
	}

	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}
	
	
}
