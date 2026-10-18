
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
            HttpServletResponse response)
            throws ServletException, IOException {

        String amountParam = request.getParameter("amount");

        if (amountParam == null || amountParam.trim().isEmpty()) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Missing payment amount.");
            return;
        }

        final long amountVnd;

        try {
            amountVnd = Long.parseLong(amountParam.trim());

            if (amountVnd <= 0) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Payment amount must be greater than zero.");
                return;
            }
        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid payment amount.");
            return;
        }

        try {
            String txnRef = UUID.randomUUID()
                    .toString()
                    .replace("-", "")
                    .substring(0, 20);

            String orderInfo = "VNPay Sandbox test " + txnRef;

            String clientIp = getClientIp(request);

            String paymentUrl = VNPayService.createPaymentUrl(
                    amountVnd,
                    txnRef,
                    orderInfo,
                    clientIp);

            response.sendRedirect(paymentUrl);

        } catch (IllegalStateException e) {
            log("VNPay configuration error.", e);
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "VNPay configuration is incomplete. Check server environment variables.");
        } catch (Exception e) {
            log("Unable to create VNPay payment URL.", e);
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to create VNPay payment URL.");
        }
    }

    private String getClientIp(HttpServletRequest request) {
        // Do not trust X-Forwarded-For unless a trusted proxy is configured.
        String ip = request.getRemoteAddr();

        if (ip == null || ip.trim().isEmpty()) {
            return "127.0.0.1";
        }

        return ip.trim();
    }
}
