
package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.OrderDAO;
import com.mycompany.sportshopweb.model.OrderStatus;
import com.mycompany.sportshopweb.util.VNPayService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/vnpay-return")
public class VNPayReturnServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String contextPath = request.getContextPath();

        // 1. Lấy tham số VNPay trả về
        Map<String, String> params = new HashMap<>();

        request.getParameterMap().forEach((key, values) -> {
            if (values != null && values.length > 0) {
                params.put(key, values[0]);
            }
        });

        try {
            // 2. Xác minh chữ ký VNPay
            boolean validSignature =
                    VNPayService.isValidSignature(params);

            String txnRef = params.get("vnp_TxnRef");
            String responseCode = params.get("vnp_ResponseCode");
            String transactionStatus =
                    params.get("vnp_TransactionStatus");

            System.out.println("===== VNPAY RETURN =====");
            System.out.println("Transaction reference = " + txnRef);
            System.out.println("Response code = " + responseCode);
            System.out.println("Transaction status = " + transactionStatus);
            System.out.println("Signature valid = " + validSignature);

            // 3. Chữ ký không hợp lệ
            if (!validSignature) {
                response.sendRedirect(
                        contextPath
                        + "/views/VNPayFail.jsp?reason=invalid_signature");
                return;
            }

            // 4. Kiểm tra kết quả thanh toán
            boolean paymentSuccess =
                    "00".equals(responseCode)
                    && "00".equals(transactionStatus);

            if (!paymentSuccess) {
                response.sendRedirect(
                        contextPath + "/views/VNPayFail.jsp");
                return;
            }

            // 5. Kiểm tra mã giao dịch
            if (txnRef == null || txnRef.isBlank()) {
                response.sendRedirect(
                        contextPath
                        + "/views/VNPayFail.jsp?reason=missing_txn_ref");
                return;
            }

            // 6. Lấy orderId đã lưu trong session khi tạo giao dịch
            HttpSession session = request.getSession(false);

            if (session == null) {
                response.sendRedirect(
                        contextPath
                        + "/views/VNPayFail.jsp?reason=session_expired");
                return;
            }

            String sessionKey = "vnpay_order_" + txnRef;
            Object savedOrderId = session.getAttribute(sessionKey);

            if (!(savedOrderId instanceof Integer)) {
                System.out.println(
                        "Cannot find order mapping for txnRef: " + txnRef);

                response.sendRedirect(
                        contextPath
                        + "/views/VNPayFail.jsp?reason=order_not_found");
                return;
            }

            int orderId = (Integer) savedOrderId;

            System.out.println("Order ID = " + orderId);

            // 7. Cập nhật trạng thái đơn hàng
            boolean updated = orderDAO.updateOrderStatus(
                    orderId,
                    OrderStatus.COMPLETED
            );

            System.out.println("Order updated = " + updated);

            if (updated) {
                // Chỉ xóa liên kết sau khi cập nhật thành công
                session.removeAttribute(sessionKey);

                response.sendRedirect(
                        contextPath + "/views/VNPaySuccess.jsp");
            } else {
                response.sendRedirect(
                        contextPath
                        + "/views/VNPayFail.jsp?reason=update_failed");
            }

        } catch (Exception e) {
            log("Error processing VNPay return.", e);

            throw new ServletException(
                    "Unable to process VNPay response.", e);
        }
    }
}
