
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Sổ địa chỉ</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/common.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/address.css">
   
</head>

<body>
<div class="container">

    <h2>Sổ địa chỉ của tôi</h2>
    
    
<div style="margin-bottom: 20px;">
    <a href="${pageContext.request.contextPath}/checkout"
       style="display: inline-block;
              padding: 10px 16px;
              background: #666;
              color: white;
              text-decoration: none;
              border-radius: 5px;">
        ← Quay lại thanh toán
    </a>
</div>

    <!-- Danh sách địa chỉ đã lưu -->
    <div class="card">
        <h3>Địa chỉ đã lưu</h3>

        <c:choose>
            <c:when test="${empty addresses}">
                <p>Bạn chưa lưu địa chỉ nào.</p>
            </c:when>

            <c:otherwise>
                <c:forEach var="address" items="${addresses}">

                    <div class="address">

                        <p>
                            <strong>${address.province}</strong>,
                            ${address.ward}
                        </p>

                        <p>${address.detailAddress}</p>

                        <c:if test="${address.defaultAddress}">
                            <span class="default-label">
                                Địa chỉ mặc định
                            </span>
                        </c:if>

                        <!-- Nút sửa địa chỉ -->
                        <button type="button"
                                class="secondary"
                                onclick="toggleEdit(${address.addressId})">
                            Sửa địa chỉ
                        </button>

                        <!-- Form đặt mặc định -->
                        <c:if test="${not address.defaultAddress}">
                            <form action="${pageContext.request.contextPath}/addresses"
                                  method="post"
                                  style="display:inline">

                                <input type="hidden"
                                       name="action"
                                       value="setDefault">

                                <input type="hidden"
                                       name="addressId"
                                       value="${address.addressId}">

                                <button type="submit" class="primary">
                                    Đặt làm mặc định
                                </button>
                            </form>
                        </c:if>

                        <!-- Form sửa địa chỉ -->
                        <div id="edit-${address.addressId}"
                             class="edit-form">

                            <form action="${pageContext.request.contextPath}/addresses"
                                  method="post">

                                <input type="hidden"
                                       name="action"
                                       value="update">

                                <input type="hidden"
                                       name="addressId"
                                       value="${address.addressId}">

                                <div class="form-group">
                                    <label>Tỉnh / Thành phố</label>
                                    <input type="text"
                                           name="province"
                                           value="${address.province}"
                                           required>
                                </div>

                                <div class="form-group">
                                    <label>Phường / Xã</label>
                                    <input type="text"
                                           name="ward"
                                           value="${address.ward}"
                                           required>
                                </div>

                                <div class="form-group">
                                    <label>Địa chỉ chi tiết</label>
                                    <input type="text"
                                           name="detailAddress"
                                           value="${address.detailAddress}"
                                           required>
                                </div>

                                <label>
                                    <input type="checkbox"
                                           name="isDefault"
                                           value="true"
                                           <c:if test="${address.defaultAddress}">
                                               checked
                                           </c:if>>
                                    Đặt làm địa chỉ mặc định
                                </label>

                                <br><br>

                                <button type="submit" class="primary">
                                    Lưu thay đổi
                                </button>

                                <button type="button"
                                        class="secondary"
                                        onclick="toggleEdit(${address.addressId})">
                                    Hủy
                                </button>
                            </form>
                        </div>

                    </div>

                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Form thêm địa chỉ mới -->
    <div class="card">
        <h3>Thêm địa chỉ mới</h3>

        <form action="${pageContext.request.contextPath}/addresses"
              method="post">

            <input type="hidden"
                   name="action"
                   value="insert">

            <div class="form-group">
                <label>Tỉnh / Thành phố</label>
                <input type="text"
                       name="province"
                       placeholder="Ví dụ: TP. Hồ Chí Minh"
                       required>
            </div>

            <div class="form-group">
                <label>Phường / Xã</label>
                <input type="text"
                       name="ward"
                       placeholder="Nhập phường hoặc xã"
                       required>
            </div>

            <div class="form-group">
                <label>Địa chỉ chi tiết</label>
                <input type="text"
                       name="detailAddress"
                       placeholder="Số nhà, tên đường..."
                       required>
            </div>

            <label>
                <input type="checkbox"
                       name="isDefault"
                       value="true">
                Đặt làm địa chỉ mặc định
            </label>

            <br><br>

            <button type="submit" class="primary">
                Thêm địa chỉ
            </button>
        </form>
    </div>

</div>

<script>
    function toggleEdit(addressId) {
        var form = document.getElementById("edit-" + addressId);

        if (form.style.display === "block") {
            form.style.display = "none";
        } else {
            form.style.display = "block";
        }
    }
</script>

</body>
</html>
