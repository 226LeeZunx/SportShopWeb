package com.mycompany.sportshopweb.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

@WebServlet(name = "RAGChatServlet", urlPatterns = {"/rag-chat"})
public class RAGChatServlet extends HttpServlet {

    private static final String DEFAULT_API_URL =
            "https://chatbot-uk2z.onrender.com/chat";

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15))
            .build();

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/rag-chat.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");

        String userQuery = request.getParameter("query");

        if (userQuery == null || userQuery.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Vui lòng nhập câu hỏi hợp lệ.");
            return;
        }

        try {
            String answer = callChatbotAPI(userQuery.trim());

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(answer);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            getServletContext().log(
                    "Yêu cầu gọi RAG API bị gián đoạn.", e);

            response.setStatus(
                    HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.getWriter().write(
                    "Yêu cầu đến hệ thống RAG AI bị gián đoạn. Vui lòng thử lại.");

        } catch (Exception e) {
            getServletContext().log(
                    "Lỗi kết nối tới hệ thống RAG AI.", e);

            response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
            response.getWriter().write(
                    "Lỗi kết nối tới hệ thống RAG AI. Vui lòng thử lại sau.");
        }
    }

    private String callChatbotAPI(String userQuery)
            throws IOException, InterruptedException {

        String apiUrl = System.getenv("CHATBOT_API_URL");
        String apiKey = System.getenv("CHATBOT_API_KEY");

        if (apiUrl == null || apiUrl.isBlank()) {
            apiUrl = DEFAULT_API_URL;
        }

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "Chưa cấu hình CHATBOT_API_KEY trên Render.");
        }

        String requestBody = OBJECT_MAPPER.writeValueAsString(
                Map.of("question", userQuery));

        HttpRequest apiRequest = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .timeout(Duration.ofSeconds(120))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("x-api-key", apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(
                        requestBody, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> apiResponse = HTTP_CLIENT.send(
                apiRequest,
                HttpResponse.BodyHandlers.ofString(
                        StandardCharsets.UTF_8));

        if (apiResponse.statusCode() < 200
                || apiResponse.statusCode() >= 300) {
            throw new IOException(
                    "Chatbot API trả về HTTP "
                            + apiResponse.statusCode());
        }

        String body = apiResponse.body();

        if (body == null || body.isBlank()) {
            throw new IOException("Chatbot API trả về dữ liệu rỗng.");
        }

        String trimmedBody = body.trim();

        // Nếu API trả về văn bản thuần, sử dụng trực tiếp.
        if (!trimmedBody.startsWith("{")) {
            return trimmedBody;
        }

        // Nếu API trả về JSON, lấy trường chứa câu trả lời.
        JsonNode root = OBJECT_MAPPER.readTree(trimmedBody);

        String[] answerFields = {
            "answer", "response", "result", "message", "output"
        };

        for (String field : answerFields) {
            JsonNode value = root.get(field);

            if (value != null && value.isValueNode()
                    && !value.asText().isBlank()) {
                return value.asText();
            }
        }

        throw new IOException(
                "Không tìm thấy trường câu trả lời trong JSON của chatbot.");
    }
}