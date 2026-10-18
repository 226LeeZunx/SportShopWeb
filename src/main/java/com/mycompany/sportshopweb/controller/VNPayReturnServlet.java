
package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.util.VNPayService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/vnpay-return")
public class VNPayReturnServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, String> params = new HashMap<>();

        request.getParameterMap().forEach((key, values) -> {
            if (values != null && values.length > 0) {
                params.put(key, values[0]);
            }
        });

        try {
            // Xác minh chữ ký phản hồi từ VNPay
            boolean validSignature = VNPayService.isValidSignature(params);

            String responseCode = params.get("vnp_ResponseCode");
            String transactionStatus = params.get("vnp_TransactionStatus");

            System.out.println("===== VNPAY RETURN =====");
            System.out.println("Response code = " + responseCode);
            System.out.println("Transaction status = " + transactionStatus);
            System.out.println("Signature valid = " + validSignature);

            String contextPath = request.getContextPath();

            // Không cho phép phản hồi có chữ ký sai được xem là thành công
            if (!validSignature) {
                response.sendRedirect(
                        contextPath + "/views/VNPayFail.jsp?reason=invalid_signature");
                return;
            }

            // Chỉ xem là thành công khi cả hai mã đều bằng 00
            boolean success = "00".equals(responseCode)
                    && "00".equals(transactionStatus);

            if (success) {
                response.sendRedirect(
                        contextPath + "/views/VNPaySuccess.jsp");
            } else {
                response.sendRedirect(
                        contextPath + "/views/VNPayFail.jsp");
            }

        } catch (Exception e) {
            throw new ServletException(
                    "Unable to verify VNPay response.", e);
        }
    }
}
