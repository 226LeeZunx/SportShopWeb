package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.OrderDAO;
import com.mycompany.sportshopweb.dao.PaymentDAO;
import com.mycompany.sportshopweb.model.Customer;
import com.mycompany.sportshopweb.model.Order;
import com.mycompany.sportshopweb.model.OrderStatus;
import com.mycompany.sportshopweb.model.Payment;
import com.mycompany.sportshopweb.model.PaymentMethod;
import com.mycompany.sportshopweb.util.VNPayService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/vnpay-return")
public class VNPayReturnServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String contextPath = request.getContextPath();

        try {
            // 1. Lấy toàn bộ tham số VNPay gửi về
            Map<String, String> params = new HashMap<>();

            request.getParameterMap().forEach((key, values) -> {
                if (values != null && values.length > 0) {
                    params.put(key, values[0]);
                }
            });

            String txnRef = params.get("vnp_TxnRef");
            String responseCode = params.get("vnp_ResponseCode");
            String transactionStatus = params.get("vnp_TransactionStatus");
            String transactionNo = params.get("vnp_TransactionNo");
            String amountText = params.get("vnp_Amount");

            // 2. Kiểm tra chữ ký
            if (!VNPayService.isValidSignature(params)) {
                response.sendRedirect(
                        contextPath + "/views/VNPayFail.jsp?reason=invalid_signature"
                );
                return;
            }

            // 3. Kiểm tra kết quả thanh toán
            boolean paymentSuccess =
                    "00".equals(responseCode)
                    && "00".equals(transactionStatus);

            if (!paymentSuccess) {
                response.sendRedirect(
                        contextPath + "/views/VNPayFail.jsp?reason=payment_failed"
                );
                return;
            }

            if (txnRef == null || txnRef.isBlank()
                    || transactionNo == null || transactionNo.isBlank()
                    || amountText == null) {
                response.sendRedirect(
                        contextPath + "/views/VNPayFail.jsp?reason=missing_data"
                );
                return;
            }

            // 4. Lấy orderId đã lưu khi tạo yêu cầu thanh toán
            HttpSession session = request.getSession(false);

            if (session == null) {
                response.sendRedirect(
                        contextPath + "/views/VNPayFail.jsp?reason=session_expired"
                );
                return;
            }

            String sessionKey = "vnpay_order_" + txnRef;
            Object savedOrderId = session.getAttribute(sessionKey);

            if (!(savedOrderId instanceof Integer)) {
                response.sendRedirect(
                        contextPath + "/views/VNPayFail.jsp?reason=order_not_found"
                );
                return;
            }

            int orderId = (Integer) savedOrderId;

            // 5. Tìm đơn hàng thuộc khách hàng đang đăng nhập
            Object customerObject = session.getAttribute("customer");

            if (!(customerObject instanceof Customer)) {
                response.sendRedirect(
                        contextPath + "/LoginServlet"
                );
                return;
            }

            Customer customer = (Customer) customerObject;

            List<Order> orders =
                    orderDAO.findByCustomerId(customer.getCustomerId());

            Order selectedOrder = null;

            if (orders != null) {
                for (Order order : orders) {
                    if (order.getOrderId() == orderId) {
                        selectedOrder = order;
                        break;
                    }
                }
            }

            if (selectedOrder == null) {
                response.sendRedirect(
                        contextPath + "/views/VNPayFail.jsp?reason=order_not_found"
                );
                return;
            }

            // 6. Đối chiếu số tiền VNPay trả về.
            // VNPay gửi số tiền theo đơn vị VND * 100.
            long expectedAmount = Math.multiplyExact(
                    selectedOrder.getTotalAmount(), 100L
            );
            long returnedAmount = Long.parseLong(amountText);

            if (returnedAmount != expectedAmount) {
                response.sendRedirect(
                        contextPath + "/views/VNPayFail.jsp?reason=amount_mismatch"
                );
                return;
            }

            // 7. Nếu đã có Payment cho đơn hàng thì không tạo bản ghi trùng
            List<Payment> existingPayments =
                    paymentDAO.findByOrderId(orderId);

            if (existingPayments == null || existingPayments.isEmpty()) {

                Payment payment = new Payment();

                payment.setOrderId(orderId);
                payment.setPaymentMethod(PaymentMethod.VNPAY);
                payment.setAmount(selectedOrder.getTotalAmount());
                payment.setTransactionCode(transactionNo);
                payment.setPaymentTime(LocalDateTime.now());

                paymentDAO.insertPayment(payment);
            }

            // 8. Cập nhật trạng thái đơn hàng
            boolean updated = orderDAO.updateOrderStatus(
                    orderId, OrderStatus.COMPLETED
            );

            if (!updated) {
                throw new ServletException(
                        "Không cập nhật được trạng thái đơn hàng " + orderId
                );
            }

            // 9. Xóa mapping sau khi xử lý thành công
            session.removeAttribute(sessionKey);

            response.sendRedirect(
                    contextPath + "/views/VNPaySuccess.jsp"
            );

        } catch (NumberFormatException e) {
            response.sendRedirect(
                    contextPath + "/views/VNPayFail.jsp?reason=invalid_amount"
            );
        } catch (Exception e) {
            log("Error processing VNPay return.", e);
            throw new ServletException(
                    "Unable to process VNPay response.", e
            );
        }
    }
}