package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.OrderDAO;
import com.mycompany.sportshopweb.dao.PaymentDAO;
import com.mycompany.sportshopweb.model.Customer;
import com.mycompany.sportshopweb.model.Order;
import com.mycompany.sportshopweb.model.Payment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/order-detail")
public class OrderDetailServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Kiem tra dang nhap
        if (session == null
                || session.getAttribute("customer") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/LoginServlet");
            return;
        }

        Customer customer = (Customer) session.getAttribute("customer");

        String orderIdParam = request.getParameter("orderId");

        if (orderIdParam == null || orderIdParam.isBlank()) {
            response.sendRedirect(
                    request.getContextPath() + "/orders");
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdParam);
            PaymentDAO paymentDAO = new PaymentDAO();

List<Payment> payments = paymentDAO.findByOrderId(orderId);

System.out.println("===== PAYMENT DEBUG =====");
System.out.println("Requested orderId: " + orderId);
System.out.println("Payment count: " + payments.size());

Payment payment = null;

if (!payments.isEmpty()) {
    payment = payments.get(0);

    System.out.println("Payment ID: " + payment.getPaymentId());
    System.out.println("Payment orderId: " + payment.getOrderId());
    System.out.println("Payment method: " + payment.getPaymentMethod());
} else {
    System.out.println("No Payment found for orderId = " + orderId);
}

request.setAttribute("payment", payment);
System.out.println("=========================");

            // Dung ham DAO co san
            List<Order> orders =
                    orderDAO.findByCustomerId(
                            customer.getCustomerId());

            Order selectedOrder = null;

            // Tim don hang theo ID
            for (Order order : orders) {
                if (order.getOrderId() == orderId) {
                    selectedOrder = order;
                    break;
                }
            }

            // Khong tim thay don hang cua khach nay
            if (selectedOrder == null) {
                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Order not found");
                return;
            }
            System.out.println("===== ORDER DETAIL DEBUG =====");
System.out.println("Order ID: " + selectedOrder.getOrderId());
System.out.println("Order items null: " + (selectedOrder.getOrderItems() == null));

if (selectedOrder.getOrderItems() != null) {
    System.out.println("Order items size: "
            + selectedOrder.getOrderItems().size());

    for (var item : selectedOrder.getOrderItems()) {
        System.out.println("Quantity: " + item.getQuantity());
    
    }
}

System.out.println("==============================");
            request.setAttribute("order", selectedOrder);

            request.getRequestDispatcher(
                    "/views/order-detail.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(
                    request.getContextPath() + "/orders");
        }
    }
}
