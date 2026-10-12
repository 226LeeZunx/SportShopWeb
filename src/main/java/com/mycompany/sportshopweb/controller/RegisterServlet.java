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

        // 6. Băm mật khẩu sang chuỗi bảo mật SHA-256
        String hashedPassword = PasswordUtil.hashPassword(password);

        // 7. Tạo và lưu tài khoản User
        User user = new User(phone, email, hashedPassword);
        boolean isUserSaved = userDAO.insert(user);

        if (!isUserSaved) {
            request.setAttribute("error", "System error while creating account. Please try again.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        // 8. Tạo và lưu thông tin Khách hàng (Customer) với hạng mặc định
        CustomerDAO customerDAO = new CustomerDAO();
        MembershipTier defaultTier = customerDAO.getDefaultMembershipTier();

        Customer customer = new Customer();
        customer.setUser(user);
        customer.setMembershipTier(defaultTier);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setGender(gender != null ? gender : "Other");

        customerDAO.insert(customer);
        Cart cart = new Cart();
        cart.setCustomer(customer);

        CartDAO cartDAO = new CartDAO();
        cartDAO.insert(cart);

        // 9. Đăng ký thành công -> Chuyển về trang đăng nhập với thông báo thành công
        request.setAttribute("message", "Registration successful! Please sign in with your credentials.");
        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }
}
