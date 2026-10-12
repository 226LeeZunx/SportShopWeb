/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.CustomerDAO;
import com.mycompany.sportshopweb.dao.UserDAO;
import com.mycompany.sportshopweb.model.Customer;
import com.mycompany.sportshopweb.model.User;

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
@WebServlet(name = "ProfileServlet", urlPatterns = {"/ProfileServlet"})
public class ProfileServlet extends HttpServlet {

    // Khi người dùng vào trang xem hồ sơ cá nhân
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // Nếu chưa đăng nhập thì chuyển hướng sang trang đăng nhập
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }

        // Đã đăng nhập -> hiển thị trang profile.jsp
        request.getRequestDispatcher("/views/profile.jsp").forward(request, response);
    }

    // Khi người dùng bấm nút "Save Changes" để cập nhật thông tin cá nhân
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        // Kiểm tra phiên đăng nhập
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }

        User user = (User) session.getAttribute("user");
        Customer customer = (Customer) session.getAttribute("customer");

        // 1. Lấy dữ liệu người dùng vừa chỉnh sửa từ form
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phone = request.getParameter("phone");
        String gender = request.getParameter("gender");

        if (firstName != null) firstName = firstName.trim();
        if (lastName != null) lastName = lastName.trim();
        if (phone != null) phone = phone.trim();

        // 2. Kiểm tra không được để trống họ và tên
        if (firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty()) {
            request.setAttribute("error", "First name and last name cannot be empty.");
            request.getRequestDispatcher("/views/profile.jsp").forward(request, response);
            return;
        }

        UserDAO userDAO = new UserDAO();
        CustomerDAO customerDAO = new CustomerDAO();

        // 3. Nếu người dùng đổi số điện thoại mới, kiểm tra xem số mới có bị ai dùng chưa
        if (phone != null && !phone.isEmpty() && !phone.equals(user.getPhone())) {
            if (userDAO.existsByPhone(phone)) {
                request.setAttribute("error", "Phone number is already used by another account.");
                request.getRequestDispatcher("/views/profile.jsp").forward(request, response);
                return;
            }
            user.setPhone(phone);
            userDAO.update(user);
        }

        // 4. Cập nhật thông tin đối tượng Customer
        if (customer != null) {
            customer.setFirstName(firstName);
            customer.setLastName(lastName);
            if (gender != null && !gender.isEmpty()) {
                customer.setGender(gender);
            }
            customerDAO.update(customer);
        }

        // 5. Cập nhật lại đối tượng mới nhất vào Session
        session.setAttribute("user", user);
        session.setAttribute("customer", customer);

        // 6. Gửi thông báo thành công về giao diện profile.jsp
        request.setAttribute("message", "Profile updated successfully!");
        request.getRequestDispatcher("/views/profile.jsp").forward(request, response);
    }
}
