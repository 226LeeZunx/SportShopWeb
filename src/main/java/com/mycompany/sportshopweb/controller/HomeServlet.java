/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java 
 */
package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.AlbumDAO;
import com.mycompany.sportshopweb.model.Album;
import com.mycompany.sportshopweb.model.Brand;
import com.mycompany.sportshopweb.model.Category;
import com.mycompany.sportshopweb.model.Product;
import com.mycompany.sportshopweb.util.BrandDB;
import com.mycompany.sportshopweb.util.CategoryDB;
import com.mycompany.sportshopweb.util.ProductDB;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author dung2
 */
@WebServlet(name = "HomeServlet", urlPatterns = {"/HomeServlet"})
public class HomeServlet extends HttpServlet {
    
    @Override
    public void init(){
        ProductDB productDAO = new ProductDB();
        getServletContext().setAttribute("ProductDAO", productDAO);
        try {
            List<Album> albums = AlbumDAO.getAllAlbums();
            getServletContext().setAttribute("globalAlbumList", albums);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        request.setAttribute("categoryList", CategoryDB.getAllCategory());

        // Nạp danh sách Album (Lookbook) để hiển thị lên Side Panel
        try {
            List<Album> albumList = AlbumDAO.getAllAlbums();
            request.setAttribute("albumList", albumList);
            getServletContext().setAttribute("globalAlbumList", albumList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    int page = 1;
    try {
        page = Integer.parseInt(request.getParameter("page"));
    } catch (NumberFormatException e) {
    }

    long total = ProductDB.countProducts();
    int totalPages = (int) Math.ceil((double) total / ProductDB.PAGE_SIZE);
    if (totalPages == 0) totalPages = 1;
    if (page < 1) page = 1;
    if (page > totalPages) page = totalPages;

    request.setAttribute("productList", ProductDB.getProductsByPage(page, ProductDB.PAGE_SIZE));
    request.setAttribute("currentPage", page);
    request.setAttribute("totalPages", totalPages);

    request.getRequestDispatcher("/views/home.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }

    
}
