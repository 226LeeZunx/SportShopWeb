<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>SPORTSHOP - Authentic Football Gear & Kits</title>
    <!-- Nhúng file CSS dùng chung -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/common.css">
</head>
<body>

    <!-- 1. Nhúng Header dùng chung (theo chuẩn JSP Chapter 6) -->
    <jsp:include page="/views/common/header.jsp" />

    <!-- 2. Phần nội dung chính: Bảng danh sách sản phẩm -->
    <main style="max-width: 1300px; margin: 40px auto; padding: 0 24px; min-height: 50vh;">
        <h2 style="margin-bottom: 20px; font-size: 1.5rem; color: var(--text-primary);">Product Catalog</h2>
        
        <table style="width: 100%; border-collapse: collapse; background-color: var(--bg-card); border: 1px solid var(--border-color); border-radius: 8px; overflow: hidden;">
            <thead>
                <tr style="border-bottom: 1px solid var(--border-color); text-align: left; background-color: #1a1a1a;">
                    <th style="padding: 14px 16px; color: var(--text-primary);">Product ID</th>
                    <th style="padding: 14px 16px; color: var(--text-primary);">Product Name</th>
                    <th style="padding: 14px 16px; color: var(--text-primary);">Description</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="product" items="${productList}">
                    <tr style="border-bottom: 1px solid var(--border-color);">
                        <td style="padding: 12px 16px;">${product.productId}</td>
                        <td style="padding: 12px 16px; font-weight: 600; color: var(--accent-lime);">${product.productName}</td>
                        <td style="padding: 12px 16px; color: var(--text-muted);">${product.description}</td>
                    </tr>
                </c:forEach>
                <c:if test="${empty productList}">
                    <tr>
                        <td colspan="3" style="padding: 32px; text-align: center; color: var(--text-muted);">
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