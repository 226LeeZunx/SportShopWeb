package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.AlbumDAO;
import com.mycompany.sportshopweb.model.Album;
import com.mycompany.sportshopweb.dao.BrandDAO;
import com.mycompany.sportshopweb.util.CategoryDB;
import com.mycompany.sportshopweb.dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import com.mycompany.sportshopweb.util.DBUtil;
import com.mycompany.sportshopweb.util.ParseUtils;

@WebServlet(name = "HomeServlet", urlPatterns = {"/HomeServlet"})
public class HomeServlet extends HttpServlet {

    private static final Set<String> SORTS =
            Set.of("id_asc", "id_desc", "price_asc", "price_desc", "name_asc");

    @Override
    public void init() {
        getServletContext().setAttribute("ProductDAO", new ProductDAO());
        getServletContext().setAttribute("AlbumDAO", new AlbumDAO());
        getServletContext().setAttribute("BrandDAO", new BrandDAO());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // ---- 1. Load Brands, Categories & Albums ----
        request.setAttribute("brandList", BrandDAO.getAllBrand());
        request.setAttribute("categoryList", CategoryDB.getAllCategory());
        
        try {
            List<Album> albumList = AlbumDAO.getAllAlbums();
            // Lấy tối đa 8 album thô theo yêu cầu
            if (albumList != null && albumList.size() > 8) {
                albumList = albumList.subList(0, 8);
            }
            request.setAttribute("albumList", albumList);
            getServletContext().setAttribute("globalAlbumList", albumList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ---- 2. Filter params ----
        int brandId = ParseUtils.parseIntOrDefault(request.getParameter("brand"), 0);
        int catId = ParseUtils.parseIntOrDefault(request.getParameter("cat"), 0);
        
        String minPriceStr = request.getParameter("minPrice");
        String maxPriceStr = request.getParameter("maxPrice");

        long minPrice = (minPriceStr == null || minPriceStr.trim().isEmpty()) 
                ? 0L : ParseUtils.parseLongOrDefault(minPriceStr, 0L);
                
        long maxPrice = (maxPriceStr == null || maxPriceStr.trim().isEmpty()) 
                ? Long.MAX_VALUE : ParseUtils.parseLongOrDefault(maxPriceStr, Long.MAX_VALUE);

        String sort = request.getParameter("sort");
        if (sort == null || !SORTS.contains(sort)) sort = "id_asc";

        int page = 1;
        int p = ParseUtils.parseIntOrDefault(request.getParameter("page"), 0);
        if (p != 0) page = p;

        // ---- 3. Products (20 sản phẩm mỗi trang) ----
        long total = ProductDAO.countFiltered(brandId, catId, minPrice, maxPrice);
        int totalPages = (int) Math.ceil((double) total / ProductDAO.PAGE_SIZE);
        if (totalPages == 0) totalPages = 1;
        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        request.setAttribute("productList", ProductDAO.getFiltered(brandId, catId, minPrice, maxPrice, sort, page, ProductDAO.PAGE_SIZE));
        request.setAttribute("totalProducts", total);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        request.setAttribute("selectedBrand", brandId);
        request.setAttribute("selectedCat", catId);
        request.setAttribute("sort", sort);
        
        request.setAttribute("minPrice", (minPriceStr != null && !minPriceStr.trim().isEmpty()) ? minPriceStr : "");
        request.setAttribute("maxPrice", (maxPriceStr != null && !maxPriceStr.trim().isEmpty()) ? maxPriceStr : "");

        request.getRequestDispatcher("/views/home.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}