
package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.OrderDAO;
import com.mycompany.sportshopweb.model.Customer;
import com.mycompany.sportshopweb.model.Order;
import com.mycompany.sportshopweb.model.OrderStatus;
import com.mycompany.sportshopweb.util.DBUtil;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/confirm-order")
public class ConfirmOrderServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("customer") == null) {
            response.sendRedirect(
                request.getContextPath() + "/LoginServlet"
            );
            return;
        }

        Customer customer =
            (Customer) session.getAttribute("customer");

        String orderIdParam = request.getParameter("orderId");

        try {
            int orderId = Integer.parseInt(orderIdParam);

            // Tìm đơn hàng thuộc về khách đang đăng nhập
            List<Order> orders =
                orderDAO.findByCustomerId(customer.getCustomerId());

            Order selectedOrder = null;

            for (Order order : orders) {
                if (order.getOrderId() == orderId) {
                    selectedOrder = order;
                    break;
                }
            }

            if (selectedOrder == null) {
                response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Order not found"
                );
                return;
            }

            // Chỉ xác nhận đơn đang PENDING
            if (selectedOrder.getOrderStatus() != OrderStatus.PENDING) {
                response.sendRedirect(
                    request.getContextPath() + "/orders"
                );
                return;
            }

            selectedOrder.setOrderStatus(OrderStatus.CONFIRMED);
            orderDAO.updateOrder(selectedOrder);

            session.setAttribute(
                "orderSuccess",
                "Order confirmed successfully. You can now proceed to payment."
            );

response.sendRedirect(request.getContextPath() + "/orders");

        } catch (NumberFormatException e) {
            response.sendRedirect(
                request.getContextPath() + "/orders"
            );
        }
    }
}
