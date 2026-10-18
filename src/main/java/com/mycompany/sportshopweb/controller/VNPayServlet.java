
package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.CustomerDAO;
import com.mycompany.sportshopweb.dao.OrderDAO;
import com.mycompany.sportshopweb.model.Customer;
import com.mycompany.sportshopweb.model.Order;
import com.mycompany.sportshopweb.util.VNPayService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet("/vnpay-create")
public class VNPayServlet extends HttpServlet {
    OrderDAO orderDAO = new OrderDAO();
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        try {
            // Lấy orderId từ form order-success.jsp
            // Lấy orderId từ form order-success.jsp
            String orderIdText = request.getParameter("orderId");

            if (orderIdText == null || orderIdText.isBlank()) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Missing order ID.");
                return;
            }

            int orderId;

            try {
                orderId = Integer.parseInt(orderIdText);
            } catch (NumberFormatException e) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid order ID.");
                return;
            }
            
            HttpSession session = request.getSession(false);

        // 1. Kiểm tra đăng nhập
        if (session == null
                || (session.getAttribute("customer") == null)) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }
        Customer customer = (Customer) session.getAttribute("customer");
            if (customer == null) {
                response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "Please log in first.");
                return;
            }

            int customerId = customer.getCustomerId();

            // Lấy danh sách đơn hàng của khách hàng
            OrderDAO orderDAO = new OrderDAO();
            List<Order> orders = orderDAO.findByCustomerId(customerId);

            // Tìm đúng đơn hàng theo orderId
            Order order = null;

            for (Order o : orders) {
                if (o.getOrderId() == orderId) {
                    order = o;
                    break;
                }
            }

            if (order == null) {
                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Order not found.");
                return;
            }

            // Lấy số tiền từ database, không lấy từ form
            long amountVnd = order.getTotalAmount();

            if (amountVnd <= 0) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Amount must be positive.");
                return;
            }

            String txnRef = UUID.randomUUID()
                    .toString()
                    .replace("-", "")
                    .substring(0, 20);

            String orderInfo = "Sandbox payment " + txnRef;
            String ipAddress = request.getRemoteAddr();

            System.out.println(
                "VNPAY_TMN_CODE exists: "
                + (System.getenv("VNPAY_TMN_CODE") != null
                && !System.getenv("VNPAY_TMN_CODE").isBlank())
        );
            String paymentUrl = VNPayService.createPaymentUrl(
                    amountVnd,
                    txnRef,
                    orderInfo,
                    ipAddress);

            response.sendRedirect(paymentUrl);

        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid payment amount.");
        } catch (Exception e) {
            throw new ServletException(
                    "Unable to create VNPay payment URL.", e);
        }
    }
}
