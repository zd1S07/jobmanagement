package com.example.app.model;

public class MotivationRequest {
    private String companyWebsite;
    private String jobTitle;
    private int yearsOfExperience; // 経験年数
    private boolean attendedSchool; // スクールに通ったかどうか

    // Getters and Setters
    public String getCompanyWebsite() {
        return companyWebsite;
    }

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public boolean isAttendedSchool() {
        return attendedSchool;
    }

    public void setAttendedSchool(boolean attendedSchool) {
        this.attendedSchool = attendedSchool;
    }
}
