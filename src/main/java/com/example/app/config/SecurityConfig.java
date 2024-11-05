package com.example.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.app.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    public SecurityConfig(CustomUserDetailsService userDetailsService) {
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/account/**", "/company/**", "/api/**","/interviewCompany/add").permitAll() // アカウント作成ページとログインページを許可
                .anyRequest().authenticated() // その他のリクエストは認証が必要
            )
            .formLogin(form -> form
                .loginPage("/account/login") // カスタムログインページ
                .defaultSuccessUrl("/home", true) // ログイン成功後にリダイレクトされるURL
                .permitAll()
            )
            .logout(logout -> logout
                    .logoutUrl("/logout") // ログアウトURLを指定
                    .logoutSuccessUrl("/account/login?logout") // ログアウト後のリダイレクト先
                    .invalidateHttpSession(true) // セッションを無効にする
                    .deleteCookies("JSESSIONID") // クッキーの削除
                    .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
