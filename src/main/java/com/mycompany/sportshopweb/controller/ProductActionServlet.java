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

@WebServlet(name = "ProductActionServlet", urlPatterns = {"/ProductActionServlet"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize = 5 * 1024 * 1024,       
    maxRequestSize = 25 * 1024 * 1024
)
public class ProductActionServlet extends HttpServlet {
    @Override
    public void init(){
        ProductDB productDAO=new ProductDB();
        getServletContext().setAttribute("ProductDAO", productDAO);

    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        BrandDB brandDAO=new BrandDB();
        CategoryDB categoryDAO=new CategoryDB();
        request.setAttribute("brands", brandDAO.getAllBrand());
        request.setAttribute("categories", categoryDAO.getAllCategory());
        request.getRequestDispatcher("/productAdd.jsp").forward(request, response);

    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String name=request.getParameter("productName");
        String description=request.getParameter("description");
        String brandStr=request.getParameter("brandId");
        String categoryStr=request.getParameter("categoryId");
        HttpSession session = request.getSession();
        ProductDB productDAO = (ProductDB) getServletContext().getAttribute("ProductDAO");

        if (productDAO == null) {
           productDAO = new ProductDB();
           getServletContext().setAttribute("ProductDAO", productDAO);
        }
        if (name == null || name.isBlank() || brandStr == null || categoryStr == null) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin.");
            doGet(request, response);
            return;
        }
        try{
            Product product =new Product();
            product.setProductName(name.trim());
            List<ProductImage> images=new ArrayList<>();
            int order = 0;
            for(Part part:request.getParts()){
                if (!"images".equals(part.getName()) || part.getSize() == 0) continue;
                String  type =part.getContentType();
                if (type == null || !type.startsWith("image/")) continue;
                
                Map result = CloudinaryConfig.getInstance().uploader().upload(
                          part.getInputStream().readAllBytes(),
                          ObjectUtils.asMap("folder", "Product"));
                ProductImage img =new ProductImage();
                img.setImageUrl((String) result.get("secure_url"));
                img.setPrimary(order==0);
                img.setSortOrder(order++);
                img.setCreatedAt(LocalDateTime.now());
                img.setProduct(product); 
                images.add(img);
            }
        product.setImages(images);
        productDAO.insert(product, Integer.parseInt(brandStr), Integer.parseInt(categoryStr));
        response.sendRedirect(request.getContextPath() + "/ProductActionServlet?success=1");
        }catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Thêm sản phẩm thất bại: " + e.getMessage());
            doGet(request, response);
        }
    }
}