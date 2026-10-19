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

@WebServlet(name = "AlbumServlet", urlPatterns = {"/AlbumServlet", "/albums", "/lookbook"})
public class AlbumServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");

        if (idParam != null && !idParam.trim().isEmpty()) {
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
            response.sendRedirect(request.getContextPath() + "/AlbumServlet");
            return;
        }

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
