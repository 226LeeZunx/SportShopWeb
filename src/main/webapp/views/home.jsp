<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/common.css?v=3">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/home.css?v=3">
</head>
<body>

    <jsp:include page="/views/common/header.jsp" />

    <!-- 1. PANEL HÌNH ẢNH BỘ SƯU TẬP (ALBUM HERO PANEL THAY THẾ BANNER) -->
    <section class="album-hero-section" id="albumHeroSection">
        <c:choose>
            <c:when test="${not empty albumList}">
                <c:forEach var="album" items="${albumList}" varStatus="status">
                    <div class="album-hero-slide ${status.first ? 'active' : ''}">
                        <!-- Bấm vào hình ảnh sẽ dẫn đến trang chi tiết album đó -->
                        <a href="${pageContext.request.contextPath}/AlbumServlet?id=${album.albumId}" class="album-hero-link" title="${album.name}">
                            <div class="album-hero-thumb">
                                <c:choose>
                                    <c:when test="${not empty album.imageUrl}">
                                        <img src="${album.imageUrl}" alt="${album.name}" class="album-hero-img">
                                    </c:when>
                                    <c:otherwise>
                                        <!-- Hiện tại chưa có hình nên để khung ảnh trống theo yêu cầu -->
                                        <div class="album-hero-empty-thumb">
                                            <i class="fa-solid fa-shirt"></i>
                                            <span>LOOKBOOK OUTFIT</span>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="album-hero-content">
                                <span class="album-hero-tag">
                                    <i class="fa-solid fa-layer-group"></i> LOOKBOOK COLLECTION
                                </span>
                                <h1 class="album-hero-title">${album.name}</h1>
                                <c:if test="${not empty album.description}">
                                    <p class="album-hero-desc">${album.description}</p>
                                </c:if>
                                <span class="album-hero-btn">
                                    SHOP THE LOOK <i class="fa-solid fa-arrow-right"></i>
                                </span>
                            </div>
                        </a>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <!-- Dữ liệu hiển thị mặc định khi Database chưa có Album -->
                <div class="album-hero-slide active">
                    <a href="${pageContext.request.contextPath}/AlbumServlet" class="album-hero-link">
                        <div class="album-hero-thumb">
                            <div class="album-hero-empty-thumb">
                                <i class="fa-solid fa-shirt"></i>
                                <span>LOOKBOOK OUTFIT</span>
                            </div>
                        </div>
                        <div class="album-hero-content">
                            <span class="album-hero-tag">
                                <i class="fa-solid fa-layer-group"></i> FEATURED LOOKBOOK
                            </span>
                            <h1 class="album-hero-title">LOOKBOOK & OUTFITS</h1>
                            <p class="album-hero-desc">Explore curated athletic outfits and high performance sets.</p>
                            <span class="album-hero-btn">
                                EXPLORE LOOKBOOK <i class="fa-solid fa-arrow-right"></i>
                            </span>
                        </div>
                    </a>
                </div>
            </c:otherwise>
        </c:choose>

        <!-- Nút chuyển slide nếu có từ 2 album trở lên -->
        <c:if test="${not empty albumList && albumList.size() > 1}">
            <button type="button" class="album-hero-arrow album-hero-prev" onclick="prevAlbumHeroSlide(event)" title="Previous album">
                <i class="fa-solid fa-chevron-left"></i>
            </button>
            <button type="button" class="album-hero-arrow album-hero-next" onclick="nextAlbumHeroSlide(event)" title="Next album">
                <i class="fa-solid fa-chevron-right"></i>
            </button>
        </c:if>
    </section>

    <!-- Script chuyển slide Album mượt mà -->
    <script>
        var currentAlbumIndex = 0;
        function getAlbumSlides() {
            return document.querySelectorAll('.album-hero-slide');
        }
        function showAlbumSlide(index) {
            var slides = getAlbumSlides();
            if (!slides || slides.length <= 1) return;
            for (var i = 0; i < slides.length; i++) {
                slides[i].classList.remove('active');
            }
            slides[index].classList.add('active');
        }
        function nextAlbumHeroSlide(e) {
            if (e) { e.preventDefault(); e.stopPropagation(); }
            var slides = getAlbumSlides();
            if (!slides || slides.length <= 1) return;
            currentAlbumIndex = (currentAlbumIndex + 1) % slides.length;
            showAlbumSlide(currentAlbumIndex);
        }
        function prevAlbumHeroSlide(e) {
            if (e) { e.preventDefault(); e.stopPropagation(); }
            var slides = getAlbumSlides();
            if (!slides || slides.length <= 1) return;
            currentAlbumIndex = (currentAlbumIndex - 1 + slides.length) % slides.length;
            showAlbumSlide(currentAlbumIndex);
        }
    </script>

    <!-- 2. THANH CAM KẾT / ƯU ĐÃI (Trust Badge Bar) -->
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

    <!-- 3. PHẦN DANH MỤC THỂ THAO TRANG CHỦ (SHOP BY CATEGORY) -->
    <main class="home-main-container">
        <h2 class="home-section-title">Shop by Category</h2>
        <p class="home-section-subtitle">Gear up for greatness with high-performance sportswear and equipment</p>

        <c:if test="${empty categoryList}">
            <div class="empty-catalog">
                <i class="fa-solid fa-layer-group"></i>
                <p>No categories found in the database.</p>
            </div>
        </c:if>

        <ul class="category-grid">
            <c:forEach var="cat" items="${categoryList}">
                <li class="category-card">
                    <a href="${pageContext.request.contextPath}/HomeServlet?cat=${cat.categoryId}" class="category-card-link">
                        <div class="category-thumb">
                            <c:choose>
                                <c:when test="${not empty cat.imageUrl}">
                                    <img src="${cat.imageUrl}" alt="${cat.name}" loading="lazy">
                                </c:when>
                                <c:otherwise>
                                    <div class="no-thumb-placeholder">
                                        <i class="fa-regular fa-image"></i>
                                        <span>No image</span>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                            <div class="category-badge">
                                <span>Explore</span>
                                <i class="fa-solid fa-arrow-right"></i>
                            </div>
                        </div>
                        <div class="category-info">
                            <h3 class="category-name">${cat.name}</h3>
                            <c:if test="${not empty cat.description}">
                                <p class="category-desc">${cat.description}</p>
                            </c:if>
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