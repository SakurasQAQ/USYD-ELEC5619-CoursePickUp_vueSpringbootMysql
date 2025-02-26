package com.elec5619.course_pro_backend.service.impl;

import com.elec5619.course_pro_backend.service.AIChatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AIChatServiceImpl implements AIChatService {

    // OpenAI API的URL
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    // 从配置文件中注入 OpenAI API 密钥
    @Value("${openai.api.key}")
    private String apiKey;

    @Override
    public String processUserMessage(String prompt) {
        RestTemplate restTemplate = new RestTemplate();


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);


        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", Collections.singletonList(
                Map.of("role", "user", "content", prompt)
        ));
        requestBody.put("max_tokens", 1000);
        requestBody.put("temperature", 0.7);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {

            ResponseEntity<Map> response = restTemplate.postForEntity(API_URL, request, Map.class);

            Map<String, Object> responseBody = response.getBody();

            if (responseBody == null) {
                return "Error: Empty response from AI service.";
            }

            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
            if (choices == null || choices.isEmpty()) {
                return "Error: No choices in AI response.";
            }

            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            if (message == null) {
                return "Error: No message in AI response.";
            }

            String aiResponse = (String) message.get("content");
            if (aiResponse == null) {
                return "Error: No content in AI response.";
            }

            return aiResponse.trim();

        } catch (HttpClientErrorException e) {
            // 处理 HTTP 错误
            String errorResponse = e.getResponseBodyAsString();
            System.err.println("HTTP Error: " + e.getStatusCode() + " - " + errorResponse);
            return "Error processing AI response: " + errorResponse;
        } catch (Exception e) {
            // 处理其他异常
            e.printStackTrace();
            return "Error processing AI response: " + e.getMessage();
        }
    }
}