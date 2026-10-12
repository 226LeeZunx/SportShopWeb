/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.CartDAO;
import com.mycompany.sportshopweb.dao.CartItemDAO;
import com.mycompany.sportshopweb.model.Cart;
import com.mycompany.sportshopweb.model.Customer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private final CartDAO cartDAO = new CartDAO();
    private final CartItemDAO cartItemDAO = new CartItemDAO();

    @Override
protected void doPost(HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

    try {
        String action = request.getParameter("action");

        HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("customer") == null) {
        response.sendRedirect(
            request.getContextPath() + "/LoginServlet"
        );
        return;
    }

    Customer customer = (Customer) session.getAttribute("customer");
    int customerId = customer.getCustomerId();

        int variantId = Integer.parseInt(request.getParameter("variantId"));

        if (variantId <= 0) {
            throw new IllegalArgumentException(
                    "Mã phiên bản sản phẩm không hợp lệ."
            );
        }

        if (action == null || action.isBlank()) {
            throw new IllegalArgumentException(
                    "Action không hợp lệ."
            );
        }

        if (action.equals("add")) {
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            if (quantity <= 0) {
                throw new IllegalArgumentException(
                        "Số lượng phải lớn hơn 0."
                );
            }

            cartItemDAO.insertCartItem(
                    customerId, variantId, quantity
            );

        } else if (action.equals("update")) {
            int quantity = Integer.parseInt(
                    request.getParameter("quantity")
            );

            if (quantity <= 0) {
                throw new IllegalArgumentException(
                        "Số lượng phải lớn hơn 0."
                );
            }

            cartItemDAO.updateCartItem(
                    customerId, variantId, quantity
            );

        } else if (action.equals("delete")) {
            cartItemDAO.deleteCartItem(
                    customerId, variantId
            );

        } else {
            throw new IllegalArgumentException(
                    "Action không hợp lệ."
            );
        }

        response.sendRedirect(request.getContextPath() + "/cart");

    } catch (NumberFormatException e) {
        response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Mã sản phẩm hoặc số lượng phải là số nguyên."
        );

    } catch (IllegalArgumentException | IllegalStateException e) {
        request.getSession().setAttribute(
                "cartError", e.getMessage()
        );

        response.sendRedirect(request.getContextPath() + "/cart"
        );

    } catch (Exception e) {
        throw new ServletException(
                "Lỗi khi xử lý giỏ hàng.", e
        );
    }
}

@Override
protected void doGet(HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("customer") == null) {
           response.sendRedirect(
               request.getContextPath() + "/LoginServlet"
           );
           return;
       }


    try {
        Customer customer = (Customer) session.getAttribute("customer");
        int customerId = customer.getCustomerId();

        Cart cart = cartDAO.findByCustomerId(customerId);

        long total = 0;

        if (cart != null && cart.getCartItems() != null) {
            request.setAttribute("cartItems", cart.getCartItems());
            request.setAttribute("cart", cart);

            for (var item : cart.getCartItems()) {
                total += item.getProductVariant().getPrice() * item.getQuantity();
            }
        } else {
            request.setAttribute("cartItems", java.util.Collections.emptyList());
        }

        request.setAttribute("total", total);

        request.getRequestDispatcher("/views/cart.jsp").forward(request, response);

    } catch (Exception e) {
        throw new ServletException("Lỗi khi tải giỏ hàng.", e);
    }
}

}
