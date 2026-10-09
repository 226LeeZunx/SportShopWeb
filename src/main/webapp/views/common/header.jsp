<%-- 
    Document   : header
    Created on : 9 Oct 2026, 11:44:38 AM
    Author     : Anh Tuan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Khung Header dùng chung - Cửa hàng thể thao bóng đá -->
<header class="site-header">
    <div class="header-container">
        
        <!-- 1. Logo thương hiệu bên trái -->
        <div class="header-logo">
            <a href="${pageContext.request.contextPath}/HomeServlet">
                <span class="logo-icon">
                    <span class="logo-bar"></span>
                    <span class="logo-bar"></span>
                    <span class="logo-bar"></span>
                </span>
                <span>SPORTSHOP</span>
            </a>
        </div>

        <!-- 2. Menu điều hướng (Tự động gạch chân active theo tham số URL) -->
        <nav class="header-nav">
            <ul>
                <li>
                    <a href="${pageContext.request.contextPath}/HomeServlet" 
                       class="${empty param.cat ? 'active' : ''}">Home</a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/HomeServlet?cat=jerseys" 
                       class="${param.cat == 'jerseys' ? 'active' : ''}">Jerseys</a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/HomeServlet?cat=boots" 
                       class="${param.cat == 'boots' ? 'active' : ''}">Boots</a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/HomeServlet?cat=accessories" 
                       class="${param.cat == 'accessories' ? 'active' : ''}">Accessories</a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/HomeServlet?cat=sale" 
                       class="${param.cat == 'sale' ? 'active' : ''}">Sale</a>
                </li>
            </ul>
        </nav>

        <!-- 3. Các nút tiện ích bên phải (Tìm kiếm, Tài khoản, Giỏ hàng) -->
        <div class="header-actions">
            <!-- Nút Tìm kiếm -->
            <a href="${pageContext.request.contextPath}/HomeServlet?action=search" class="action-btn" title="Search products">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <circle cx="11" cy="11" r="8"></circle>
                    <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
                </svg>
            </a>

            <!-- Nút Tài khoản: Kiểm tra trạng thái đăng nhập từ Session -->
            <c:choose>
                <c:when test="${not empty sessionScope.customer || not empty sessionScope.user}">
                    <!-- Đã đăng nhập: chuyển tới trang hồ sơ cá nhân -->
                    <a href="${pageContext.request.contextPath}/views/profile.jsp" class="action-btn" title="My Profile">
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                            <circle cx="12" cy="7" r="4"></circle>
                        </svg>
                    </a>
                </c:when>
                <c:otherwise>
                    <!-- Chưa đăng nhập: chuyển tới trang đăng nhập / đăng ký -->
                    <a href="${pageContext.request.contextPath}/views/login.jsp" class="action-btn" title="Sign In / Register">
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                            <circle cx="12" cy="7" r="4"></circle>
                        </svg>
                    </a>
                </c:otherwise>
            </c:choose>

            <!-- Nút Giỏ hàng -->
            <a href="${pageContext.request.contextPath}/views/cart.jsp" class="action-btn" title="Shopping Cart">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M6 2L3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-2z"></path>
                    <line x1="3" y1="6" x2="21" y2="6"></line>
                    <path d="M16 10a4 4 0 0 1-8 0"></path>
                </svg>
                <!-- Huy hiệu hiển thị số lượng sản phẩm thực tế trong giỏ -->
                <span class="cart-badge">${not empty sessionScope.cartCount ? sessionScope.cartCount : '0'}</span>
            </a>
        </div>

    </div>
</header>
