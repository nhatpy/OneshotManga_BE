package com.anime_social.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;

import com.anime_social.services.interfaces.OllamaService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OllamaServiceImpl implements OllamaService {
    @Value("${OLLAMA_HOST}")
    private String ollamaHost;
    @Value("${OLLAMA_PORT}")
    private String ollamaPort;

    @Override
    public List<String> extractGenres(String message) {
        try {
            String prompt = """
                    Hãy đọc đoạn văn sau và trích xuất ra danh sách thể loại (genre) có trong đó dưới dạng mảng JSON.
                    Ví dụ kết quả: ["Trinh thám", "Kinh dị"]
                    Nội dung: "%s"
                    Chỉ trả về mảng JSON, không giải thích gì thêm.
                    """.formatted(message);

            ObjectMapper mapper = new ObjectMapper();
            String escapedPrompt = mapper.writeValueAsString(prompt);

            String jsonBody = """
                    {
                        "model": "mistral",
                        "prompt": %s,
                        "stream": true
                    }
                    """.formatted(escapedPrompt);

            URI uri = new URI("http", null, ollamaHost, Integer.parseInt(ollamaPort), "/api/generate", null, null);
            URL url = uri.toURL();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.getOutputStream().write(jsonBody.getBytes(StandardCharsets.UTF_8));

            int status = conn.getResponseCode();
            if (status != 200) {
                BufferedReader errorReader = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
                StringBuilder error = new StringBuilder();
                String errLine;
                while ((errLine = errorReader.readLine()) != null) {
                    error.append(errLine).append("\n");
                }
                errorReader.close();
                throw new RuntimeException("HTTP Error " + status + ": " + error);
            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));

            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    JsonNode node = mapper.readTree(line);
                    String chunk = node.get("response").asText();
                    responseBuilder.append(chunk);
                }
            }

            reader.close();
            conn.disconnect();

            String fullResponse = responseBuilder.toString().trim();

            if (fullResponse.isEmpty()) {
                return Collections.emptyList();
            }

            return mapper.readValue(fullResponse, new TypeReference<List<String>>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Không thể parse phản hồi NDJSON", e);
        }
    }

}
