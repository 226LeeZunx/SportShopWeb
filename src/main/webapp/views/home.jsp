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

    <main class="home-main-container">
        <h2 class="home-section-title">Product Catalog</h2>

        <c:if test="${empty productList}">
            <p>No products found in the database.</p>
        </c:if>

        <ul class="product-grid">
            <c:forEach var="product" items="${productList}">
                <li>
                    <c:choose>
                        <c:when test="${empty product.images}">
                            <img src="${pageContext.request.contextPath}/assets/images/no-image.png"
                                 alt="Chưa có ảnh">
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="img" items="${product.images}">
                                <c:if test="${img.primary}">
                                    <img src="${img.imageUrl}" alt="${product.productName}">
                                </c:if>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    <h3>${product.productName}</h3>
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