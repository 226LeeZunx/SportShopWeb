
package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.OrderDAO;
import com.mycompany.sportshopweb.dao.CartDAO;
import com.mycompany.sportshopweb.dao.OrderItemDAO;
import com.mycompany.sportshopweb.model.Customer;
import com.mycompany.sportshopweb.model.Cart;
import com.mycompany.sportshopweb.model.CartItem;
import com.mycompany.sportshopweb.model.Order;
import com.mycompany.sportshopweb.model.OrderStatus;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();
    private final CartDAO cartDAO = new CartDAO();
    private final OrderItemDAO orderItemDAO = new OrderItemDAO();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Kiểm tra khách hàng đã đăng nhập chưa
        if (session == null || (session.getAttribute("customer") == null)) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }

        // Hiển thị trang checkout
        request.getRequestDispatcher("/views/checkout.jsp")
                .forward(request, response);
    }



@Override
protected void doPost(HttpServletRequest request,
                      HttpServletResponse response)
        throws ServletException, IOException {

    // 1. Kiểm tra đăng nhập
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("customer") == null) {
        response.sendRedirect(
                request.getContextPath() + "/LoginServlet");
        return;
    }

    Customer customer = (Customer) session.getAttribute("customer");

    // 2. Xác định hành động
    String action = request.getParameter("action");
    System.out.println("URI = " + request.getRequestURI());
System.out.println("Action = " + request.getParameter("action"));
  
    if (!"review".equals(action) && !"confirm".equals(action)) {
        response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Action không hợp lệ.");
        return;
    }

    try {
        // 3. Lấy danh sách sản phẩm được chọn
        String[] ids = request.getParameterValues("selectedVariantIds");

        if (ids == null || ids.length == 0) {
            request.setAttribute(
                    "error", "Vui lòng chọn ít nhất một sản phẩm.");

            request.getRequestDispatcher("/views/cart.jsp")
                   .forward(request, response);
            return;
        }

        Set<Integer> selectedIds = new HashSet<>();

        for (String id : ids) {
            selectedIds.add(Integer.parseInt(id));
        }

        // 4. Lấy giỏ hàng của khách hàng
        Cart cart = cartDAO.findByCustomerId(
                customer.getCustomerId());

        if (cart == null || cart.getCartItems() == null) {
            request.setAttribute("error", "Giỏ hàng không tồn tại.");

            request.getRequestDispatcher("/views/cart.jsp")
                   .forward(request, response);
            return;
        }

        // 5. Lọc sản phẩm đã chọn và tính tổng tiền
        List<CartItem> selectedItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cart.getCartItems()) {
            int variantId =
                    item.getProductVariant().getVariantId();

            if (selectedIds.contains(variantId)) {
                selectedItems.add(item);

                BigDecimal price = BigDecimal.valueOf(
                        item.getProductVariant().getPrice());

                BigDecimal subtotal = price.multiply(
                        BigDecimal.valueOf(item.getQuantity()));

                total = total.add(subtotal);
            }
        }

        if (selectedItems.isEmpty()) {
            request.setAttribute(
                    "error", "Không tìm thấy sản phẩm được chọn.");

            request.getRequestDispatcher("/views/cart.jsp")
                   .forward(request, response);
            return;
        }

        // 6. Truyền dữ liệu sản phẩm sang checkout.jsp
        request.setAttribute("selectedItems", selectedItems);
        request.setAttribute("total", total);

        // 7. Hành động REVIEW: chỉ xem lại đơn hàng
        if ("review".equals(action)) {
            request.getRequestDispatcher("/views/checkout.jsp")
                   .forward(request, response);
            return;
        }

        // 8. Hành động CONFIRM: kiểm tra thông tin đặt hàng
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String paymentMethod = request.getParameter("paymentMethod");

        if (fullName == null || fullName.trim().isEmpty()
                || phone == null || phone.trim().isEmpty()
                || address == null || address.trim().isEmpty()
                || paymentMethod == null || paymentMethod.trim().isEmpty()) {

            request.setAttribute(
                    "error", "Vui lòng nhập đầy đủ thông tin đặt hàng.");

            request.getRequestDispatcher("/views/checkout.jsp")
                   .forward(request, response);
            return;
        }

        // 9. Tạo và lưu Order
        Order order = new Order(customer);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(total.longValue());
        order.setOrderDate(new Date());

        orderDAO.insertOrder(order);

        // 10. Lưu từng OrderItem
        for (CartItem item : selectedItems) {
            int orderId = order.getOrderId();
            int variantId = item.getProductVariant().getVariantId();
            int quantity = item.getQuantity();

            orderItemDAO.insertOrderItem(
                    orderId,
                    variantId,
                    quantity);
        }

        // 11. Chuyển đến trang đặt hàng thành công
        request.setAttribute("orderId", order.getOrderId());
        request.setAttribute("orderTotal", total);

        request.getRequestDispatcher("/views/order-success.jsp")
               .forward(request, response);

    } catch (NumberFormatException e) {
        response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Mã sản phẩm không hợp lệ.");

    } catch (Exception e) {
        e.printStackTrace();

        request.setAttribute(
                "error",
                "Đặt hàng thất bại. Vui lòng thử lại.");

        request.getRequestDispatcher("/views/checkout.jsp")
               .forward(request, response);
    }
}
}
