package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.model.Category;
import com.mycompany.sportshopweb.model.Product;
import com.mycompany.sportshopweb.util.DBUtil;
import com.mycompany.sportshopweb.dao.ProductDAO;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminProductServlet", urlPatterns = { "/admin/products" })
public class AdminProductServlet extends HttpServlet {

    private final ProductDAO productDB = new ProductDAO();

    // GET: Hiển thị danh sách sản phẩm trong trang quản trị.

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Đọc số trang từ URL.
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

        // 2. Đọc bộ lọc từ form GET.
        String keyword = request.getParameter("keyword");

        if (keyword == null) {
            keyword = "";
        } else {
            keyword = keyword.trim();
        }

        int categoryId = 0;
        String categoryParam = request.getParameter("category");

        try {
            if (categoryParam != null && !categoryParam.isBlank()) {
                categoryId = Integer.parseInt(categoryParam);

                if (categoryId < 0) {
                    categoryId = 0;
                }
            }
        } catch (NumberFormatException e) {
            categoryId = 0;
        }

        // 3. Xác định kích thước trang.
        int pageSize = ProductDAO.PAGE_SIZE;

        // 4. Đếm sản phẩm theo bộ lọc hiện tại.
        long totalProducts = ProductDAO.countProducts(keyword, categoryId);

        // 5. Tính tổng số trang sau khi lọc.
        int totalPages = (int) Math.ceil(
                (double) totalProducts / pageSize);

        // 6. Điều chỉnh page nếu vượt phạm vi.
        if (totalPages == 0) {
            page = 1;
        } else if (page > totalPages) {
            page = totalPages;
        }

        // 7. Lấy sản phẩm theo trang và bộ lọc.
        List<Product> products = ProductDAO.getProductsByPage(
                page, pageSize, keyword, categoryId);

        // 8. Lấy danh sách category để hiển thị dropdown.
        // Giữ nguyên cách truy vấn hiện tại, không sửa CategoryDB.
        List<Category> categories;
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            categories = em.createQuery(
                    "SELECT c FROM Category c ORDER BY c.name",
                    Category.class)
                    .getResultList();
        } finally {
            em.close();
        }

        // 9. Chuyển dữ liệu sang JSP.
        request.setAttribute("products", products);
        request.setAttribute("totalProducts", totalProducts);
        request.setAttribute("categories", categories);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageSize", pageSize);

        // Giữ bộ lọc trên form và khi chuyển trang.
        request.setAttribute("keyword", keyword);
        request.setAttribute("selectedCategory", categoryId);

        // 10. Forward sang trang quản lý sản phẩm.
        request.getRequestDispatcher(
                "/views/admin/products.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Đảm bảo dữ liệu gửi từ form được đọc đúng encoding.
        request.setCharacterEncoding("UTF-8");

        // Chỉ xử lý action Delete ở bước này.
        String action = request.getParameter("action");

        if (!"delete".equals(action)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            return;
        }

        // Đọc ID sản phẩm từ hidden input trong products.jsp.
        int productId;

        try {
            productId = Integer.parseInt(request.getParameter("productId"));

            if (productId <= 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID");
                return;
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID");
            return;
        }

        try {
            // Đặt quantity = 0 cho tất cả biến thể của sản phẩm.
            boolean success = productDB.softDeleteProduct(productId);

            // Chuyển về danh sách sản phẩm và hiển thị kết quả.
            String contextPath = request.getContextPath();

            if (success) {
                response.sendRedirect(contextPath + "/admin/products?deleteSuccess=1");
            } else {
                response.sendRedirect(contextPath + "/admin/products?deleteError=notFound");
            }

        } catch (RuntimeException e) {
            log("Unable to update product inventory for product ID: " + productId, e);
            response.sendRedirect(
                    request.getContextPath() + "/admin/products?deleteError=server");
        }
    }

}