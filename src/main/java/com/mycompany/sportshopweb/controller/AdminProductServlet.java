package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.model.Category;
import com.mycompany.sportshopweb.model.Product;
import com.mycompany.sportshopweb.util.DBUtil;
import com.mycompany.sportshopweb.util.ProductDB;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminProductServlet", urlPatterns = { "/admin/products" })
public class AdminProductServlet extends HttpServlet {

    private final ProductDB productDB = new ProductDB();

    // GET: Hiển thị danh sách sản phẩm trong trang quản trị.
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Đọc số trang từ URL, ví dụ: /admin/products?page=2.
        int page = 1;

        try {
            String pageParam = request.getParameter("page");

            if (pageParam != null && !pageParam.isBlank()) {
                page = Integer.parseInt(pageParam);
            }

            if (page < 1) {
                page = 1;
            }
        } catch (NumberFormatException e) {
            page = 1;
        }

        int pageSize = ProductDB.PAGE_SIZE;

        // Đếm tổng số sản phẩm trong database.
        long totalProducts = ProductDB.countProducts();

        // Tính tổng số trang.
        int totalPages = (int) Math.ceil(
                (double) totalProducts / pageSize);

        // Nếu số trang yêu cầu vượt quá số trang hiện có,
        // đưa người dùng về trang cuối cùng.
        if (totalPages > 0 && page > totalPages) {
            page = totalPages;
        }

        // Lấy danh sách sản phẩm của trang hiện tại.
        List<Product> products = ProductDB.getProductsByPage(page, pageSize);

        // Lấy danh sách category để hiển thị trong bộ lọc.
        // Tạm thời truy vấn tại servlet vì chưa có CategoryDAO
        // hoặc phương thức lấy tất cả category được cung cấp.
        List<Category> categories;
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            categories = em.createQuery(
                    "SELECT c FROM Category c ORDER BY c.name",
                    Category.class).getResultList();
        } finally {
            em.close();
        }

        // Đưa dữ liệu sang JSP thông qua request attributes.
        request.setAttribute("products", products);
        request.setAttribute("totalProducts", totalProducts);
        request.setAttribute("categories", categories);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageSize", pageSize);

        // Forward: chuyển request sang JSP để render HTML.
        request.getRequestDispatcher(
                "/views/admin/products.jsp").forward(request, response);
    }
}