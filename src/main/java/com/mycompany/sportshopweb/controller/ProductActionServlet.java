package com.mycompany.sportshopweb.controller;

import com.cloudinary.utils.ObjectUtils;
import com.mycompany.sportshopweb.model.Product;
import com.mycompany.sportshopweb.model.ProductImage;
import com.mycompany.sportshopweb.util.BrandDB;
import com.mycompany.sportshopweb.util.CategoryDB;
import com.mycompany.sportshopweb.util.CloudinaryConfig;
import com.mycompany.sportshopweb.util.ProductDB;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ProductActionServlet", urlPatterns = { "/ProductActionServlet" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024, maxRequestSize = 25 * 1024 * 1024)
public class ProductActionServlet extends HttpServlet {
    @Override
    public void init() {
        ProductDB productDAO = new ProductDB();
        getServletContext().setAttribute("ProductDAO", productDAO);

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Chuẩn bị dữ liệu cho các dropdown Brand và Category.
        BrandDB brandDAO = new BrandDB();
        CategoryDB categoryDAO = new CategoryDB();

        request.setAttribute("brands", brandDAO.getAllBrand());
        request.setAttribute("categories", categoryDAO.getAllCategory());

        // 2. Xác định thao tác: tạo mới hoặc chỉnh sửa sản phẩm.
        String action = request.getParameter("action");

        if ("edit".equals(action)) {

            // Lấy productId từ URL, ví dụ:
            // /ProductActionServlet?action=edit&id=5
            String idStr = request.getParameter("id");

            try {
                int productId = Integer.parseInt(idStr);

                if (productId <= 0) {
                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid product ID");
                    return;
                }

                // 3. Tìm sản phẩm trong database.
                Product product = ProductDB.getProductById(productId);

                if (product == null) {
                    response.sendError(
                            HttpServletResponse.SC_NOT_FOUND,
                            "Product not found");
                    return;
                }

                // 4. Gửi dữ liệu sản phẩm sang JSP ở chế độ Edit.
                request.setAttribute("product", product);
                request.setAttribute("editMode", true);

            } catch (NumberFormatException e) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid product ID");
                return;
            }

        } else {

            // Chế độ Create: chưa có sản phẩm để chỉnh sửa.
            request.setAttribute("editMode", false);
        }

        // 5. Hiển thị form Admin dùng chung cho Create và Edit.
        request.getRequestDispatcher(
                "/views/admin/products-form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // Xác định thao tác từ form: create hoặc update.
        String action = request.getParameter("action");
        boolean isUpdate = "update".equals(action);

        if (!isUpdate && !"create".equals(action)) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid product action");
            return;
        }

        String name = request.getParameter("productName");
        String description = request.getParameter("description");
        String brandStr = request.getParameter("brandId");
        String categoryStr = request.getParameter("categoryId");

        int productId = 0;
        int brandId;
        int categoryId;

        // Kiểm tra dữ liệu cơ bản trước khi thao tác database.
        if (name == null || name.isBlank()
                || brandStr == null || brandStr.isBlank()
                || categoryStr == null || categoryStr.isBlank()) {

            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin.");
            doGet(request, response);
            return;
        }

        try {
            brandId = Integer.parseInt(brandStr);
            categoryId = Integer.parseInt(categoryStr);

            if (brandId <= 0 || categoryId <= 0) {
                throw new NumberFormatException("Invalid brand/category ID");
            }

            if (isUpdate) {
                String productIdStr = request.getParameter("productId");

                if (productIdStr == null || productIdStr.isBlank()) {
                    throw new NumberFormatException("Missing product ID");
                }

                productId = Integer.parseInt(productIdStr);

                if (productId <= 0) {
                    throw new NumberFormatException("Invalid product ID");
                }
            }

        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid product, brand, or category ID");
            return;
        }

        ProductDB productDAO = (ProductDB) getServletContext().getAttribute("ProductDAO");

        if (productDAO == null) {
            productDAO = new ProductDB();
            getServletContext().setAttribute("ProductDAO", productDAO);
        }

        try {
            if (isUpdate) {
                // Cập nhật thông tin cơ bản, chưa thay đổi danh sách ảnh.
                boolean updated = productDAO.updateProduct(
                        productId,
                        name.trim(),
                        description,
                        brandId,
                        categoryId);

                if (!updated) {
                    response.sendError(
                            HttpServletResponse.SC_NOT_FOUND,
                            "Product not found");
                    return;
                }

                // Ở bước này, Update chỉ cập nhật thông tin cơ bản.
                // Ảnh cũ được giữ nguyên; xử lý thêm ảnh mới ở bước tiếp theo.
                response.sendRedirect(
                        request.getContextPath()
                                + "/admin/products?updateSuccess=1");
                return;
            }

            // CREATE: giữ nguyên luồng tạo sản phẩm và upload Cloudinary.
            Product product = new Product();
            product.setProductName(name.trim());
            product.setDescription(description);

            List<ProductImage> images = new ArrayList<>();
            int order = 0;

            for (Part part : request.getParts()) {
                if (!"images".equals(part.getName()) || part.getSize() == 0) {
                    continue;
                }

                String type = part.getContentType();

                if (type == null || !type.startsWith("image/")) {
                    continue;
                }

                Map result = CloudinaryConfig.getInstance().uploader().upload(
                        part.getInputStream().readAllBytes(),
                        ObjectUtils.asMap("folder", "Product"));

                ProductImage img = new ProductImage();
                img.setImageUrl((String) result.get("secure_url"));
                img.setPrimary(order == 0);
                img.setSortOrder(order++);
                img.setCreatedAt(LocalDateTime.now());
                img.setProduct(product);

                images.add(img);
            }

            product.setImages(images);

            productDAO.insert(product, brandId, categoryId);

            response.sendRedirect(
                    request.getContextPath()
                            + "/admin/products?createSuccess=1");

        } catch (Exception e) {
            log("Unable to process product action: " + action, e);

            request.setAttribute(
                    "error",
                    isUpdate
                            ? "Cập nhật sản phẩm thất bại. Vui lòng kiểm tra lại dữ liệu."
                            : "Thêm sản phẩm thất bại. Vui lòng kiểm tra lại dữ liệu.");

            // Khôi phục chế độ Edit khi xảy ra lỗi để không mất productId.
            if (isUpdate) {
                Product product = ProductDB.getProductById(productId);

                if (product == null) {
                    response.sendError(
                            HttpServletResponse.SC_NOT_FOUND,
                            "Product not found");
                    return;
                }

                request.setAttribute("product", product);
                request.setAttribute("editMode", true);
            } else {
                request.setAttribute("editMode", false);
            }

            BrandDB brandDAO = new BrandDB();
            CategoryDB categoryDAO = new CategoryDB();

            request.setAttribute("brands", brandDAO.getAllBrand());
            request.setAttribute("categories", categoryDAO.getAllCategory());

            request.getRequestDispatcher(
                    "/views/admin/products-form.jsp").forward(request, response);
        }
    }

}