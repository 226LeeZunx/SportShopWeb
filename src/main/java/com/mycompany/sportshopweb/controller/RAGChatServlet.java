package com.mycompany.sportshopweb.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "RAGChatServlet", urlPatterns = {"/rag-chat"})
public class RAGChatServlet extends HttpServlet {

    private static final String PYTHON_EXE = "D:\\Project Web\\SportShopWeb\\python-rag\\.venv\\Scripts\\python.exe";
    private static final String RAG_SCRIPT_PATH = "D:\\Project Web\\SportShopWeb\\python-rag\\query_rag.py";
    private static final String WORKING_DIR = "D:\\Project Web\\SportShopWeb\\python-rag";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Cho phép truy cập trực tiếp trang nếu cần
        request.getRequestDispatcher("rag-chat.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Cấu hình mã hóa tiếng Việt UTF-8 cho request & response
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");

        String userQuery = request.getParameter("query");
        StringBuilder aiAnswer = new StringBuilder();

        if (userQuery != null && !userQuery.trim().isEmpty()) {
            try {
                ProcessBuilder pb = new ProcessBuilder(PYTHON_EXE, RAG_SCRIPT_PATH, userQuery);
                pb.directory(new File(WORKING_DIR));
                pb.redirectErrorStream(true);

                Process process = pb.start();

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        aiAnswer.append(line).append("\n");
                    }
                }

                int exitCode = process.waitFor();
                if (exitCode != 0 && aiAnswer.length() == 0) {
                    aiAnswer.append("Lỗi: Tiến trình Python dừng bất thường (Exit Code: ").append(exitCode).append(")");
                }

            } catch (Exception e) {
                e.printStackTrace();
                aiAnswer.append("Lỗi kết nối tới hệ thống RAG AI: ").append(e.getMessage());
            }
        } else {
            aiAnswer.append("Vui lòng nhập câu hỏi hợp lệ.");
        }

        // Trả kết quả trực tiếp về cho JavaScript Fetch API
        PrintWriter out = response.getWriter();
        out.print(aiAnswer.toString().trim());
        out.flush();
    }
}