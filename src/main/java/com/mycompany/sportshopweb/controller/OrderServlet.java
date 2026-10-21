
package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.OrderDAO;
import com.mycompany.sportshopweb.model.Customer;
import com.mycompany.sportshopweb.model.Order;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/orders")
public class OrderServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // 1. Kiểm tra đăng nhập
        if (session == null
                || (session.getAttribute("customer") == null)) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }
        Customer customer = (Customer) session.getAttribute("customer");
        int customerId = customer.getCustomerId();

        try {
            // 2. Lấy toàn bộ lịch sử đơn hàng của khách hàng
            List<Order> allOrders = orderDAO.findByCustomerId(customerId);

            // 3. Đọc tham số bộ lọc trạng thái (?status=ALL, CONFIRMED, COMPLETED,
            // CANCELLED)
            String status = request.getParameter("status");
            if (status == null || status.trim().isEmpty()) {
                status = "ALL";
            } else {
                status = status.trim().toUpperCase();
            }

            List<Order> displayOrders;
            if ("ALL".equals(status)) {
                displayOrders = allOrders;
            } else {
                displayOrders = new ArrayList<>();
                for (Order o : allOrders) {
                    if (o.getOrderStatus() != null && o.getOrderStatus().name().equalsIgnoreCase(status)) {
                        displayOrders.add(o);
                    }
                }
            }

            request.setAttribute("orders", displayOrders);
            request.setAttribute("allOrdersCount", allOrders.size());
            request.setAttribute("selectedStatus", status);

            // 4. Chuyển thông báo thành công/lỗi từ session sang request
            Object success = session.getAttribute("orderSuccess");
            Object error = session.getAttribute("orderError");

            if (success != null) {
                request.setAttribute("success", success);
                session.removeAttribute("orderSuccess");
            }

            if (error != null) {
                request.setAttribute("error", error);
                session.removeAttribute("orderError");
            }

            // 5. Hiển thị orders.jsp
            request.getRequestDispatcher("/views/orders.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            throw new ServletException(
                    "Lỗi khi tải lịch sử đơn hàng.", e);
        }
    }

}
