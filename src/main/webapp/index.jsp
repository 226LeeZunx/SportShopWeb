<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Tự động chuyển hướng vào HomeServlet để lấy dữ liệu sản phẩm từ DB
    response.sendRedirect(request.getContextPath() + "/HomeServlet");
%>
