<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Đặt hàng thành công | SportShopWeb</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/common.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/order-sucess.css">
</head>

<body>

    <jsp:include page="/views/common/header.jsp" />

    <main class="success-page">
        <section class="success-card">

            <!-- Biểu tượng thành công -->
            <div class="success-icon">&#10003;</div>

            <p class="success-label">SPORTSHOPWEB</p>

            <h1>Đặt hàng thành công!</h1>

            <p class="success-message">
                Cảm ơn bạn đã mua sắm tại SportShopWeb.
                <br>
                Đơn hàng của bạn đã được tiếp nhận.
            </p>

            <!-- Thông tin đơn hàng -->
            <div class="order-info">

                <div class="info-row">
                    <span class="info-label">
                        <span class="info-icon">&#9776;</span>
                        Mã đơn hàng
                    </span>

                    <strong>
                        <c:choose>
                            <c:when test="${not empty orderId}">
                                #<c:out value="${orderId}" />
                            </c:when>
                            <c:otherwise>
                                Đang cập nhật
                            </c:otherwise>
                        </c:choose>
                    </strong>
                </div>

                <div class="info-row">
                    <span class="info-label">
                        <span class="info-icon">&#9711;</span>
                        Trạng thái
                    </span>

                    <span class="status-badge">
                        <span class="status-dot"></span>
                        Đã tiếp nhận
                    </span>
                </div>

                <div class="info-row">
                    <span class="info-label">
                        <span class="info-icon">&#8364;</span>
                        Tổng thanh toán
                    </span>

                    <strong class="order-total">
                        <c:choose>
                            <c:when test="${not empty orderTotal}">
                                <fmt:formatNumber
                                    value="${orderTotal}"
                                    type="number"
                                    groupingUsed="true"
                                    maxFractionDigits="0" /> ₫
                            </c:when>
                            <c:otherwise>
                                Đang cập nhật
                            </c:otherwise>
                        </c:choose>
                    </strong>
                </div>

            </div>

            <p class="confirmation-note">
                Vui lòng lưu lại mã đơn hàng để tiện theo dõi và
                liên hệ với cửa hàng khi cần hỗ trợ.
            </p>

            <!-- Khu vực thanh toán VNPay -->
            <section class="payment-section">

                <div class="payment-heading">
                    <div class="payment-icon">&#128179;</div>

                    <div class="payment-heading-text">
                        <h2>Thanh toán đơn hàng</h2>
                        <p>
                            Tiếp tục thanh toán theo phương thức bạn đã
                            chọn khi đặt hàng.
                        </p>
                    </div>

                </div>

                <div class="payment-content">

                    <!-- Thông tin thanh toán -->
                    <div class="payment-details">

                        <div class="payment-detail-row">
                            <span>Mã đơn hàng</span>
                            <strong>
                                <c:choose>
                                    <c:when test="${not empty orderId}">
                                        #<c:out value="${orderId}" />
                                    </c:when>
                                    <c:otherwise>
                                        Đang cập nhật
                                    </c:otherwise>
                                </c:choose>
                            </strong>
                        </div>

                        <div class="payment-detail-row">
                            <span>Số tiền thanh toán</span>
                            <strong class="payment-amount">
                                <c:choose>
                                    <c:when test="${not empty orderTotal}">
                                        <fmt:formatNumber
                                            value="${orderTotal}"
                                            type="number"
                                            groupingUsed="true"
                                            maxFractionDigits="0" /> ₫
                                    </c:when>
                                    <c:otherwise>
                                        Đang cập nhật
                                    </c:otherwise>
                                </c:choose>
                            </strong>
                        </div>

                        <div class="secure-payment-note">
                            <span class="secure-icon">&#128737;</span>

                            <div>
                                <strong>Thanh toán an toàn qua VNPay</strong>
                                <p>
                                    Bạn sẽ được chuyển đến cổng thanh toán
                                    thử nghiệm VNPay để tiếp tục giao dịch.
                                </p>
                            </div>
                        </div>

                    </div>

                    <!-- Khu vực logo và nút VNPay -->
                    <div class="payment-provider">

                        <div class="vnpay-logo">
                            <span class="vnpay-symbol">&#9670;</span>
                            <span class="vnpay-word">
                                <strong>VNPAY</strong>
                                <small>Scan to Pay</small>
                            </span>
                        </div>

                        <!--
                            Chưa gắn action nếu Servlet VNPay chưa hoàn tất.
                            Khi có endpoint, thay phần này bằng form gửi
                            đến Servlet tạo URL thanh toán.
                        -->
                        <p class="payment-action-hint">
                            Nhấn nút để bắt đầu thanh toán đơn hàng.
                        </p>

                        <a class="vnpay-button"
                           href="${pageContext.request.contextPath}/vnpay-payment?orderId=${orderId}">
                            <span>&#128279;</span>
                            Thanh toán qua VNPay
                            <span class="button-arrow">&#8250;</span>
                        </a>

                    </div>

                </div>

            </section>

            <!-- Điều hướng -->
            <div class="success-actions">

                <a class="btn btn-primary"
                   href="${pageContext.request.contextPath}/HomeServlet">
                    <span>&#8962;</span>
                    Tiếp tục mua sắm
                </a>

                <a class="btn btn-outline"
                   href="${pageContext.request.contextPath}/cart">
                    <span>&#128722;</span>
                    Quay lại giỏ hàng
                </a>

            </div>

        </section>
    </main>

    <jsp:include page="/views/common/footer.jsp" />

</body>
</html>