
package com.mycompany.sportshopweb.controller;

// ProductDB nằm trong package util, không phải package dao.
import com.mycompany.sportshopweb.util.ProductDB;
import com.mycompany.sportshopweb.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Controller xử lý request truy cập trang Admin Dashboard.
 *
 * Nhiệm vụ:
 * 1. Lấy dữ liệu tổng quan cần hiển thị.
 * 2. Lấy danh sách sản phẩm theo trang.
 * 3. Chuyển dữ liệu sang JSP để hiển thị.
 */
@WebServlet(name = "AdminDashboardServlet", urlPatterns = { "/admin/dashboard" })
public class AdminDashboardServlet extends HttpServlet {

    // Xử lý request GET khi người dùng mở Dashboard.
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // 1. Đếm tổng số sản phẩm trong hệ thống.
            long totalProducts = ProductDB.countProducts();

            // 2. Đọc số trang từ URL.
            int page = 1;

            String pageParam = request.getParameter("page");

            if (pageParam != null && !pageParam.isBlank()) {
                try {
                    page = Integer.parseInt(pageParam);
                } catch (NumberFormatException e) {
                    // Tham số page không hợp lệ thì quay về trang 1.
                    page = 1;
                }
            }

            // Không chấp nhận số trang nhỏ hơn 1.
            if (page < 1) {
                page = 1;
            }

            // 3. Xác định kích thước trang từ ProductDB.
            int pageSize = ProductDB.PAGE_SIZE;

            // Tính tổng số trang.
            // Nếu chưa có sản phẩm, vẫn hiển thị trang 1.
            long totalPages = Math.max(
                    1L,
                    (long) Math.ceil((double) totalProducts / pageSize));

            // Nếu page vượt quá số trang hiện có,
            // đưa người dùng về trang cuối cùng.
            if (page > totalPages) {
                page = (int) totalPages;
            }

            // 4. Lấy danh sách sản phẩm của trang hiện tại.
            List<Product> products = ProductDB.getProductsByPage(page, pageSize);

            // 5. Đưa dữ liệu vào request để JSP sử dụng.
            request.setAttribute("totalProducts", totalProducts);
            request.setAttribute("products", products);
            request.setAttribute("currentPage", page);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("totalPages", totalPages);

            // 6. Chuyển tiếp sang JSP Dashboard.
            request.getRequestDispatcher(
                    "/views/admin/dashboard.jsp").forward(request, response);

        } catch (RuntimeException e) {
            throw new ServletException(
                    "Unable to load Admin Dashboard.", e);
        }
    }
}
