<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>SPORTSHOP - Premium Athletic Footwear & Sportswear</title>
    <!-- Nhúng file CSS dùng chung -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/common.css">
</head>
<body>

    <!-- 1. Nhúng Header dùng chung (theo chuẩn JSP Chapter 6) -->
    <jsp:include page="/views/common/header.jsp" />

    <!-- 2. Phần nội dung chính: Bảng danh sách sản phẩm -->
    <main class="home-main-container">
        <h2 class="home-section-title">Product Catalog</h2>
        
        <table class="data-table">
            <thead>
                <tr>
                    <th>Product ID</th>
                    <th>Product Name</th>
                    <th>Description</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="product" items="${productList}">
                    <tr>
                        <td>${product.productId}</td>
                        <td class="cell-highlight">${product.productName}</td>
                        <td class="cell-muted">${product.description}</td>
                    </tr>
                </c:forEach>
                <c:if test="${empty productList}">
                    <tr>
                        <td colspan="3" class="empty-cell">
                            No products found in the database.
                        </td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </main>

    <!-- 3. Nhúng Footer dùng chung (theo chuẩn JSP Chapter 6) -->
    <jsp:include page="/views/common/footer.jsp" />

</body>
</html>