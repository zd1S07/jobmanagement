package com.example.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.app.service.OpenAIService;

@Controller
@RequestMapping("/motivation") // motivationページのベースパス
public class MotivationController {

    private final OpenAIService openAIService;

    public MotivationController(OpenAIService openAIService) {
        this.openAIService = openAIService; // OpenAIServiceをインジェクション
    }

    @GetMapping // このメソッドが呼ばれると、motivation.htmlが表示される
    public String ToMotivation() {
        return "motivation"; // motivation.htmlに遷移
    }

    @GetMapping("/generate") // URLマッピングを修正
    public ResponseEntity<String> generateMotivation(
            @RequestParam String companyWebsite,
            @RequestParam String jobTitle,
            @RequestParam String experienceStatus) { // 経験状況を受け取るためのパラメータを追加

        // OpenAIServiceを呼び出し、志望動機を生成する
        String motivation = openAIService.generateMotivation(companyWebsite, jobTitle, experienceStatus); // experienceStatusも渡す

        return ResponseEntity.ok(motivation); // 生成された志望動機を返す
    }
}
