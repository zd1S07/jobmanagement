package com.example.app.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate interviewDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private String jobTitle;
    private String selectionStatus;
    private String motivation;

    // Companyとの多対1の関係
    @ManyToOne
    @JoinColumn(name = "company_id") // CompanyのIDを参照
    private Company company;

	public Long getId() {
		return id;
	}

	public LocalDate getInterviewDate() {
		return interviewDate;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public String getLocation() {
		return location;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public String getSelectionStatus() {
		return selectionStatus;
	}

	public String getMotivation() {
		return motivation;
	}

	public Company getCompany() {
		return company;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setInterviewDate(LocalDate interviewDate) {
		this.interviewDate = interviewDate;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public void setSelectionStatus(String selectionStatus) {
		this.selectionStatus = selectionStatus;
	}

	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	@Override
	public String toString() {
	    return "Interview{" +
	            "id=" + id +
	            ", interviewDate=" + interviewDate +
	            ", startTime=" + startTime +
	            ", endTime=" + endTime +
	            ", location='" + location + '\'' +
	            ", jobTitle='" + jobTitle + '\'' +
	            ", selectionStatus='" + selectionStatus + '\'' +
	            ", motivation='" + motivation + '\'' +
	            '}';
	}


}