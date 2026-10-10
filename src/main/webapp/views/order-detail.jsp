<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
               href="${pageContext.request.contextPath}/views/orders.jsp">
                <i class="fa-solid fa-arrow-left"></i>
                Back to My Orders
            </a>

            <!-- Order Overview -->
            <section class="detail-heading">
                <div>
                    <p class="detail-eyebrow">ORDER DETAILS</p>
                    <h1>Order <span class="order-number">#ORD-1001</span></h1>
                    <p class="detail-description">
                        Review the items, delivery information, and payment details for this order.
                    </p>
                </div>

                <span class="order-status status-delivered">
                    <i class="fa-solid fa-circle-check"></i>
                    Delivered
                </span>
            </section>

            <section class="detail-meta">
                <div class="meta-item">
                    <span class="meta-label">ORDER DATE</span>
                    <span class="meta-value">October 8, 2026</span>
                </div>

                <div class="meta-item">
                    <span class="meta-label">ORDER NUMBER</span>
                    <span class="meta-value">ORD-1001</span>
                </div>
            </section>

            <div class="order-detail-layout">

                <!-- Left Column -->
                <div class="detail-main-column">

                    <!-- Order Items -->
                    <section class="detail-card">
                        <div class="section-heading">
                            <div>
                                <p class="section-eyebrow">YOUR PURCHASE</p>
                                <h2>Order Items</h2>
                            </div>
                            <span class="item-count">2 items</span>
                        </div>

                        <div class="detail-product">
                            <div class="product-image-placeholder">
                                <i class="fa-solid fa-shoe-prints"></i>
                            </div>

                            <div class="detail-product-info">
                                <h3>Football Boots</h3>
                                <p class="product-variant">Size: 42</p>
                                <p class="product-quantity">Quantity: 1</p>
                            </div>

                            <div class="detail-product-price">
                                1,200,000 ₫
                            </div>
                        </div>

                        <div class="detail-product">
                            <div class="product-image-placeholder">
                                <i class="fa-solid fa-shirt"></i>
                            </div>

                            <div class="detail-product-info">
                                <h3>Training T-Shirt</h3>
                                <p class="product-variant">Size: M</p>
                                <p class="product-quantity">Quantity: 2</p>
                            </div>

                            <div class="detail-product-price">
                                600,000 ₫
                            </div>
                        </div>
                    </section>

                    <!-- Shipping Address -->
                    <section class="detail-card">
                        <div class="section-heading">
                            <div>
                                <p class="section-eyebrow">DELIVERY</p>
                                <h2>Shipping Address</h2>
                            </div>

                            <span class="section-icon">
                                <i class="fa-solid fa-location-dot"></i>
                            </span>
                        </div>

                        <div class="shipping-info">
                            <p class="shipping-name">Nguyen Van A</p>
                            <p>0901 234 567</p>
                            <p>123 Nguyen Trai Street, District 1</p>
                            <p>Ho Chi Minh City, Viet Nam</p>
                        </div>
                    </section>

                    <!-- Payment Information -->
                    <section class="detail-card">
                        <div class="section-heading">
                            <div>
                                <p class="section-eyebrow">TRANSACTION</p>
                                <h2>Payment Information</h2>
                            </div>

                            <span class="section-icon">
                                <i class="fa-regular fa-credit-card"></i>
                            </span>
                        </div>

                        <div class="payment-info-row">
                            <span class="payment-label">Payment Method</span>
                            <span class="payment-value">Cash on Delivery (COD)</span>
                        </div>

                        <div class="payment-info-row">
                            <span class="payment-label">Payment Status</span>
                            <span class="payment-status payment-paid">Paid</span>
                        </div>
                    </section>

                </div>

                <!-- Right Column: Order Summary -->
                <aside class="detail-summary-column">
                    <section class="detail-card order-summary-card">
                        <div class="section-heading">
                            <div>
                                <p class="section-eyebrow">PAYMENT BREAKDOWN</p>
                                <h2>Order Summary</h2>
                            </div>
                        </div>

                        <div class="summary-row">
                            <span>Subtotal</span>
                            <span>1,800,000 ₫</span>
                        </div>

                        <div class="summary-row">
                            <span>Shipping Fee</span>
                            <span>30,000 ₫</span>
                        </div>

                        <div class="summary-row">
                            <span>Discount</span>
                            <span>0 ₫</span>
                        </div>

                        <div class="summary-divider"></div>

                        <div class="summary-total">
                            <span>Total</span>
                            <strong>1,830,000 ₫</strong>
                        </div>

                        <p class="summary-note">
                            <i class="fa-solid fa-shield-halved"></i>
                            Your order information is displayed above.
                        </p>

                        <a class="continue-shopping-btn"
                           href="${pageContext.request.contextPath}/HomeServlet">
                            <i class="fa-solid fa-arrow-left"></i>
                            Continue Shopping
                        </a>
                    </section>
                </aside>

            </div>
        </div>
    </main>

    <jsp:include page="/views/common/footer.jsp" />

</body>
</html>