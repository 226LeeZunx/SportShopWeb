package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.OrderDAO;
import com.mycompany.sportshopweb.dao.CartDAO;
import com.mycompany.sportshopweb.dao.VoucherDAO;
import com.mycompany.sportshopweb.dao.AdressDAO;
import com.mycompany.sportshopweb.dao.OrderItemDAO;

import com.mycompany.sportshopweb.model.Adress;
import com.mycompany.sportshopweb.model.Voucher;
import com.mycompany.sportshopweb.model.Customer;
import com.mycompany.sportshopweb.model.Cart;
import com.mycompany.sportshopweb.model.CartItem;
import com.mycompany.sportshopweb.model.Order;
import com.mycompany.sportshopweb.model.OrderStatus;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private final VoucherDAO voucherDAO = new VoucherDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final CartDAO cartDAO = new CartDAO();
    private final OrderItemDAO orderItemDAO = new OrderItemDAO();
    private final AdressDAO adressDAO = new AdressDAO();

    // =========================================================
    // GET: HIỂN THỊ CHECKOUT
    // =========================================================
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // 1. Kiểm tra đăng nhập
        if (session == null || session.getAttribute("customer") == null) {
            response.sendRedirect(
                    request.getContextPath() + "/LoginServlet");
            return;
        }

        Customer customer = (Customer) session.getAttribute("customer");
        int customerId = customer.getCustomerId();

        // 2. Lấy danh sách sản phẩm đã chọn từ session
        @SuppressWarnings("unchecked")
        Set<Integer> selectedIds =
                (Set<Integer>) session.getAttribute("checkoutVariantIds");

        // 3. Nếu chưa có danh sách trong session,
        // lấy danh sách từ tham số selectedVariantIds nếu có.
        if (selectedIds == null || selectedIds.isEmpty()) {

            String[] ids =
                    request.getParameterValues("selectedVariantIds");

            if (ids != null && ids.length > 0) {
                selectedIds = new HashSet<>();

                for (String id : ids) {
                    if (id != null && !id.trim().isEmpty()) {
                        selectedIds.add(Integer.parseInt(id));
                    }
                }

                session.setAttribute("checkoutVariantIds", selectedIds);
            }
        }

        // 4. Tải giỏ hàng và tính lại tổng tiền
        List<CartItem> selectedItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        if (selectedIds != null && !selectedIds.isEmpty()) {

            Cart cart = cartDAO.findByCustomerId(customerId);

            if (cart != null && cart.getCartItems() != null) {

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
            }
        }

        // 5. Khôi phục mã voucher đã áp dụng
        String voucherCode =
                (String) session.getAttribute("checkoutVoucherCode");

        Voucher selectedVoucher = null;
        BigDecimal discountAmount = BigDecimal.ZERO;

        if (voucherCode != null && !voucherCode.trim().isEmpty()) {

            selectedVoucher = voucherDAO.findByCode(voucherCode.trim());

            if (selectedVoucher != null
                    && selectedVoucher.isActive()
                    && selectedVoucher.getDiscount() > 0
                    && selectedVoucher.getDiscount() <= 100) {

                BigDecimal discountPercent =
                        BigDecimal.valueOf(selectedVoucher.getDiscount());

                discountAmount = total
                        .multiply(discountPercent)
                        .divide(BigDecimal.valueOf(100));

                discountAmount = discountAmount.setScale(
                        0, RoundingMode.HALF_UP);

                if (discountAmount.compareTo(total) > 0) {
                    discountAmount = total;
                }

            } else {
                selectedVoucher = null;
                voucherCode = null;
                session.removeAttribute("checkoutVoucherCode");
            }
        }

        BigDecimal finalTotal = total.subtract(discountAmount);

        // 6. Đưa dữ liệu sản phẩm và giá tiền sang JSP
        request.setAttribute("selectedItems", selectedItems);
        request.setAttribute("total", total);
        request.setAttribute("discountAmount", discountAmount);
        request.setAttribute("finalTotal", finalTotal);
        request.setAttribute("voucherCode", voucherCode);
        request.setAttribute("selectedVoucher", selectedVoucher);

        request.setAttribute(
                "activeVouchers",
                voucherDAO.findActiveVouchers());

        // 7. Lấy địa chỉ của khách hàng
        List<Adress> addresses =
                adressDAO.findByCustomerId(customerId);

        request.setAttribute("addresses", addresses);

        Adress defaultAddress =
                adressDAO.findDefaultByCustomerId(customerId);

        request.setAttribute("defaultAddress", defaultAddress);

        // 8. Khôi phục địa chỉ đang chọn
        Integer selectedAddressId =
                (Integer) session.getAttribute("selectedAddressId");

        Adress selectedAddress = null;

        if (selectedAddressId != null) {
            selectedAddress = adressDAO.findByIdAndCustomerId(
                    selectedAddressId, customerId);
        }

        if (selectedAddress == null) {
            selectedAddress = defaultAddress;
        }

        if (selectedAddress != null) {
            session.setAttribute(
                    "selectedAddressId",
                    selectedAddress.getAddressId());
        }

        request.setAttribute("selectedAddress", selectedAddress);

        // 9. Khôi phục thông tin người nhận
        request.setAttribute(
                "fullName",
                session.getAttribute("checkoutFullName"));

        request.setAttribute(
                "phone",
                session.getAttribute("checkoutPhone"));

        request.setAttribute(
                "note",
                session.getAttribute("checkoutNote"));

        request.setAttribute(
                "paymentMethod",
                session.getAttribute("checkoutPaymentMethod"));

        // 10. Hiển thị Checkout
        request.getRequestDispatcher("/views/checkout.jsp")
                .forward(request, response);
    }

    // =========================================================
    // POST: REVIEW VOUCHER HOẶC CONFIRM ĐƠN HÀNG
    // =========================================================
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // 1. Kiểm tra đăng nhập
        if (session == null || session.getAttribute("customer") == null) {
            response.sendRedirect(
                    request.getContextPath() + "/LoginServlet");
            return;
        }

        Customer customer = (Customer) session.getAttribute("customer");
        int customerId = customer.getCustomerId();

        String action = request.getParameter("action");

        if (!"review".equals(action) && !"confirm".equals(action)) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Action không hợp lệ.");
            return;
        }

        try {

            // -------------------------------------------------
            // 2. Lấy danh sách sản phẩm đã chọn
            // -------------------------------------------------
            String[] ids =
                    request.getParameterValues("selectedVariantIds");

            Set<Integer> selectedIds = new HashSet<>();

            if (ids != null) {
                for (String id : ids) {
                    if (id != null && !id.trim().isEmpty()) {
                        selectedIds.add(Integer.parseInt(id));
                    }
                }
            }

            // Nếu form không gửi IDs, sử dụng danh sách trong session
            if (selectedIds.isEmpty()) {

                @SuppressWarnings("unchecked")
                Set<Integer> savedIds =
                        (Set<Integer>) session.getAttribute(
                                "checkoutVariantIds");

                if (savedIds != null) {
                    selectedIds.addAll(savedIds);
                }
            }

            if (selectedIds.isEmpty()) {
                request.setAttribute(
                        "error",
                        "Vui lòng chọn ít nhất một sản phẩm.");

                request.getRequestDispatcher("/views/cart.jsp")
                        .forward(request, response);
                return;
            }

            // Lưu lại danh sách sản phẩm đã chọn
            session.setAttribute("checkoutVariantIds", selectedIds);

            // -------------------------------------------------
            // 3. Lấy giỏ hàng
            // -------------------------------------------------
            Cart cart = cartDAO.findByCustomerId(customerId);

            if (cart == null || cart.getCartItems() == null) {
                request.setAttribute(
                        "error",
                        "Giỏ hàng không tồn tại.");

                request.getRequestDispatcher("/views/cart.jsp")
                        .forward(request, response);
                return;
            }

            // -------------------------------------------------
            // 4. Lọc sản phẩm và tính tổng tiền
            // -------------------------------------------------
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
                session.removeAttribute("checkoutVariantIds");

                request.setAttribute(
                        "error",
                        "Không tìm thấy sản phẩm được chọn trong giỏ hàng.");

                request.getRequestDispatcher("/views/cart.jsp")
                        .forward(request, response);
                return;
            }

            // -------------------------------------------------
            // 5. Lấy và lưu thông tin giao hàng
            // -------------------------------------------------
            String fullName = request.getParameter("fullName");
            String phone = request.getParameter("phone");
            String note = request.getParameter("note");
            String paymentMethod = request.getParameter("paymentMethod");

            String addressIdValue = request.getParameter("addressId");

            // Chỉ cập nhật session khi request có gửi trường đó.
            // Tránh ghi đè dữ liệu cũ bằng null khi form review thiếu trường.
            if (fullName != null) {
                session.setAttribute("checkoutFullName", fullName.trim());
            }

            if (phone != null) {
                session.setAttribute("checkoutPhone", phone.trim());
            }

            if (note != null) {
                session.setAttribute("checkoutNote", note);
            }

            if (paymentMethod != null) {
                session.setAttribute(
                        "checkoutPaymentMethod",
                        paymentMethod);
            }

            if (addressIdValue != null
                    && !addressIdValue.trim().isEmpty()) {

                int addressId = Integer.parseInt(addressIdValue);

                Adress address = adressDAO.findByIdAndCustomerId(
                        addressId, customerId);

                if (address != null) {
                    session.setAttribute(
                            "selectedAddressId",
                            address.getAddressId());
                }
            }

            // -------------------------------------------------
            // 6. Xử lý voucher
            // -------------------------------------------------
            String voucherCode = request.getParameter("voucherCode");

            // Nếu form không gửi mã voucher, khôi phục mã trong session.
            if (voucherCode == null) {
                voucherCode =
                        (String) session.getAttribute("checkoutVoucherCode");
            }

            BigDecimal discountAmount = BigDecimal.ZERO;
            Voucher selectedVoucher = null;

            String voucherError = null;

            if (voucherCode != null && !voucherCode.trim().isEmpty()) {

                voucherCode = voucherCode.trim();

                selectedVoucher = voucherDAO.findByCode(voucherCode);

                if (selectedVoucher == null
                        || !selectedVoucher.isActive()) {

                    voucherError =
                            "Voucher không tồn tại hoặc đã hết hiệu lực.";

                    selectedVoucher = null;
                    voucherCode = null;
                    session.removeAttribute("checkoutVoucherCode");

                } else if (selectedVoucher.getDiscount() <= 0
                        || selectedVoucher.getDiscount() > 100) {

                    voucherError =
                            "Phần trăm giảm giá của voucher không hợp lệ.";

                    selectedVoucher = null;
                    voucherCode = null;
                    session.removeAttribute("checkoutVoucherCode");

                } else {

                    BigDecimal discountPercent =
                            BigDecimal.valueOf(
                                    selectedVoucher.getDiscount());

                    discountAmount = total
                            .multiply(discountPercent)
                            .divide(BigDecimal.valueOf(100));

                    discountAmount = discountAmount.setScale(
                            0, RoundingMode.HALF_UP);

                    if (discountAmount.compareTo(total) > 0) {
                        discountAmount = total;
                    }

                    session.setAttribute(
                            "checkoutVoucherCode",
                            voucherCode);
                }

            } else {
                // Không nhập voucher nghĩa là bỏ voucher hiện tại
                voucherCode = null;
                session.removeAttribute("checkoutVoucherCode");
            }

            BigDecimal finalTotal = total.subtract(discountAmount);

            // -------------------------------------------------
            // 7. Chuẩn bị dữ liệu cho checkout.jsp
            // -------------------------------------------------
            request.setAttribute("selectedItems", selectedItems);
            request.setAttribute("total", total);
            request.setAttribute("discountAmount", discountAmount);
            request.setAttribute("finalTotal", finalTotal);
            request.setAttribute("voucherCode", voucherCode);
            request.setAttribute("selectedVoucher", selectedVoucher);
            request.setAttribute("voucherError", voucherError);

            request.setAttribute(
                    "activeVouchers",
                    voucherDAO.findActiveVouchers());

            request.setAttribute("fullName", fullName != null
                    ? fullName
                    : session.getAttribute("checkoutFullName"));

            request.setAttribute("phone", phone != null
                    ? phone
                    : session.getAttribute("checkoutPhone"));

            request.setAttribute("note", note != null
                    ? note
                    : session.getAttribute("checkoutNote"));

            request.setAttribute("paymentMethod", paymentMethod != null
                    ? paymentMethod
                    : session.getAttribute("checkoutPaymentMethod"));

            // Tải lại địa chỉ mỗi lần trả về Checkout
            List<Adress> addresses =
                    adressDAO.findByCustomerId(customerId);

            request.setAttribute("addresses", addresses);

            Adress defaultAddress =
                    adressDAO.findDefaultByCustomerId(customerId);

            request.setAttribute("defaultAddress", defaultAddress);

            Integer selectedAddressId =
                    (Integer) session.getAttribute("selectedAddressId");

            Adress selectedAddress = null;

            if (selectedAddressId != null) {
                selectedAddress = adressDAO.findByIdAndCustomerId(
                        selectedAddressId, customerId);
            }

            if (selectedAddress == null) {
                selectedAddress = defaultAddress;
            }

            request.setAttribute("selectedAddress", selectedAddress);

            // -------------------------------------------------
            // 8. REVIEW: chỉ xem lại Checkout, chưa tạo đơn hàng
            // -------------------------------------------------
            if ("review".equals(action)) {
                request.getRequestDispatcher("/views/checkout.jsp")
                        .forward(request, response);
                return;
            }

            // -------------------------------------------------
            // 9. CONFIRM: kiểm tra thông tin trước khi đặt hàng
            // -------------------------------------------------
            fullName = (String) session.getAttribute("checkoutFullName");
            phone = (String) session.getAttribute("checkoutPhone");
            note = (String) session.getAttribute("checkoutNote");
            paymentMethod =
                    (String) session.getAttribute("checkoutPaymentMethod");

            selectedAddressId =
                    (Integer) session.getAttribute("selectedAddressId");

            if (fullName == null || fullName.trim().isEmpty()
                    || phone == null || phone.trim().isEmpty()
                    || paymentMethod == null
                    || paymentMethod.trim().isEmpty()) {

                request.setAttribute(
                        "error",
                        "Vui lòng nhập đầy đủ họ tên, số điện thoại và phương thức thanh toán.");

                request.getRequestDispatcher("/views/checkout.jsp")
                        .forward(request, response);
                return;
            }

            if (!"COD".equals(paymentMethod)
                    && !"BANK".equals(paymentMethod)) {

                request.setAttribute(
                        "error",
                        "Phương thức thanh toán không hợp lệ.");

                request.getRequestDispatcher("/views/checkout.jsp")
                        .forward(request, response);
                return;
            }

            if (selectedAddressId == null) {
                request.setAttribute(
                        "error",
                        "Vui lòng chọn địa chỉ giao hàng.");

                request.getRequestDispatcher("/views/checkout.jsp")
                        .forward(request, response);
                return;
            }

            selectedAddress =
                    adressDAO.findByIdAndCustomerId(
                            selectedAddressId, customerId);

            if (selectedAddress == null) {
                session.removeAttribute("selectedAddressId");

                request.setAttribute(
                        "error",
                        "Địa chỉ giao hàng không hợp lệ. Vui lòng chọn lại.");

                request.getRequestDispatcher("/views/checkout.jsp")
                        .forward(request, response);
                return;
            }

            // -------------------------------------------------
            // 10. Tạo đơn hàng
            // -------------------------------------------------
            Order order = new Order(customer);

            order.setOrderStatus(OrderStatus.CONFIRMED);
            order.setTotalAmount(finalTotal.longValue());
            order.setOrderDate(new Date());
            order.setVoucher(selectedVoucher);
            order.setDiscountAmount(discountAmount.longValue());
            order.setAddress(selectedAddress);

            orderDAO.insertOrder(order);

            // -------------------------------------------------
            // 11. Lưu các sản phẩm trong đơn hàng
            // -------------------------------------------------
            for (CartItem item : selectedItems) {

                int orderId = order.getOrderId();

                int variantId =
                        item.getProductVariant().getVariantId();

                int quantity = item.getQuantity();

                orderItemDAO.insertOrderItem(
                        orderId,
                        variantId,
                        quantity);
            }

            // -------------------------------------------------
            // 12. Hiển thị trang thành công
            // -------------------------------------------------
            request.setAttribute("orderId", order.getOrderId());
            request.setAttribute("orderTotal", finalTotal);
            request.setAttribute("discountAmount", discountAmount);

            // Xóa trạng thái Checkout sau khi tạo đơn thành công
            session.removeAttribute("checkoutVariantIds");
            session.removeAttribute("checkoutVoucherCode");
            session.removeAttribute("checkoutFullName");
            session.removeAttribute("checkoutPhone");
            session.removeAttribute("checkoutNote");
            session.removeAttribute("checkoutPaymentMethod");
            session.removeAttribute("selectedAddressId");

            request.getRequestDispatcher("/views/order-success.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Mã sản phẩm hoặc địa chỉ không hợp lệ.");

        } catch (Exception e) {

            e.printStackTrace();

            request.setAttribute(
                    "error",
                    "Đặt hàng thất bại. Vui lòng thử lại.");

            // Không forward một trang Checkout thiếu dữ liệu.
            // Chuyển hướng về GET để tải lại sản phẩm, địa chỉ và giá tiền.
            response.sendRedirect(
                    request.getContextPath() + "/checkout");
        }
    }
}