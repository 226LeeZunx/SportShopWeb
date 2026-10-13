/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.AlbumDAO;
import com.mycompany.sportshopweb.model.Album;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controller cho tính năng Lookbook / Album phối đồ thể thao
 * @author Anh Tuan
 */
@WebServlet(name = "AlbumServlet", urlPatterns = {"/AlbumServlet", "/albums", "/lookbook"})
public class AlbumServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");

        if (idParam != null && !idParam.trim().isEmpty()) {
            // Xem chi tiết một Album phối đồ cụ thể
            try {
                int albumId = Integer.parseInt(idParam.trim());
                Album album = AlbumDAO.getAlbumById(albumId);

                if (album != null) {
                    request.setAttribute("album", album);
                    request.getRequestDispatcher("/views/album-detail.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            // Nếu không tìm thấy hoặc ID sai định dạng, quay về danh sách Album
            response.sendRedirect(request.getContextPath() + "/AlbumServlet");
            return;
        }

        // Xem danh sách tất cả các Album / Bộ sưu tập phối đồ
        List<Album> albumList = AlbumDAO.getAllAlbums();
        request.setAttribute("albumList", albumList);
        request.getRequestDispatcher("/views/album-list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
