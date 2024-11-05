package com.example.app.service;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OpenAIService {

    private final String apiUrl = "https://api.openai.com/v1/chat/completions";

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public OpenAIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String generateMotivation(String companyWebsite, String jobTitle, String experienceStatus) { // 引数を修正
        try {
            // URLから会社情報を取得
            String companyContent = extractCompanyContent(companyWebsite);

            // 会社情報が抽出できない場合のエラーハンドリング
            if (companyContent == null || companyContent.isEmpty()) {
                return "会社情報の取得に失敗しました。URLが正しいか確認してください。";
            }

            // 職種に必要なスキルを抽出
            String skills = extractSkills(jobTitle, companyContent);

            // 経験年数とスクールの情報を含めたプロンプト作成
            String experienceInfo = experienceStatus.equals("未経験") 
                ? "経験はありません。" 
                : experienceStatus + "の経験があります。";

            String prompt = String.format(
            		 "以下の会社情報とスキルを基に、%sの志望動機を200文字以上で生成してください:\n%s\n必要なスキル: %s\n%s\n\n生成する志望動機には、なぜこの会社で働きたいのか、どのように貢献できるかを含めてください。",
                jobTitle,
                companyContent,
                skills,
                experienceInfo
            );

            // OpenAI APIリクエストのためのヘッダー作成
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            // リクエストボディ作成
            Map<String, Object> requestBody = Map.of("model", "gpt-3.5-turbo", "messages",
                    List.of(Map.of("role", "user", "content", prompt)));

            // HTTPリクエスト実行
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            String response = restTemplate.postForObject(apiUrl, requestEntity, String.class);

            // JSONレスポンス解析
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> responseMap = mapper.readValue(response, new TypeReference<>() {});

            // choicesから志望動機の抽出
            List<Map<String, Object>> choices = mapper.convertValue(responseMap.get("choices"), new TypeReference<>() {});
            if (choices != null && !choices.isEmpty()) {
                Object messageObj = choices.get(0).get("message");
                if (messageObj instanceof Map) {
                    Map<?, ?> messageMap = (Map<?, ?>) messageObj;
                    Object contentObj = messageMap.get("content");
                    if (contentObj instanceof String) {
                        return (String) contentObj;
                    }
                }
            }
            return "志望動機の生成に失敗しました。";

        } catch (Exception e) {
            e.printStackTrace();
            return "志望動機の生成に失敗しました。";
        }
    }

    // URLから会社情報を抽出するメソッド
    private String extractCompanyContent(String url) {
        try {
            // URLからHTMLコンテンツを取得
            String htmlContent = restTemplate.exchange(url, HttpMethod.GET, null, String.class).getBody();

            // HTMLから会社情報のセクションを抽出（例として、事業内容、ミッション、価値観に関連するテキストを抽出）
            Pattern pattern = Pattern.compile("(?i)(ミッション|経営理念|理念|企業理念|価値観|技術|事業内容)[^<]{10,500}");
            Matcher matcher = pattern.matcher(htmlContent);

            StringBuilder extractedContent = new StringBuilder();
            while (matcher.find()) {
                extractedContent.append(matcher.group()).append("\n");
            }

            return extractedContent.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 職種に必要なスキルを抽出するメソッド
    private String extractSkills(String jobTitle, String companyContent) {
        try {
            // スキル抽出のためのプロンプトを作成
            String skillsPrompt = "以下の会社情報を基に、" + jobTitle + "に必要なスキルをリストアップしてください:\n" + companyContent;

            // OpenAI APIリクエストのためのヘッダー作成
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            // リクエストボディ作成
            Map<String, Object> requestBody = Map.of("model", "gpt-3.5-turbo", "messages",
                    List.of(Map.of("role", "user", "content", skillsPrompt)));

            // HTTPリクエスト実行
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            String response = restTemplate.postForObject(apiUrl, request, String.class);

            // JSONレスポンス解析
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> responseMap = mapper.readValue(response, new TypeReference<>() {});

            // choicesからスキルの抽出
            List<Map<String, Object>> choices = mapper.convertValue(responseMap.get("choices"), new TypeReference<>() {});
            if (choices != null && !choices.isEmpty()) {
                Object messageObj = choices.get(0).get("message");
                if (messageObj instanceof Map) {
                    Map<?, ?> messageMap = (Map<?, ?>) messageObj;
                    Object contentObj = messageMap.get("content");
                    if (contentObj instanceof String) {
                        return (String) contentObj; // スキルを返す
                    }
                }
            }
            return "スキルの抽出に失敗しました。";

        } catch (Exception e) {
            e.printStackTrace();
            return "スキルの抽出に失敗しました。";
        }
    }
}
