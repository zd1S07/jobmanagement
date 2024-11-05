package com.example.app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    
    @Value("${spring.mail.password}") // プロパティファイルから読み込む
    private String appPassword; // アプリパスワードを格納する変数

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(String to, String token) {
        String fromAddress = "tachan.jobc@gmail.com"; // 送信元のメールアドレス
        String subject = "パスワードリセットのリクエスト";
        String text = "パスワードをリセットするには、以下のリンクをクリックしてください:\n" + 
                      "http://localhost:8080/account/password/reset/confirm?token=" + token;
        
        System.out.println("Using email: " + fromAddress);
        System.out.println("Using app password: " + appPassword);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);  // 送信元
        message.setTo(to);             // 送信先
        message.setSubject(subject);   // 件名
        message.setText(text);         // メッセージ内容

        try {
            mailSender.send(message);   // メール送信を試みる
        } catch (MailSendException e) {
            // エラーログを記録する
            System.err.println("メール送信に失敗しました: " + e.getMessage());
            // 必要に応じて、例外を再スローすることもできます
            // throw e; 
        }
    }
}
