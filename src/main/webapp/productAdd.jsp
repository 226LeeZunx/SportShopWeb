<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Thêm sản phẩm</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/product-add.css">
</head>
<body>
<div class="card">
  <h2>Thêm sản phẩm</h2>

  <c:if test="${not empty message}">
    <p class="msg-ok">${message}</p>
  </c:if>
  <c:if test="${not empty error}">
    <p class="msg-err">${error}</p>
  </c:if>

  <form action="${pageContext.request.contextPath}/ProductActionServlet"
        method="post" enctype="multipart/form-data">

    <label for="productName">Tên sản phẩm</label>
    <input type="text" id="productName" name="productName" maxlength="200" required>

    <label for="description">Mô tả</label>
    <textarea id="description" name="description"></textarea>

    <label for="brandId">Thương hiệu</label>
    <select id="brandId" name="brandId" required>
      <option value="">-- Chọn thương hiệu --</option>
      <c:forEach var="b" items="${brands}">
        <option value="${b.brandId}">${b.name}</option>
      </c:forEach>
    </select>

    <label for="categoryId">Danh mục</label>
    <select id="categoryId" name="categoryId" required>
      <option value="">-- Chọn danh mục --</option>
      <c:forEach var="c" items="${categories}">
        <option value="${c.categoryId}">${c.name}</option>
      </c:forEach>
    </select>

    <label for="images">Hình ảnh (ảnh đầu tiên là ảnh chính, tối đa 5MB/ảnh)</label>
    <input type="file" id="images" name="images" accept="image/*" multiple>

    <button type="submit">Thêm sản phẩm</button>
  </form>
</div>
</body>
</html>