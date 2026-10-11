<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>My Orders | SportShopWeb</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/orders.css">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>

<body>

    <jsp:include page="/views/common/header.jsp" />

    <main class="orders-page">

        <section class="orders-heading">
            <div>
                <p class="orders-eyebrow">YOUR ACCOUNT</p>
                <h1 class="orders-title">My Orders</h1>
                <p class="orders-description">
                    Track your orders and review your purchase history.
                </p>
            </div>

            <c:if test="${not empty orders}">
                <div class="orders-count">
                    <c:out value="${orders.size()}"/> orders
                </div>
            </c:if>
        </section>

        <!-- Success Message -->
        <c:if test="${not empty success}">
            <div class="orders-alert" role="status">
                <i class="fa-solid fa-circle-check"></i>
                <c:out value="${success}"/>
            </div>
        </c:if>

        <!-- Error Message -->
        <c:if test="${not empty error}">
            <div class="orders-alert orders-alert-error" role="alert">
                <i class="fa-solid fa-circle-exclamation"></i>
                <c:out value="${error}"/>
            </div>
        </c:if>

        <c:choose>

          
            <c:when test="${empty orders}">
                <section class="orders-empty">
                    <div class="orders-empty-icon">
                        <i class="fa-solid fa-box-open"></i>
                    </div>

                    <h2>You Don't Have Any Orders Yet</h2>

                    <p>
                        Your orders will appear here once you place them.
                        Explore our products and start shopping today!
                    </p>

                    <a class="orders-shop-btn"
                       href="${pageContext.request.contextPath}/products">
                        <i class="fa-solid fa-bag-shopping"></i>
                        Explore Products
                    </a>
                </section>
            </c:when>

            
            <c:otherwise>
                <section class="orders-list">

                    <c:forEach var="order" items="${orders}">

                        <article class="order-card">

                            <div class="order-card-header">
                                <div>
                                    <p class="order-number-label">
                                        ORDER NUMBER
                                    </p>

                                    <p class="order-number">
                                        #<c:out value="${order.orderId}"/>
                                    </p>

                                    <p class="order-date">
                                        <i class="fa-regular fa-calendar"></i>
                                        <c:out value="${order.orderDate}"/>
                                    </p>
                                </div>

                                <!-- Display Order Status -->
                                <c:set var="status" value="${order.status}"/>

                                <c:choose>
                                    <c:when test="${status eq 'PENDING'}">
                                        <span class="order-status status-pending">
                                            <i class="fa-regular fa-clock"></i>
                                            Pending Confirmation
                                        </span>
                                    </c:when>

                                    <c:when test="${status eq 'CONFIRMED'}">
                                        <span class="order-status status-confirmed">
                                            <i class="fa-solid fa-check"></i>
                                            Confirmed
                                        </span>
                                    </c:when>

                                    <c:when test="${status eq 'PROCESSING'}">
                                        <span class="order-status status-processing">
                                            <i class="fa-solid fa-box"></i>
                                            Processing
                                        </span>
                                    </c:when>

                                    <c:when test="${status eq 'SHIPPING'}">
                                        <span class="order-status status-shipping">
                                            <i class="fa-solid fa-truck"></i>
                                            Shipping
                                        </span>
                                    </c:when>

                                    <c:when test="${status eq 'DELIVERED'}">
                                        <span class="order-status status-delivered">
                                            <i class="fa-solid fa-circle-check"></i>
                                            Delivered
                                        </span>
                                    </c:when>

                                    <c:when test="${status eq 'CANCELLED'}">
                                        <span class="order-status status-cancelled">
                                            <i class="fa-solid fa-circle-xmark"></i>
                                            Cancelled
                                        </span>
                                    </c:when>

                                    <c:otherwise>
                                        <span class="order-status status-default">
                                            <c:out value="${status}"/>
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="order-card-body">
                                <div class="order-info-grid">

                                    <div>
                                        <p class="order-info-label">
                                            Recipient
                                        </p>
                                        <p class="order-info-value">
                                            <c:out value="${order.fullName}"/>
                                        </p>
                                    </div>

                                    <div>
                                        <p class="order-info-label">
                                            Phone Number
                                        </p>
                                        <p class="order-info-value">
                                            <c:out value="${order.phone}"/>
                                        </p>
                                    </div>

                                    <div>
                                        <p class="order-info-label">
                                            Payment Method
                                        </p>
                                        <p class="order-info-value">
                                            <c:out value="${order.paymentMethod}"/>
                                        </p>
                                    </div>

                                    <div>
                                        <p class="order-info-label">
                                            Total Amount
                                        </p>
                                        <p class="order-total">
                                            <fmt:formatNumber
                                                value="${order.totalAmount}"
                                                type="number"
                                                groupingUsed="true"/> ₫
                                        </p>
                                    </div>

                                </div>
                            </div>

                            <div class="order-card-footer">
                                <p class="order-payment">
                                    <i class="fa-solid fa-shield-halved"></i>
                                    Order ID:
                                    <strong>
                                        #<c:out value="${order.orderId}"/>
                                    </strong>
                                </p>

                                <a class="order-detail-btn"
                                   href="${pageContext.request.contextPath}/order-detail.jsp?orderId=${order.orderId}">
                                    View Details
                                    <i class="fa-solid fa-arrow-right"></i>
                                </a>
                            </div>

                        </article>

                    </c:forEach>

                </section>
            </c:otherwise>

        </c:choose>

    </main>

    <jsp:include page="/views/common/footer.jsp" />

</body>
</html>