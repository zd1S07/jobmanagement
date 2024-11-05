package com.example.app.model;

public class InterviewCompanyRequest {
    private String companyName;
    private Long userId;

    // コンストラクタ
    public InterviewCompanyRequest() {
    }

    public InterviewCompanyRequest(String companyName) {
        this.companyName = companyName;
    }

    // GetterとSetter
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
