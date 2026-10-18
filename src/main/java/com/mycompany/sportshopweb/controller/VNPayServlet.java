
package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.util.VNPayService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/vnpay-create")
public class VNPayServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        try {
            // Luồng thử nghiệm độc lập: số tiền lấy từ form.
            String amountText = request.getParameter("amount");
            if (amountText == null || amountText.isBlank()) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Missing payment amount.");
                return;
            }

            long amountVnd = Long.parseLong(amountText);

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
