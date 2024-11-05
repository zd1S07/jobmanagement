package com.example.app.model;

import java.util.ArrayList;
import java.util.List; // 追加

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany; // 追加

@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String contactPerson;
    private String email;
    private String phone;
    private String website;

    // ユーザーとの多対1の関係
    @ManyToOne
    @JoinColumn(name = "account_id")
    private User user;

 // 面接情報との一対多の関係
    @OneToMany(mappedBy = "company", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Interview> interviews = new ArrayList<>();



    // ゲッターとセッター
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<Interview> getInterviews() { return interviews; } // 追加
    public void setInterviews(List<Interview> interviews) { this.interviews = interviews; } // 追加
    
    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", website='" + website + '\'' +
                ", user=" + (user != null ? user.getId() : "null") + // ユーザーIDを表示
                ", interviews=" + (interviews != null ? interviews.size() : 0) + // 面接情報の数を表示
                '}';
    }

}
