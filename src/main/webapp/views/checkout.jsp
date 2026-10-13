<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Checkout | SportShopWeb</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/common.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/checkout.css">

</head>

<body>

<jsp:include page="/views/common/header.jsp" />

<main class="checkout-container">

    <header class="checkout-heading">
        <p class="eyebrow">SPORTSHOPWEB / CHECKOUT</p>
        <h1>Thanh toán đơn hàng</h1>
        <p>Kiểm tra sản phẩm và điền thông tin giao hàng của bạn.</p>
    </header>

    <c:if test="${not empty error}">
        <div class="error-message">
            <c:out value="${error}" />
        </div>
    </c:if>

    <div class="checkout-layout">

        <div class="checkout-main">

            <!-- Sản phẩm đã chọn -->
            <section class="checkout-card">

                <div class="card-heading">
                    <h2>Sản phẩm đã chọn</h2>
                    <p>
                        Có <c:out value="${selectedItems.size()}" />
                        loại sản phẩm trong đơn hàng.
                    </p>
                </div>

                <c:choose>
                    <c:when test="${not empty selectedItems}">

                        <div class="product-list">

                            <c:forEach var="item" items="${selectedItems}">
                                <div class="product-item">

                                    <div class="checkout-product-image-wrapper">
                                        <c:choose>
                                            <c:when test="${not empty item.productVariant.product.images
                                                and not empty item.productVariant.product.images[0].imageUrl}">
                                                <img
                                                    src="<c:out value='${item.productVariant.product.images[0].imageUrl}'/>"
                                                    alt="<c:out value='${item.productVariant.variantName}'/>"
                                                    class="checkout-product-image"
                                                    onerror="this.style.display='none';">
                                            </c:when>
                                            <c:otherwise>
                                                <span style="font-size: 11px; color: #718096;">
                                                    No image
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>

                                    <div class="product-info">
                                        <h3>
                                            <c:out value="${item.productVariant.variantName}" />
                                        </h3>

                                        <p class="product-meta">
                                            Mã phiên bản:
                                            <c:out value="${item.productVariant.variantId}" />
                                            · Số lượng:
                                            <c:out value="${item.quantity}" />
                                        </p>

                                        <p class="product-meta">
                                            Đơn giá:
                                            <strong>
                                                <fmt:formatNumber
                                                    value="${item.productVariant.price}"
                                                    type="number"
                                                    groupingUsed="true"
                                                    maxFractionDigits="0" />
                                                ₫
                                            </strong>
                                        </p>
                                    </div>

                                    <div class="product-price">
                                        <span>Thành tiền</span>
                                        <strong>
                                            <fmt:formatNumber
                                                value="${item.productVariant.price * item.quantity}"
                                                type="number"
                                                groupingUsed="true"
                                                maxFractionDigits="0" />
                                            ₫
                                        </strong>
                                    </div>

                                </div>
                            </c:forEach>

                        </div>

                    </c:when>

                    <c:otherwise>
                        <div class="empty-selection">
                            Chưa có sản phẩm được chọn.
                            <br><br>
                            <a class="btn btn-outline"
                               href="${pageContext.request.contextPath}/cart">
                                Quay lại giỏ hàng
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>

            </section>

            <!-- Thông tin giao hàng -->
            <section class="checkout-card">

                <div class="card-heading">
                    <h2>Thông tin giao hàng</h2>
                    <p>Vui lòng cung cấp thông tin nhận hàng chính xác.</p>
                </div>

                <div class="card-body">

                    <form action="${pageContext.request.contextPath}/checkout"
                          method="post">

                        <input type="hidden" name="action" value="confirm">

                        <!-- Giữ lại danh sách sản phẩm đã chọn -->
                        <c:forEach var="item" items="${selectedItems}">
                            <input type="hidden"
                                   name="selectedVariantIds"
                                   value="${item.productVariant.variantId}">
                        </c:forEach>

                        <div class="form-grid">

                            <div class="form-group full-width">
                                <label for="fullName">
                                    Họ và tên người nhận
                                    <span class="required">*</span>
                                </label>

                                <input class="form-control"
                                       type="text"
                                       id="fullName"
                                       name="fullName"
                                       autocomplete="name"
                                       value="<c:out value='${param.fullName}'/>"
                                       placeholder="Nhập họ và tên"
                                       required>
                            </div>

                            <div class="form-group full-width">
                                <label for="phone">
                                    Số điện thoại
                                    <span class="required">*</span>
                                </label>

                                <input class="form-control"
                                       type="tel"
                                       id="phone"
                                       name="phone"
                                       autocomplete="tel"
                                       value="<c:out value='${param.phone}'/>"
                                       placeholder="Nhập số điện thoại"
                                       required>
                            </div>

                            <div class="form-group full-width">
                                <label for="address">
                                    Địa chỉ giao hàng
                                    <span class="required">*</span>
                                </label>

                                <textarea class="form-control"
                                          id="address"
                                          name="address"
                                          autocomplete="street-address"
                                          placeholder="Số nhà, đường, phường/xã, quận/huyện, tỉnh/thành phố"
                                          required><c:out value="${param.address}"/></textarea>
                            </div>

                            <div class="form-group full-width">
                                <label for="note">Ghi chú đơn hàng</label>

                                <textarea class="form-control"
                                          id="note"
                                          name="note"
                                          placeholder="Ghi chú thêm cho người giao hàng (nếu có)"><c:out value="${param.note}"/></textarea>
                            </div>

                            <div class="form-group full-width">
                                <label>
                                    Phương thức thanh toán
                                    <span class="required">*</span>
                                </label>

                                <div class="payment-options">

                                    <label class="payment-option">
                                        <input type="radio"
                                               name="paymentMethod"
                                               value="COD"
                                               checked
                                               required>
                                        <span>
                                            <strong>Thanh toán khi nhận hàng (COD)</strong>
                                            <small>Thanh toán trực tiếp khi nhận sản phẩm.</small>
                                        </span>
                                    </label>

                                    <label class="payment-option">
                                        <input type="radio"
                                               name="paymentMethod"
                                               value="BANK">
                                        <span>
                                            <strong>Chuyển khoản ngân hàng</strong>
                                            <small>Phương thức này cần được xử lý thêm ở backend.</small>
                                        </span>
                                    </label>

                                </div>
                            </div>

                        </div>

                        <button class="btn btn-primary"
                                type="submit"
                                <c:if test="${empty selectedItems}">disabled</c:if>>
                            Xác nhận đặt hàng
                        </button>

                    </form>

                </div>
            </section>

        </div>

        <!-- Tóm tắt đơn hàng -->
        <aside class="checkout-sidebar">

            <section class="checkout-card summary-card">

                <h2>Tóm tắt đơn hàng</h2>

                <div class="summary-row">
                    <span>Số loại sản phẩm</span>
                    <strong>
                        <c:out value="${selectedItems.size()}" />
                    </strong>
                </div>

                <div class="summary-row">
                    <span>Phí vận chuyển</span>
                    <strong>Được xác nhận sau</strong>
                </div>

                <div class="summary-divider"></div>

                <div class="summary-total">
                    <span>Tổng tiền hàng</span>
                    <strong>
                        <c:set var="checkoutTotal" value="${0}" />

                        <c:forEach var="item" items="${selectedItems}">
                            <c:set var="checkoutTotal"
                                   value="${checkoutTotal + item.productVariant.price * item.quantity}" />
                        </c:forEach>

                        <fmt:formatNumber
                            value="${checkoutTotal}"
                            type="number"
                            groupingUsed="true"
                            maxFractionDigits="0" />
                        ₫
                    </strong>
                </div>

                <p class="shipping-note">
                    Tổng tiền trên chưa bao gồm phí vận chuyển nếu có.
                </p>

                <a class="btn btn-outline"
                   href="${pageContext.request.contextPath}/cart">
                    ← Quay lại giỏ hàng
                </a>

                <p class="secure-note">
                    🔒 Thông tin của bạn được bảo vệ.
                </p>

            </section>

        </aside>

    </div>

</main>

<jsp:include page="/views/common/footer.jsp" />

</body>
</html>
