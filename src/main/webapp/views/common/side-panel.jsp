<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>

<!-- Lớp nền tối mờ khi mở panel -->
<div class="drawer-overlay" id="drawerOverlay" onclick="closeDrawer()"></div>

<!-- Cửa sổ trượt danh mục -->
<aside class="drawer-menu" id="drawerMenu">
    
    <!-- Tiêu đề & nút đóng -->
    <div class="drawer-header">
        <div class="drawer-logo">
            <i class="fa-solid fa-bolt"></i>
            <span>SPORTSHOP</span>
        </div>
        <button type="button" class="drawer-close-btn" onclick="closeDrawer()">
            <i class="fa-solid fa-xmark"></i>
        </button>
    </div>

    <!-- Nội dung danh mục cuộn -->
    <div class="drawer-body">
        
        <!-- Tab danh mục trên cùng -->
        <div class="drawer-tabs">
            <a href="${pageContext.request.contextPath}/AlbumServlet" class="drawer-tab">LOOKBOOK</a>
            <c:forEach var="cat" items="${categoryList}">
                <a href="${pageContext.request.contextPath}/HomeServlet?cat=${cat.categoryId}" class="drawer-tab">
                    ${cat.name}
                </a>
            </c:forEach>
            <a href="${pageContext.request.contextPath}/HomeServlet?tag=sale" class="drawer-tab">SALE</a>
        </div>

        <!-- Các Album / Lookbook (Hình hiện tại để trống) -->
        <div class="drawer-cards-grid">
            <c:forEach var="alb" items="${albumList}">
                <a href="${pageContext.request.contextPath}/AlbumServlet?id=${alb.albumId}" class="drawer-card-item">
                    <div class="drawer-card-thumb empty-card-thumb"></div>
                    <span>${alb.name}</span>
                </a>
            </c:forEach>
            <a href="${pageContext.request.contextPath}/AlbumServlet" class="drawer-card-item">
                <div class="drawer-card-thumb empty-card-thumb"></div>
                <span>ALL LOOKBOOK</span>
            </a>
        </div>

        <!-- |01| BỘ SƯU TẬP (LOOKBOOK) -->
        <div class="drawer-section">
            <div class="drawer-section-heading">
                <span class="drawer-idx">|01|</span>
                <span class="drawer-section-title">COLLECTIONS & LOOKBOOK</span>
            </div>
            <ul class="drawer-link-list">
                <li><a href="${pageContext.request.contextPath}/AlbumServlet">ALL COLLECTIONS</a></li>
                <c:forEach var="alb" items="${albumList}">
                    <li><a href="${pageContext.request.contextPath}/AlbumServlet?id=${alb.albumId}">${alb.name}</a></li>
                </c:forEach>
            </ul>
        </div>

        <!-- |02| CATEGORIES -->
        <div class="drawer-section">
            <div class="drawer-section-heading">
                <span class="drawer-idx">|02|</span>
                <span class="drawer-section-title">CATEGORIES</span>
            </div>
            <ul class="drawer-link-list">
                <c:forEach var="cat" items="${categoryList}">
                    <li><a href="${pageContext.request.contextPath}/HomeServlet?cat=${cat.categoryId}">${cat.name}</a></li>
                </c:forEach>
            </ul>
        </div>

        <!-- |03| BRANDS -->
        <div class="drawer-section">
            <div class="drawer-section-heading">
                <span class="drawer-idx">|03|</span>
                <span class="drawer-section-title">BRANDS</span>
            </div>
            <ul class="drawer-link-list">
                <c:forEach var="brand" items="${brandList}">
                    <li><a href="${pageContext.request.contextPath}/HomeServlet?brand=${brand.brandId}">${brand.name}</a></li>
                </c:forEach>
            </ul>
        </div>

        <!-- |04| EXPLORE -->
        <div class="drawer-section">
            <div class="drawer-section-heading">
                <span class="drawer-idx">|04|</span>
                <span class="drawer-section-title">EXPLORE</span>
            </div>
            <ul class="drawer-link-list">
                <li><a href="${pageContext.request.contextPath}/HomeServlet">HOME PAGE</a></li>
                <li><a href="${pageContext.request.contextPath}/HomeServlet?tag=hot">HOT PRODUCTS</a></li>
                <li><a href="${pageContext.request.contextPath}/HomeServlet?tag=sale">SALE & OFFERS</a></li>
            </ul>
        </div>

    </div>
</aside>

<!-- Javascript đóng / mở đơn giản -->
<script>
    function openDrawer() {
        document.getElementById('drawerMenu').classList.add('active');
        document.getElementById('drawerOverlay').classList.add('active');
        document.body.style.overflow = 'hidden';
    }

    function closeDrawer() {
        document.getElementById('drawerMenu').classList.remove('active');
        document.getElementById('drawerOverlay').classList.remove('active');
        document.body.style.overflow = '';
    }

    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            closeDrawer();
        }
    });
</script>
