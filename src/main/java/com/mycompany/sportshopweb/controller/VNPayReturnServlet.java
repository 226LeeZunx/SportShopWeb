
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
            HttpServletResponse response) throws ServletException, IOException {

        Map<String, String> params = new HashMap<>();

        request.getParameterMap().forEach((key, values) -> {
            if (values != null && values.length > 0) {
                params.put(key, values[0]);
            }
        });

        try {
            boolean validSignature = VNPayService.isValidSignature(params);

            String responseCode = params.get("vnp_ResponseCode");
            String transactionStatus = params.get("vnp_TransactionStatus");

            response.setContentType("text/html;charset=UTF-8");

            if (!validSignature) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println(
                        "<h2>Invalid payment signature</h2>");
                return;
            }

            boolean success = "00".equals(responseCode)
                    && "00".equals(transactionStatus);

            if (success) {
                response.getWriter().println(
                        "<h2>Sandbox payment successful</h2>");
            } else {
                response.getWriter().println(
                        "<h2>Sandbox payment unsuccessful</h2>");
                response.getWriter().println(
                        "<p>Response code: "
                                + escapeHtml(responseCode)
                                + "</p>");
            }

        } catch (Exception e) {
            throw new ServletException(
                    "Unable to verify VNPay response.", e);
        }
    }

    private String escapeHtml(String value) {
        if (value == null) {
            return "N/A";
        }

        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
