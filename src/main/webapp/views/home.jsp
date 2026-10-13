<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>SPORTSHOP - Premium Athletic Footwear & Sportswear</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/home.css">
</head>
<body>

    <jsp:include page="/views/common/header.jsp" />

    <!-- 1. KHUNG BANNER HÌNH ẢNH HERO (Hỗ trợ 2 ảnh slide - 100% HTML & CSS thuần, không dùng script) -->
    <section class="hero-banner-section">
        
        <!-- Bộ chọn Slide bằng Radio Input ẩn (Thuần CSS, chuyển ảnh khi click) -->
        <input type="radio" name="hero-slide" id="slide-1" class="hero-radio" checked>
        <input type="radio" name="hero-slide" id="slide-2" class="hero-radio">

        <!-- Dải trượt chứa 2 ảnh Banner -->
        <div class="hero-slider-track">
            
            <!-- SLIDE 1: FC BARCELONA -->
            <div class="hero-slide">
                <!-- Ảnh 1 do bạn tải vào thư mục assets/images/hero-banner-1.jpg -->
                <img src="${pageContext.request.contextPath}/assets/images/hero-banner-1.jpg" 
                     alt="FC Barcelona Celebration" 
                     class="hero-banner-image">

                <div class="hero-banner-content">
                    <h1 class="hero-banner-title">Push Your Limits</h1>
                    <a href="${pageContext.request.contextPath}/HomeServlet?cat=football" class="hero-banner-btn">
                        SHOP NOW
                    </a>
                </div>

                <!-- Nút mũi tên chuyển sang ảnh 2 -->
                <label for="slide-2" class="hero-nav-arrow hero-nav-next" title="Next banner">
                    <i class="fa-solid fa-chevron-right"></i>
                </label>
            </div>

            <!-- SLIDE 2: MESSI THE GOAT -->
            <div class="hero-slide">
                <!-- Ảnh 2 do bạn tải vào thư mục assets/images/hero-banner-2.jpg -->
                <img src="${pageContext.request.contextPath}/assets/images/hero-banner-2.jpg" 
                     alt="Messi The GOAT" 
                     class="hero-banner-image">

                <div class="hero-banner-content">
                    <h1 class="hero-banner-title">Elevate Your Game</h1>
                    <a href="${pageContext.request.contextPath}/HomeServlet?tag=hot" class="hero-banner-btn">
                        EXPLORE NOW
                    </a>
                </div>

                <!-- Nút mũi tên quay về ảnh 1 -->
                <label for="slide-1" class="hero-nav-arrow hero-nav-prev" title="Previous banner">
                    <i class="fa-solid fa-chevron-left"></i>
                </label>
            </div>

        </div>

        <!-- Hai chấm chỉ báo góc dưới bên phải (Bấm vào cũng đổi ảnh thuần CSS) -->
        <div class="hero-indicators">
            <label for="slide-1" class="indicator-dot dot-1" title="Banner 1"></label>
            <label for="slide-2" class="indicator-dot dot-2" title="Banner 2"></label>
        </div>

        <!-- 2. THANH CAM KẾT / ƯU ĐÃI DƯỚI BANNER (Trust Badge Bar) -->
        <div class="hero-trust-bar">
            <div class="trust-item">
                <i class="fa-solid fa-crown"></i>
                <span>Members get 10% off</span>
            </div>
            <div class="trust-item">
                <i class="fa-solid fa-shield-halved"></i>
                <span>Buy direct and support your club</span>
            </div>
            <div class="trust-item">
                <i class="fa-solid fa-award"></i>
                <span>100% Authentic Guarantee</span>
            </div>
        </div>
    </section>

    <!-- 3. PHẦN DANH SÁCH SẢN PHẨM BÊN DƯỚI -->
    <main class="home-main-container">
        <h2 class="home-section-title">Product Catalog</h2>

        <c:if test="${empty productList}">
            <div class="empty-catalog">
                <i class="fa-solid fa-box-open"></i>
                <p>No products found in the catalog.</p>
            </div>
        </c:if>

        <ul class="product-grid">
            <c:forEach var="product" items="${productList}">
                <li class="product-card">
                    <a href="${pageContext.request.contextPath}/ProductDetailServlet?id=${product.productId}" class="product-card-link">
                        <div class="product-thumb">
                            <c:choose>
                                <c:when test="${empty product.images}">
                                    <div class="no-thumb-placeholder">
                                        <i class="fa-regular fa-image"></i>
                                        <span>No image</span>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="foundPrimary" value="false" />
                                    <c:forEach var="img" items="${product.images}">
                                        <c:if test="${img.primary && !foundPrimary}">
                                            <img src="${img.imageUrl}" alt="${product.productName}" loading="lazy">
                                            <c:set var="foundPrimary" value="true" />
                                        </c:if>
                                    </c:forEach>
                                    <c:if test="${!foundPrimary}">
                                        <img src="${product.images[0].imageUrl}" alt="${product.productName}" loading="lazy">
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="product-info">
                            <h3 class="product-name" title="${product.productName}">${product.productName}</h3>
                        </div>
                    </a>
                </li>
            </c:forEach>
        </ul>

        <c:if test="${totalPages > 1}">
            <nav class="pagination">
                <c:if test="${currentPage > 1}">
                    <a href="?page=${currentPage - 1}">&laquo; Trước</a>
                </c:if>

                <c:forEach var="i" begin="1" end="${totalPages}">
                    <a href="?page=${i}" class="${i == currentPage ? 'active' : ''}">${i}</a>
                </c:forEach>

                <c:if test="${currentPage < totalPages}">
                    <a href="?page=${currentPage + 1}">Sau &raquo;</a>
                </c:if>
            </nav>
        </c:if>
    </main>

    <jsp:include page="/views/common/footer.jsp" />

</body>
</html>