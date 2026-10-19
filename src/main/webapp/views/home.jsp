<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>SportShop</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/common.css?v=3">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/home.css?v=4">
</head>
<body>

    <jsp:include page="/views/common/header.jsp" />

    <main class="home-main-container">

        <!-- 1. SHOP BY BRAND -->
        <section class="home-section">
            <h2 class="home-section-title">Shop by Brand</h2>
            <p class="home-section-subtitle">Official gear from the brands you trust</p>
            
            <c:if test="${empty brandList}">
                <div class="empty-catalog"><p>No brands found.</p></div>
            </c:if>

            <ul class="brand-grid">
                <c:forEach var="item" items="${brandList}">
                    <li class="brand-card ${selectedBrand == item.brandId ? 'active' : ''}">
                        <a href="${pageContext.request.contextPath}/HomeServlet?brand=${item.brandId}#products">
                            <c:choose>
                                <c:when test="${not empty item.logoUrl}">
                                    <img src="${item.logoUrl}" alt="${item.name}" loading="lazy">
                                </c:when>
                                <c:otherwise>
                                    <span class="brand-text">${item.name}</span>
                                </c:otherwise>
                            </c:choose>
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </section>

        <!-- 2. LOOKBOOK COLLECTIONS / ALBUMS (Lấy tối đa 8 album) -->
        <section class="home-section">
            <h2 class="home-section-title">Lookbook Collections</h2>
            <p class="home-section-subtitle">Curated outfits — shop the whole look</p>

            <c:if test="${empty albumList}">
                <div class="empty-catalog"><p>No albums yet.</p></div>
            </c:if>

            <ul class="album-grid">
                <c:forEach var="album" items="${albumList}">
                    <li class="album-card">
                        <a href="${pageContext.request.contextPath}/AlbumServlet?id=${album.albumId}" title="${album.name}">
                            <div class="album-card-thumb">
                                <c:choose>
                                    <c:when test="${not empty album.imageUrl}">
                                        <img src="${album.imageUrl}" alt="${album.name}" loading="lazy">
                                    </c:when>
                                    <c:otherwise>
                                        <div class="no-thumb-placeholder">
                                            <i class="fa-solid fa-shirt"></i><span>LOOKBOOK</span>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="album-card-info">
                                <h3>${album.name}</h3>
                                <span class="album-card-btn">SHOP THE LOOK <i class="fa-solid fa-arrow-right"></i></span>
                            </div>
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </section>

        <!-- 3. SHOP BY CATEGORY -->
        <section class="home-section" id="categories">
    <div class="section-header-flex">
        <div>
            <h2 class="home-section-title">Shop by Category</h2>
            <p class="home-section-subtitle">Gear up for greatness with high-performance sportswear and equipment</p>
        </div>
        <!-- Nút điều hướng Slide -->
        <c:if test="${not empty categoryList and categoryList.size() > 6}">
            <div class="category-slider-controls">
                <button type="button" class="cat-nav-btn cat-prev" id="catPrevBtn" disabled aria-label="Previous">
                    <i class="fa-solid fa-chevron-left"></i>
                </button>
                <span class="cat-page-indicator" id="catPageIndicator">1 / 1</span>
                <button type="button" class="cat-nav-btn cat-next" id="catNextBtn" aria-label="Next">
                    <i class="fa-solid fa-chevron-right"></i>
                </button>
            </div>
        </c:if>
    </div>

    <c:if test="${empty categoryList}">
        <div class="empty-catalog"><p>No categories found.</p></div>
    </c:if>

    <div class="category-slider-wrapper">
        <ul class="category-grid" id="categoryGrid">
            <c:forEach var="cat" items="${categoryList}">
                <li class="category-card">
                    <a href="${pageContext.request.contextPath}/HomeServlet?cat=${cat.categoryId}#products" class="category-card-link">
                        <div class="category-thumb">
                            <c:choose>
                                <c:when test="${not empty cat.imageUrl}">
                                    <img src="${cat.imageUrl}" alt="${cat.name}" loading="lazy">
                                </c:when>
                                <c:otherwise>
                                    <div class="no-thumb-placeholder">
                                        <i class="fa-regular fa-image"></i><span>No image</span>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                            <div class="category-badge">
                                <span>Explore</span><i class="fa-solid fa-arrow-right"></i>
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
    </div>
</section>

        <!-- 4. ALL PRODUCTS + FILTER (20 sản phẩm/trang) -->
        <section class="home-section" id="products">
            <h2 class="home-section-title">All Products</h2>
            <form class="filter-bar" method="get" action="${pageContext.request.contextPath}/HomeServlet#products">
                <div class="filter-field">
                <label for="fKeyword">Search name</label>
                <input type="text" name="keyword" id="fKeyword" value="${keyword}" placeholder="Product name...">
                </div>
                <div class="filter-field">
                    <label for="fBrand">Brand</label>
                    <select name="brand" id="fBrand">
                        <option value="">All brands</option>
                        <c:forEach var="b" items="${brandList}">
                            <option value="${b.brandId}" ${selectedBrand == b.brandId ? 'selected' : ''}>${b.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="filter-field">
                    <label for="fCat">Category</label>
                    <select name="cat" id="fCat">
                        <option value="">All categories</option>
                        <c:forEach var="cat" items="${categoryList}">
                            <option value="${cat.categoryId}" ${selectedCat == cat.categoryId ? 'selected' : ''}>${cat.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="filter-field">
                    <label for="fMin">Price from</label>
                    <input type="number" name="minPrice" id="fMin" min="0" step="any" value="${minPrice}" placeholder="0">
                </div>

                <div class="filter-field">
                    <label for="fMax">Price to</label>
                    <input type="number" name="maxPrice" id="fMax" min="0" step="any" value="${maxPrice}" placeholder="Any">
                </div>

                <div class="filter-field">
                    <label for="fSort">Sort by</label>
                    <select name="sort" id="fSort">
                        <option value="id_asc"     ${sort == 'id_asc'     ? 'selected' : ''}>ID ↑</option>
                        <option value="id_desc"    ${sort == 'id_desc'    ? 'selected' : ''}>ID ↓ (newest)</option>
                        <option value="price_asc"  ${sort == 'price_asc'  ? 'selected' : ''}>Price: low → high</option>
                        <option value="price_desc" ${sort == 'price_desc' ? 'selected' : ''}>Price: high → low</option>
                        <option value="name_asc"   ${sort == 'name_asc'   ? 'selected' : ''}>Name A → Z</option>
                    </select>
                </div>

                <div class="filter-actions">
                    <button type="submit" class="filter-btn">Apply</button>
                    <a href="${pageContext.request.contextPath}/HomeServlet#products" class="filter-reset">Reset</a>
                </div>
            </form>

            <p class="result-count">${totalProducts} product(s) found</p>

            <c:if test="${empty productList}">
                <div class="empty-catalog">
                    <i class="fa-solid fa-box-open"></i>
                    <p>No products match your filters.</p>
                </div>
            </c:if>

            <ul class="product-grid">
                <c:forEach var="p" items="${productList}">
                    <li class="product-card">
                        <a href="${pageContext.request.contextPath}/ProductDetailServlet?id=${p.productId}">
                            <div class="product-thumb">
                                <c:choose>
                                    <c:when test="${not empty p.images}">
                                        <img src="${p.images[0].imageUrl}" alt="${p.productName}" loading="lazy">
                                    </c:when>
                                    <c:otherwise>
                                        <div class="no-thumb-placeholder">
                                            <i class="fa-regular fa-image"></i><span>No image</span>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="product-info">
                                <c:if test="${not empty p.brand}">
                                    <span class="product-brand">${p.brand.name}</span>
                                </c:if>
                                <h3 class="product-name">${p.productName}</h3>
                            </div>
                        </a>
                    </li>
                </c:forEach>
            </ul>

            <!-- Pagination -->
            <c:if test="${totalPages > 1}">
                <nav class="pagination">
                    <c:if test="${currentPage > 1}">
                        <c:url var="prevUrl" value="/HomeServlet">
                            <c:param name="page" value="${currentPage - 1}"/>
                            <c:param name="keyword" value="${param.keyword}"/>
                            <c:param name="brand" value="${param.brand}"/>
                            <c:param name="cat" value="${param.cat}"/>
                            <c:param name="minPrice" value="${param.minPrice}"/>
                            <c:param name="maxPrice" value="${param.maxPrice}"/>
                            <c:param name="sort" value="${sort}"/>
                        </c:url>
                        <a href="${prevUrl}#products">&laquo; Trước</a>
                    </c:if>

                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <c:url var="pageUrl" value="/HomeServlet">
                            <c:param name="page" value="${i}"/>
                            <c:param name="keyword" value="${param.keyword}"/>
                            <c:param name="brand" value="${param.brand}"/>
                            <c:param name="cat" value="${param.cat}"/>
                            <c:param name="minPrice" value="${param.minPrice}"/>
                            <c:param name="maxPrice" value="${param.maxPrice}"/>
                            <c:param name="sort" value="${sort}"/>
                        </c:url>
                        <a href="${pageUrl}#products" class="${i == currentPage ? 'active' : ''}">${i}</a>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <c:url var="nextUrl" value="/HomeServlet">
                            <c:param name="page" value="${currentPage + 1}"/>
                            <c:param name="keyword" value="${param.keyword}"/>
                            <c:param name="brand" value="${param.brand}"/>
                            <c:param name="cat" value="${param.cat}"/>
                            <c:param name="minPrice" value="${param.minPrice}"/>
                            <c:param name="maxPrice" value="${param.maxPrice}"/>
                            <c:param name="sort" value="${sort}"/>
                        </c:url>
                        <a href="${nextUrl}#products">Sau &raquo;</a>
                    </c:if>
                </nav>
            </c:if>
        </section>
    </main>

    <jsp:include page="/views/common/footer.jsp" />

</body>
</html>