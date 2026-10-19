
package com.mycompany.sportshopweb.controller;

import com.cloudinary.utils.ObjectUtils;
import com.mycompany.sportshopweb.model.Product;
import com.mycompany.sportshopweb.model.ProductImage;
import com.mycompany.sportshopweb.dao.BrandDAO;
import com.mycompany.sportshopweb.dao.ProductDAO;
import com.mycompany.sportshopweb.util.CategoryDB;
import com.mycompany.sportshopweb.util.CloudinaryConfig;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet(name = "ProductActionServlet", urlPatterns = { "/ProductActionServlet" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024, maxRequestSize = 25 * 1024 * 1024)
public class ProductActionServlet extends HttpServlet {

    @Override
    public void init() {
        ProductDAO productDAO = new ProductDAO();
        getServletContext().setAttribute("ProductDAO", productDAO);
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        prepareFormData(request);

        String action = request.getParameter("action");

        if ("edit".equals(action)) {
            String idStr = request.getParameter("id");

            try {
                int productId = Integer.parseInt(idStr);

                if (productId <= 0) {
                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid product ID");
                    return;
                }

                Product product = ProductDAO.getProductById(productId);

                if (product == null) {
                    response.sendError(
                            HttpServletResponse.SC_NOT_FOUND,
                            "Product not found");
                    return;
                }

                request.setAttribute("product", product);
                request.setAttribute("editMode", true);

            } catch (NumberFormatException e) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid product ID");
                return;
            }

        } else {
            // Create mode: số lượng mặc định là 10.
            Product product = new Product();
            product.setQuantity(10);

            request.setAttribute("product", product);
            request.setAttribute("editMode", false);
        }

        request.getRequestDispatcher(
                "/views/admin/products-form.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        boolean isUpdate = "update".equals(action);
        boolean isCreate = "create".equals(action);

        if (!isUpdate && !isCreate) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid product action");
            return;
        }

        String name = request.getParameter("productName");
        String description = request.getParameter("description");
        String brandStr = request.getParameter("brandId");
        String categoryStr = request.getParameter("categoryId");
        String quantityStr = request.getParameter("quantity");

        int productId = 0;
        int brandId;
        int categoryId;
        int quantity;

        // Validate thông tin bắt buộc.
        if (name == null || name.isBlank()
                || brandStr == null || brandStr.isBlank()
                || categoryStr == null || categoryStr.isBlank()
                || quantityStr == null || quantityStr.isBlank()) {

            request.setAttribute(
                    "error",
                    "Vui lòng nhập đầy đủ thông tin, bao gồm số lượng.");

            showFormAfterError(request, response, isUpdate, productId);
            return;
        }

        // Validate số lượng, Brand, Category và Product ID.
        try {
            brandId = Integer.parseInt(brandStr);
            categoryId = Integer.parseInt(categoryStr);
            quantity = Integer.parseInt(quantityStr.trim());

            if (brandId <= 0 || categoryId <= 0) {
                throw new NumberFormatException(
                        "Invalid brand/category ID");
            }

            if (quantity < 0) {
                request.setAttribute(
                        "error",
                        "Số lượng sản phẩm không được âm.");

                showFormAfterError(request, response, isUpdate, productId);
                return;
            }

            if (isUpdate) {
                String productIdStr = request.getParameter("productId");

                if (productIdStr == null || productIdStr.isBlank()) {
                    throw new NumberFormatException(
                            "Missing product ID");
                }

                productId = Integer.parseInt(productIdStr);

                if (productId <= 0) {
                    throw new NumberFormatException(
                            "Invalid product ID");
                }
            }

        } catch (NumberFormatException e) {
            request.setAttribute(
                    "error",
                    "ID sản phẩm, thương hiệu, danh mục hoặc số lượng không hợp lệ.");

            showFormAfterError(request, response, isUpdate, productId);
            return;
        }

        ProductDAO productDAO = (ProductDAO) getServletContext().getAttribute("ProductDAO");

        if (productDAO == null) {
            productDAO = new ProductDAO();
            getServletContext().setAttribute("ProductDAO", productDAO);
        }

        try {
            if (isUpdate) {
                // UPDATE: cập nhật thông tin và quantity.
                boolean updated = productDAO.updateProduct(
                        productId,
                        name.trim(),
                        description,
                        brandId,
                        categoryId,
                        quantity);

                if (!updated) {
                    response.sendError(
                            HttpServletResponse.SC_NOT_FOUND,
                            "Product not found");
                    return;
                }

                response.sendRedirect(
                        request.getContextPath()
                                + "/admin/products?updateSuccess=1");
                return;
            }

            // CREATE: tạo sản phẩm mới.
            Product product = new Product();
            product.setProductName(name.trim());
            product.setDescription(description);
            product.setQuantity(quantity);

            // Giữ nguyên logic upload ảnh Cloudinary.
            List<ProductImage> images = new ArrayList<>();
            int order = 0;

            for (Part part : request.getParts()) {
                if (!"images".equals(part.getName())
                        || part.getSize() == 0) {
                    continue;
                }

                String contentType = part.getContentType();

                if (contentType == null
                        || !contentType.toLowerCase().startsWith("image/")) {
                    continue;
                }

                Map result = CloudinaryConfig.getInstance()
                        .uploader()
                        .upload(
                                part.getInputStream().readAllBytes(),
                                ObjectUtils.asMap("folder", "Product"));

                ProductImage image = new ProductImage();
                image.setImageUrl((String) result.get("secure_url"));
                image.setPrimary(order == 0);
                image.setSortOrder(order++);
                image.setCreatedAt(LocalDateTime.now());
                image.setProduct(product);

                images.add(image);
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

            showFormAfterError(request, response, isUpdate, productId);
        }
    }

    /**
     * Chuẩn bị dữ liệu dùng chung cho form.
     */
    private void prepareFormData(HttpServletRequest request) {
        BrandDAO brandDAO = new BrandDAO();
        CategoryDB categoryDAO = new CategoryDB();

        request.setAttribute("brands", brandDAO.getAllBrand());
        request.setAttribute("categories", categoryDAO.getAllCategory());
    }

    /**
     * Hiển thị lại form nếu dữ liệu nhập không hợp lệ
     * hoặc thao tác database thất bại.
     */
    private void showFormAfterError(
            HttpServletRequest request,
            HttpServletResponse response,
            boolean isUpdate,
            int productId)
            throws ServletException, IOException {

        if (isUpdate) {
            // Nếu đã có productId thì tải lại sản phẩm để giữ chế độ Edit.
            if (productId <= 0) {
                String idStr = request.getParameter("productId");

                try {
                    productId = Integer.parseInt(idStr);
                } catch (NumberFormatException e) {
                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid product ID");
                    return;
                }
            }

            Product product = ProductDAO.getProductById(productId);

            if (product == null) {
                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Product not found");
                return;
            }

            request.setAttribute("product", product);
            request.setAttribute("editMode", true);

        } else {
            Product product = new Product();
            product.setProductName(request.getParameter("productName"));
            product.setDescription(request.getParameter("description"));

            // Giữ quantity người dùng vừa nhập nếu có thể.
            String quantityStr = request.getParameter("quantity");

            try {
                int quantity = Integer.parseInt(quantityStr);
                if (quantity >= 0) {
                    product.setQuantity(quantity);
                }
            } catch (NumberFormatException | NullPointerException e) {
                product.setQuantity(10);
            }

            request.setAttribute("product", product);
            request.setAttribute("editMode", false);
        }

        prepareFormData(request);

        request.getRequestDispatcher(
                "/views/admin/products-form.jsp")
                .forward(request, response);
    }
}
