<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Order Details | SportShopWeb</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/common.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/order-detail.css">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>

<body>

<jsp:include page="/views/common/header.jsp" />

<main class="order-detail-page">
    <div class="order-detail-container">

        <a class="back-to-orders"
           href="${pageContext.request.contextPath}/orders">
            <i class="fa-solid fa-arrow-left"></i>
            Back to My Orders
        </a>

        <c:choose>
            <c:when test="${empty order}">
                <section class="detail-card empty-order">
                    <div class="empty-order-icon">
                        <i class="fa-solid fa-receipt"></i>
                    </div>
                    <h2>Order not found</h2>
                    <p>We couldn't find the order you're looking for.</p>
                    <a class="continue-shopping-btn"
                       href="${pageContext.request.contextPath}/orders">
                        Back to My Orders
                    </a>
                </section>
            </c:when>

            <c:otherwise>
                <c:set var="status"
                       value="${order.orderStatus.toString()}" />

                <!-- Order header -->
                <section class="order-hero">
                    <div class="order-hero-top">
                        <div>
                            <div class="detail-eyebrow">
                                <i class="fa-solid fa-receipt"></i>
                                ORDER DETAILS
                            </div>

                            <h1>Order #${order.orderId}</h1>

                            <p class="hero-description">
                                Thank you for shopping with SportShopWeb.
                                Here's your order summary.
                            </p>
                        </div>

                        <c:choose>
                            <c:when test="${status eq 'PENDING'}">
                                <span class="order-status status-pending">
                                    <i class="fa-regular fa-clock"></i> Pending
                                </span>
                            </c:when>
                            <c:when test="${status eq 'CONFIRMED'}">
                                <span class="order-status status-confirmed">
                                    <i class="fa-solid fa-check"></i> Confirmed
                                </span>
                            </c:when>
                            <c:when test="${status eq 'PROCESSING'}">
                                <span class="order-status status-processing">
                                    <i class="fa-solid fa-box"></i> Processing
                                </span>
                            </c:when>
                            <c:when test="${status eq 'SHIPPING'}">
                                <span class="order-status status-shipping">
                                    <i class="fa-solid fa-truck"></i> Shipping
                                </span>
                            </c:when>
                            <c:when test="${status eq 'DELIVERED'}">
                                <span class="order-status status-delivered">
                                    <i class="fa-solid fa-circle-check"></i> Delivered
                                </span>
                            </c:when>
                            <c:when test="${status eq 'CANCELLED'}">
                                <span class="order-status status-cancelled">
                                    <i class="fa-solid fa-circle-xmark"></i> Cancelled
                                </span>
                            </c:when>
                            <c:otherwise>
                                <span class="order-status status-default">
                                    <c:out value="${status}" />
                                </span>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="order-meta-grid">
                        <div class="order-meta-item">
                            <span class="meta-icon">
                                <i class="fa-regular fa-calendar"></i>
                            </span>
                            <div>
                                <span class="meta-label">ORDER DATE</span>
                                <strong>
                                    <c:choose>
                                        <c:when test="${not empty order.orderDate}">
                                            <fmt:formatDate
                                                value="${order.orderDate}"
                                                pattern="dd/MM/yyyy HH:mm" />
                                        </c:when>
                                        <c:otherwise>Not available</c:otherwise>
                                    </c:choose>
                                </strong>
                            </div>
                        </div>

                        <div class="order-meta-item">
                            <span class="meta-icon">
                                <i class="fa-solid fa-hashtag"></i>
                            </span>
                            <div>
                                <span class="meta-label">ORDER NUMBER</span>
                                <strong>#${order.orderId}</strong>
                            </div>
                        </div>
                    </div>
                </section>

                <div class="order-detail-layout">

                    <!-- Left column -->
                    <div class="detail-main-column">

<!-- Products -->
<section class="detail-card products-card">

    <div class="products-heading">
        <div>
            <span class="section-eyebrow">YOUR PURCHASE</span>
            <h2>Order Items</h2>
            <p class="products-subtitle">
                Products included in this order
            </p>
        </div>

        <span class="item-count">
            <i class="fa-solid fa-bag-shopping"></i>
            <c:out value="${order.orderItems.size()}" /> items
        </span>
    </div>

    <c:choose>
        <c:when test="${empty order.orderItems}">
            <div class="products-empty">
                <i class="fa-solid fa-box-open"></i>
                <h3>No products found</h3>
                <p>This order currently has no associated items.</p>
            </div>
        </c:when>

        <c:otherwise>
            <div class="products-list">

                <c:forEach var="item" items="${order.orderItems}">
                    <div class="order-product-item">
                        
                        <div class="product-image">
                            <c:if test="${not empty item.productVariant.product.images}">
                                <img
                                    src="<c:out value='${item.productVariant.product.images[0].imageUrl}'/>"
                                    alt="<c:out value='${item.productVariant.variantName}'/>"
                                    class="product-img"
                                    onerror="this.style.display='none';">
                            </c:if>
                        </div>
                        <div class="order-product-info">
                            <div class="order-product-name">
                                <c:choose>
                                    <c:when test="${not empty item.productVariant
                                                   and not empty item.productVariant.product}">
                                        <c:out value="${item.productVariant.product.productName}" />
                                    </c:when>
                                    <c:otherwise>
                                        Product information unavailable
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="order-product-variant">
                                Variant ID:
                                <c:out value="${item.productVariant.variantId}" />
                            </div>

                            <div class="order-product-quantity">
                                Quantity:
                                <strong>
                                    <c:out value="${item.quantity}" />
                                </strong>
                            </div>
                        </div>

                    </div>
                </c:forEach>

            </div>
        </c:otherwise>
    </c:choose>

</section>

                        <!-- Customer -->
                        <section class="detail-card">
                            <div class="section-heading">
                                <div>
                                    <span class="section-eyebrow">ORDER CONTACT</span>
                                    <h2>Customer Information</h2>
                                </div>
                                <span class="section-icon">
                                    <i class="fa-regular fa-user"></i>
                                </span>
                            </div>

                            <div class="info-panel">
                                <div class="info-panel-icon">
                                    <i class="fa-solid fa-user"></i>
                                </div>
                                <div>
                                    <span class="info-label">Customer ID</span>
                                    <strong>
                                        <c:out value="${order.customer.customerId}" />
                                    </strong>
                                </div>
                            </div>
                        </section>

                        <!-- Payment -->
                        <section class="detail-card">
                            <div class="section-heading">
                                <div>
                                    <span class="section-eyebrow">TRANSACTION</span>
                                    <h2>Payment Information</h2>
                                </div>
                                <span class="section-icon">
                                    <i class="fa-regular fa-credit-card"></i>
                                </span>
                            </div>

                            <div class="payment-info-row">
                                <span class="payment-label">Order status</span>
                                <strong><c:out value="${status}" /></strong>
                            </div>

                            <div class="payment-info-row">
                                <span class="payment-label">Payment method</span>
                                <span class="payment-value">
                                    Not available
                                </span>
                            </div>

                            <div class="payment-info-row">
                                <span class="payment-label">Payment status</span>
                                <span class="payment-value">
                                    Not available
                                </span>
                            </div>
                        </section>
                    </div>

                    <!-- Right column -->
                    <aside class="detail-summary-column">
                        <section class="detail-card order-summary-card">
                            <div class="section-heading">
                                <div>
                                    <span class="section-eyebrow">AT A GLANCE</span>
                                    <h2>Order Summary</h2>
                                </div>
                            </div>

                            <div class="summary-order-number">
                                <span>Order reference</span>
                                <strong>#${order.orderId}</strong>
                            </div>

                            <div class="summary-row">
                                <span>Items</span>
                                <strong>${order.orderItems.size()}</strong>
                            </div>

                            <div class="summary-row">
                                <span>Discount</span>
                                <strong class="discount-value">
                                    −<fmt:formatNumber
                                        value="${order.discountAmount}"
                                        type="number"
                                        groupingUsed="true" /> ₫
                                </strong>
                            </div>

                            <div class="summary-divider"></div>

                            <div class="summary-total">
                                <span>Total</span>
                                <strong>
                                    <fmt:formatNumber
                                        value="${order.totalAmount}"
                                        type="number"
                                        groupingUsed="true" /> ₫
                                </strong>
                            </div>

                            <p class="summary-note">
                                <i class="fa-solid fa-shield-halved"></i>
                                Your order details are available here for reference.
                            </p>

                            <a class="continue-shopping-btn"
                               href="${pageContext.request.contextPath}/HomeServlet">
                                Continue Shopping
                                <i class="fa-solid fa-arrow-right"></i>
                            </a>
                        </section>
                    </aside>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</main>

<jsp:include page="/views/common/footer.jsp" />

</body>
</html>