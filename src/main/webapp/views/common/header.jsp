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
                <i class="fa-solid fa-bolt"></i>
                <span>SPORTSHOP</span>
            </a>
        </div>

        <!-- 2. Menu điều hướng chính: Home, Categories, Brands, Hot Products, Sale -->
        <nav class="header-nav">
            <ul>
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/HomeServlet" 
                       class="${empty param.cat && empty param.brand && empty param.tag ? 'active' : ''}">Home</a>
                </li>

                <!-- Danh mục sản phẩm (Rê chuột xổ ra các môn thể thao) -->
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/HomeServlet?view=categories" 
                       class="${not empty param.cat ? 'active' : ''}">
                        Categories <i class="fa-solid fa-chevron-down nav-arrow"></i>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="${pageContext.request.contextPath}/HomeServlet?cat=running">Running & Training</a></li>
                        <li><a href="${pageContext.request.contextPath}/HomeServlet?cat=football">Football</a></li>
                        <li><a href="${pageContext.request.contextPath}/HomeServlet?cat=basketball">Basketball</a></li>
                        <li><a href="${pageContext.request.contextPath}/HomeServlet?cat=gym">Gym & Fitness</a></li>
                        <li><a href="${pageContext.request.contextPath}/HomeServlet?cat=tennis">Tennis & Badminton</a></li>
                        <li><a href="${pageContext.request.contextPath}/HomeServlet?cat=accessories">Sports Accessories</a></li>
                    </ul>
                </li>

                <!-- Thương hiệu (Rê chuột xổ ra các hãng nổi tiếng) -->
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/HomeServlet?view=brands" 
                       class="${not empty param.brand ? 'active' : ''}">
                        Brands <i class="fa-solid fa-chevron-down nav-arrow"></i>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="${pageContext.request.contextPath}/HomeServlet?brand=nike">Nike</a></li>
                        <li><a href="${pageContext.request.contextPath}/HomeServlet?brand=adidas">Adidas</a></li>
                        <li><a href="${pageContext.request.contextPath}/HomeServlet?brand=puma">Puma</a></li>
                        <li><a href="${pageContext.request.contextPath}/HomeServlet?brand=under-armour">Under Armour</a></li>
                        <li><a href="${pageContext.request.contextPath}/HomeServlet?brand=mizuno">Mizuno</a></li>
                    </ul>
                </li>

                <!-- Sản phẩm Hot -->
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/HomeServlet?tag=hot" 
                       class="${param.tag == 'hot' ? 'active' : ''}">Hot Products</a>
                </li>

                <!-- Khuyến mãi -->
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/HomeServlet?tag=sale" 
                       class="${param.tag == 'sale' ? 'active' : ''}">Sale</a>
                </li>
            </ul>
        </nav>

        <!-- 3. Các nút tiện ích bên phải (Dùng icon nhúng từ ngoài) -->
        <div class="header-actions">
            <!-- Nút Tìm kiếm -->
            <a href="${pageContext.request.contextPath}/HomeServlet?action=search" class="action-btn" title="Search products">
                <i class="fa-solid fa-magnifying-glass"></i>
            </a>

            <!-- Nút Tài khoản: Kiểm tra trạng thái đăng nhập từ Session -->
            <c:choose>
                <c:when test="${not empty sessionScope.customer || not empty sessionScope.user}">
                    <!-- Đã đăng nhập: chuyển tới trang hồ sơ cá nhân -->
                    <a href="${pageContext.request.contextPath}/views/profile.jsp" class="action-btn" title="My Profile">
                        <i class="fa-regular fa-user"></i>
                    </a>
                </c:when>
                <c:otherwise>
                    <!-- Chưa đăng nhập: chuyển tới trang đăng nhập / đăng ký -->
                    <a href="${pageContext.request.contextPath}/views/login.jsp" class="action-btn" title="Sign In / Register">
                        <i class="fa-regular fa-user"></i>
                    </a>
                </c:otherwise>
            </c:choose>
            <!-- Nút Đơn hàng -->
            <a href="${pageContext.request.contextPath}/views/orders.jsp" class="action-btn" title="Shopping Order">
                <i class="fa-solid fa-box-open"></i>
            </a>
            <!-- Nút Giỏ hàng -->
            <a href="${pageContext.request.contextPath}/views/cart.jsp" class="action-btn" title="Shopping Cart">
                <i class="fa-solid fa-bag-shopping"></i>
                <!-- Huy hiệu hiển thị số lượng sản phẩm thực tế trong giỏ -->
                <span class="cart-badge">${not empty sessionScope.cartCount ? sessionScope.cartCount : '0'}</span>
            </a>
        </div>

    </div>
</header>
