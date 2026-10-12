
package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.OrderDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Kiểm tra khách hàng đã đăng nhập chưa
        if (session == null
                || !(session.getAttribute("customerId") instanceof Integer)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Hiển thị trang checkout
        request.getRequestDispatcher("/checkout.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        // 1. Kiểm tra đăng nhập
        if (session == null
                || !(session.getAttribute("customerId") instanceof Integer)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int customerId = (Integer) session.getAttribute("customerId");

        // 2. Lấy thông tin giao hàng
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String note = request.getParameter("note");
        String paymentMethod = request.getParameter("paymentMethod");

        // 3. Kiểm tra thông tin bắt buộc
        if (fullName == null || fullName.isBlank()
                || phone == null || phone.isBlank()
                || address == null || address.isBlank()
                || paymentMethod == null || paymentMethod.isBlank()) {

            request.setAttribute(
                    "error",
                    "Vui lòng nhập đầy đủ thông tin giao hàng.");

            request.getRequestDispatcher("/checkout.jsp")
                    .forward(request, response);
            return;
        }

        // 4. Lấy danh sách sản phẩm khách đã chọn
        String[] selectedValues =
                request.getParameterValues("selectedVariants");

        if (selectedValues == null || selectedValues.length == 0) {
            session.setAttribute(
                    "orderError",
                    "Vui lòng chọn ít nhất một sản phẩm.");

            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        List<Integer> variantIds = new ArrayList<>();

        try {
            for (String value : selectedValues) {
                int variantId = Integer.parseInt(value);

                if (variantId <= 0 || variantIds.contains(variantId)) {
                    throw new IllegalArgumentException();
                }

                variantIds.add(variantId);
            }

            /*
             * TODO: Gọi OrderDAO để tạo đơn hàng.
             * Cần bổ sung phương thức DAO nhận customerId,
             * thông tin giao hàng, paymentMethod và variantIds.
             *
             * Không gọi insertOrder() rồi insertOrderItem() riêng lẻ
             * vì các DAO hiện đang sử dụng transaction riêng.
             */

            response.sendError(
                    HttpServletResponse.SC_NOT_IMPLEMENTED,
                    "Chưa bổ sung phương thức checkout trong OrderDAO.");

        } 
        catch (IllegalArgumentException e) {
            session.setAttribute(
                    "orderError",
                    "Danh sách sản phẩm không hợp lệ.");

            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }
}
