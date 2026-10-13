/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.CustomerDAO;
import com.mycompany.sportshopweb.dao.UserDAO;
import com.mycompany.sportshopweb.model.AccountStatus;
import com.mycompany.sportshopweb.model.Customer;
import com.mycompany.sportshopweb.model.User;
import com.mycompany.sportshopweb.util.PasswordUtil;
import com.mycompany.sportshopweb.dao.AdminDAO;
import com.mycompany.sportshopweb.model.Admin;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Anh Tuan
 */
@WebServlet(name = "LoginServlet", urlPatterns = { "/LoginServlet" })
public class LoginServlet extends HttpServlet {

    // Khi người dùng bấm vào link hoặc truy cập URL trang đăng nhập
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }

    // Khi người dùng điền email/sđt + mật khẩu và bấm "Sign In"
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Thiết lập mã hóa tiếng Việt
        request.setCharacterEncoding("UTF-8");

        // 1. Lấy dữ liệu từ form login.jsp
        String identifier = request.getParameter("identifier");
        String password = request.getParameter("password");

        if (identifier != null) {
            identifier = identifier.trim();
        }

        // Giữ lại tên đăng nhập để người dùng không phải gõ lại nếu sai mật khẩu
        request.setAttribute("identifier", identifier);

        // 2. Kiểm tra không được bỏ trống
        if (identifier == null || identifier.isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("error", "Please enter your email/phone and password.");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }

        // 3. Tìm tài khoản trong database theo email hoặc số điện thoại
        UserDAO userDAO = new UserDAO();
        User user = userDAO.findByIdentifier(identifier);

        // 4. Nếu không tìm thấy người dùng
        if (user == null) {
            request.setAttribute("error", "Invalid email/phone or password.");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }

        // 5. Kiểm tra tài khoản có bị khóa không
        if (user.getStatus() != null && user.getStatus() != AccountStatus.ACTIVE) {
            request.setAttribute("error", "Your account has been locked or deactivated. Please contact support.");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }

        // 6. Kiểm tra tính trùng khớp của mật khẩu
        boolean isPasswordCorrect = PasswordUtil.verifyPassword(password, user.getPasswordHash());
        if (!isPasswordCorrect) {
            request.setAttribute("error", "Invalid email/phone or password.");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }
        // 7. Mật khẩu đúng -> Kiểm tra tài khoản có phải Admin không
        AdminDAO adminDAO = new AdminDAO();
        Admin admin = adminDAO.findByUserId(user.getUserId());

        // 8. Lưu thông tin đăng nhập vào Session
        HttpSession session = request.getSession();
        session.setAttribute("user", user);

        // 9. Nếu là Admin -> Chuyển đến Admin Dashboard
        if (admin != null) {
            session.setAttribute("admin", admin);

            response.sendRedirect(
                    request.getContextPath() + "/admin/dashboard");
            return;
        }

        // 10. Nếu không phải Admin -> Xử lý như User thông thường
        CustomerDAO customerDAO = new CustomerDAO();
        Customer customer = customerDAO.findByUserId(user.getUserId());

        session.setAttribute("customer", customer);

        response.sendRedirect(
                request.getContextPath() + "/HomeServlet");

    }
}
