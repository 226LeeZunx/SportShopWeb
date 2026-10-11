<form action="${pageContext.request.contextPath}/admin/product-add"
      method="post" enctype="multipart/form-data">

  <input type="text" name="productName" placeholder="Tên s?n ph?m" required>
  <textarea name="description" placeholder="Mô t?"></textarea>

  <select name="brandId" required>
    <c:forEach var="b" items="${brands}">
      <option value="${b.brandId}">${b.brandName}</option>
    </c:forEach>
  </select>

  <select name="categoryId" required>
    <c:forEach var="c" items="${categories}">
      <option value="${c.categoryId}">${c.categoryName}</option>
    </c:forEach>
  </select>

  <input type="file" name="images" accept="image/*" multiple>

  <p style="color:red">${error}</p>
  <button type="submit">Thêm s?n ph?m</button>
</form>