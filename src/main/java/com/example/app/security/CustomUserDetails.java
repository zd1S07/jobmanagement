package com.example.app.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public  class CustomUserDetails implements UserDetails {

    private String email;
    private String password;
    private Long id; // ユーザーIDを追加

    public CustomUserDetails(String email, String password, Long id) {
        this.email = email;
        this.password = password;
        this.id = id; // ユーザーIDを初期化
    }

   

	public Long getId() {
        return id; // ユーザーIDを取得するメソッドを追加
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
}
