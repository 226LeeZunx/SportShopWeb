<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>SPORTSHOP - Sport Lookbook & Curated Outfits</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/album.css">
</head>
<body>

    <jsp:include page="/views/common/header.jsp" />

    <main class="album-container">
        
        <!-- Banner đầu trang Lookbook -->
        <div class="album-header-banner">
            <div>
                <h1 class="album-banner-title">Lookbook & Outfits</h1>
                <p class="album-banner-subtitle">
                    Explore curated professional athletic outfits curated by SPORTSHOP. 
                    Elevate your performance and style with complete coordinated sets.
                </p>
            </div>
            <div class="album-count-badge">
                <i class="fa-solid fa-layer-group"></i>
                <span>${not empty albumList ? albumList.size() : 0} Collections</span>
            </div>
        </div>

        <!-- Thông báo nếu chưa có album -->
        <c:if test="${empty albumList}">
            <div class="album-empty-state">
                <i class="fa-solid fa-shirt"></i>
                <h3>No Lookbook Collections Available</h3>
                <p>Coordinated outfit sets will be updated soon.</p>
            </div>
        </c:if>

        <!-- Lưới danh sách các Album Lookbook -->
        <ul class="album-grid">
            <c:forEach var="album" items="${albumList}">
                <li class="album-card">
                    <a href="${pageContext.request.contextPath}/AlbumServlet?id=${album.albumId}" class="album-card-link">
                        <div class="album-preview-thumb">
                            <c:choose>
                                <c:when test="${not empty album.imageUrl}">
                                    <img src="${album.imageUrl}" alt="${album.name}" loading="lazy">
                                </c:when>
                                <c:when test="${not empty album.items && not empty album.items[0].product && not empty album.items[0].product.images}">
                                    <img src="${album.items[0].product.images[0].imageUrl}" alt="${album.name}" loading="lazy">
                                </c:when>
                                <c:otherwise>
                                    <div class="album-empty-thumb"></div>
                                </c:otherwise>
                            </c:choose>
                            <span class="album-badge-tag">
                                <i class="fa-solid fa-shirt"></i>
                                ${not empty album.items ? album.items.size() : 0} Items in set
                            </span>
                        </div>
                        <div class="album-info">
                            <div>
                                <h2 class="album-title">${album.name}</h2>
                                <p class="album-desc">${album.description}</p>
                            </div>
                            <div class="album-footer-link">
                                <span>Explore complete outfit</span>
                                <i class="fa-solid fa-arrow-right"></i>
                            </div>
                        </div>
                    </a>
                </li>
            </c:forEach>
        </ul>

    </main>

    <jsp:include page="/views/common/footer.jsp" />

</body>
</html>
