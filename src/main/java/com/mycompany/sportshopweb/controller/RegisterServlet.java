/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.CartDAO;
import com.mycompany.sportshopweb.dao.CustomerDAO;
import com.mycompany.sportshopweb.dao.UserDAO;
import com.mycompany.sportshopweb.model.Cart;
import com.mycompany.sportshopweb.model.Customer;
import com.mycompany.sportshopweb.model.MembershipTier;
import com.mycompany.sportshopweb.model.User;
import com.mycompany.sportshopweb.util.EmailService;
import com.mycompany.sportshopweb.util.PasswordUtil;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Anh Tuan
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/RegisterServlet"})
public class RegisterServlet extends HttpServlet {

    // Khi người dùng truy cập trực tiếp bằng URL thì chuyển hướng đến trang register.jsp
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/register.jsp").forward(request, response);
    }

    // Khi người dùng bấm nút Submit form đăng ký
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Thiết lập mã hóa tiếng Việt
        request.setCharacterEncoding("UTF-8");

        // 1. Lấy dữ liệu từ form gửi lên
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String gender = request.getParameter("gender");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Loại bỏ khoảng trắng thừa
        if (firstName != null) firstName = firstName.trim();
        if (lastName != null) lastName = lastName.trim();
        if (email != null) email = email.trim();
        if (phone != null) phone = phone.trim();

        // 2. Giữ lại giá trị người dùng đã nhập để hiển thị lại nếu có lỗi
        request.setAttribute("firstName", firstName);
        request.setAttribute("lastName", lastName);
        request.setAttribute("email", email);
        request.setAttribute("phone", phone);
        request.setAttribute("gender", gender);

        // 3. Kiểm tra tính hợp lệ cơ bản của dữ liệu
        if (firstName == null || firstName.isEmpty() ||
            lastName == null || lastName.isEmpty() ||
            email == null || email.isEmpty() ||
            phone == null || phone.isEmpty() ||
            password == null || password.isEmpty()) {
            request.setAttribute("error", "Please fill in all required fields.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        if (password.length() < 6) {
            request.setAttribute("error", "Password must be at least 6 characters long.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Confirm password does not match.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        // Kiểm tra định dạng Email hợp lệ (chống nhập bậy hoặc sai cú pháp email)
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!email.matches(emailRegex)) {
            request.setAttribute("error", "Invalid email format. Please enter a valid email address (e.g. name@gmail.com).");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        // Kiểm tra định dạng Số điện thoại Việt Nam (10 chữ số, bắt đầu bằng số 0)
        String phoneRegex = "^0[0-9]{9}$";
        if (!phone.matches(phoneRegex)) {
            request.setAttribute("error", "Invalid phone number. Must be 10 digits starting with 0 (e.g. 0901234567).");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        UserDAO userDAO = new UserDAO();

        // 4. Kiểm tra xem Email đã có ai đăng ký chưa
        if (userDAO.existsByEmail(email)) {
            request.setAttribute("error", "Email is already registered. Please use another email.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        // 5. Kiểm tra xem Số điện thoại đã có ai đăng ký chưa
        if (userDAO.existsByPhone(phone)) {
            request.setAttribute("error", "Phone number is already registered. Please use another phone number.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        // 6. Tạo mã OTP ngẫu nhiên 6 chữ số (Hiệu lực 5 phút)
        String otp = String.format("%06d", new java.util.Random().nextInt(1000000));

        // 7. Lưu tạm thông tin đăng ký vào Session để chờ xác thực OTP
        jakarta.servlet.http.HttpSession session = request.getSession();
        session.setAttribute("reg_firstName", firstName);
        session.setAttribute("reg_lastName", lastName);
        session.setAttribute("reg_email", email);
        session.setAttribute("reg_phone", phone);
        session.setAttribute("reg_gender", (gender != null && !gender.isEmpty()) ? gender : "Other");
        session.setAttribute("reg_password", password);
        session.setAttribute("reg_otp", otp);
        session.setAttribute("reg_otp_expiry", System.currentTimeMillis() + (5 * 60 * 1000)); // 5 phút

        // 8. Gửi mã OTP xác nhận đến Gmail của khách hàng
        String fullName = firstName + " " + lastName;
        EmailService.sendOtpEmail(email, fullName, otp);

        // 9. Chuyển hướng sang trang xác nhận mã OTP
        response.sendRedirect(request.getContextPath() + "/VerifyOtpServlet");
    }
}
