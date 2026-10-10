
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Checkout | SportShopWeb</title>

    <c:url var="cartCss" value="/assets/css/cart.css"/>
    <link rel="stylesheet" href="${cartCss}">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/common.css">
</head>

<body>
<jsp:include page="/views/common/header.jsp" />

<main class="page-container checkout-page">

    <!-- Page heading -->
    <section class="page-heading">
        <div class="heading-icon" aria-hidden="true">
            &#128179;
        </div>

        <div>
            <p class="eyebrow">SPORTSHOPWEB / CHECKOUT</p>
            <h1>Checkout</h1>
            <p class="heading-description">
                Complete your details to place your order.
            </p>
        </div>
    </section>

    <!-- Breadcrumb -->
    <nav class="checkout-breadcrumb" aria-label="Breadcrumb">
        <a href="${pageContext.request.contextPath}/cart">
            Shopping Cart
        </a>
        <span aria-hidden="true">&rsaquo;</span>
        <span>Checkout</span>
    </nav>

    <!-- Error message from Servlet, if any -->
    <c:if test="${not empty error}">
        <div class="checkout-alert" role="alert">
            <c:out value="${error}"/>
        </div>
    </c:if>

    <c:choose>

        <c:when test="${empty cartItems}">
            <section class="empty-cart">
                <div class="empty-cart-icon" aria-hidden="true">
                    &#128722;
                </div>

                <h2>No Items to Checkout</h2>

                <p>
                    Your cart is empty.
                    Add some products to your cart before placing an order.
                </p>

                <a class="btn btn-primary"
                   href="${pageContext.request.contextPath}/products">
                    Continue Shopping
                </a>
            </section>
        </c:when>

        <c:otherwise>

            <form class="checkout-form"
                  action="${pageContext.request.contextPath}/checkout"
                  method="post">

                <div class="checkout-layout">

                    <!-- Left: Customer information -->
                    <section class="checkout-panel">

                        <div class="panel-heading">
                            <div>
                                <h2>Shipping Information</h2>
                                <p>
                                    Please provide accurate delivery details.
                                </p>
                            </div>

                            <span class="checkout-step">01</span>
                        </div>

                        <div class="checkout-fields">

                            <div class="form-group">
                                <label for="fullName">
                                    Full Name
                                    <span class="required">*</span>
                                </label>

                                <input
                                    type="text"
                                    id="fullName"
                                    name="fullName"
                                    value="${param.fullName}"
                                    placeholder="Enter the recipient's full name"
                                    maxlength="100"
                                    autocomplete="name"
                                    required>
                            </div>

                            <div class="form-group">
                                <label for="phone">
                                    Phone Number
                                    <span class="required">*</span>
                                </label>

                                <input
                                    type="tel"
                                    id="phone"
                                    name="phone"
                                    value="${param.phone}"
                                    placeholder="Enter your phone number"
                                    maxlength="20"
                                    autocomplete="tel"
                                    required>
                            </div>

                            <div class="form-group">
                                <label for="address">
                                    Shipping Address
                                    <span class="required">*</span>
                                </label>

                                <textarea
                                    id="address"
                                    name="address"
                                    rows="3"
                                    maxlength="500"
                                    placeholder="Street address, ward, district, city or province"
                                    autocomplete="street-address"
                                    required><c:out value="${param.address}"/></textarea>
                            </div>

                            <div class="form-group">
                                <label for="note">
                                    Order Notes
                                    <span class="optional">(Optional)</span>
                                </label>

                                <textarea
                                    id="note"
                                    name="note"
                                    rows="3"
                                    maxlength="500"
                                    placeholder="For example: Please deliver in the afternoon"><c:out value="${param.note}"/></textarea>
                            </div>

                        </div>

                        <div class="checkout-section-divider"></div>

                        <div class="panel-heading payment-heading">
                            <div>
                                <h2>Payment Method</h2>
                                <p>Choose how you would like to pay for your order.</p>
                            </div>

                            <span class="checkout-step">02</span>
                        </div>

                        <div class="payment-option">
                            <label class="payment-choice">
                                <input
                                    type="radio"
                                    name="paymentMethod"
                                    value="COD"
                                    checked>

                                <span class="payment-radio-content">
                                    <span class="payment-icon"
                                          aria-hidden="true">
                                        &#128179;
                                    </span>

                                    <span>
                                        <strong>Cash on Delivery (COD)</strong>
                                        <small>
                                            Pay when your order is delivered.
                                        </small>
                                    </span>
                                </span>
                            </label>
                        </div>

                        <p class="checkout-security">
                            <span aria-hidden="true">&#128274;</span>
                            Your shipping information will only be used to process your order.
                        </p>

                    </section>

                    <!-- Right: Order summary -->
                    <aside class="checkout-summary">

                        <section class="summary-card">

                            <div class="summary-heading">
                                <h2>Your Order</h2>

                                <span class="summary-badge">
                                    ORDER SUMMARY
                                </span>
                            </div>

                            <div class="checkout-products">

                                <c:forEach var="item" items="${cartItems}">
                                    <div class="checkout-product">

                                        <div class="checkout-product-info">
                                            <h3>
                                                <c:out
                                                    value="${item.productVariant.product.description}"/>
                                            </h3>

                                            <p>
                                                Quantity:
                                                <c:out value="${item.quantity}"/>
                                            </p>
                                        </div>

                                        <strong class="checkout-product-price">
                                            <fmt:formatNumber
                                                value="${item.productVariant.product.price * item.quantity}"
                                                type="number"
                                                groupingUsed="true"
                                                maxFractionDigits="0"/> ₫
                                        </strong>

                                    </div>
                                </c:forEach>

                            </div>

                            <div class="summary-divider"></div>

                            <div class="summary-row">
                                <span>Subtotal</span>

                                <strong>
                                    <fmt:formatNumber
                                        value="${total}"
                                        type="number"
                                        groupingUsed="true"
                                        maxFractionDigits="0"/> ₫
                                </strong>
                            </div>

                            <div class="summary-row">
                                <span>Shipping Fee</span>

                                <span class="shipping-pending">
                                    To Be Confirmed
                                </span>
                            </div>

                            <div class="summary-divider"></div>

                            <div class="summary-total">
                                <span>Total</span>

                                <strong>
                                    <fmt:formatNumber
                                        value="${total}"
                                        type="number"
                                        groupingUsed="true"
                                        maxFractionDigits="0"/> ₫
                                </strong>
                            </div>

                            <p class="checkout-total-note">
                                Shipping fees have not been included in the total.
                            </p>

                            <button class="btn btn-primary checkout-btn"
                                    type="submit">
                                Place Order
                                <span aria-hidden="true">&rarr;</span>
                            </button>

                            <a class="back-to-cart"
                               href="${pageContext.request.contextPath}/cart">
                                &larr; Back to Cart
                            </a>

                        </section>

                        <div class="checkout-help">
                            <strong>Need Help?</strong>

                            <p>
                                Please contact our store if you need assistance
                                with your order.
                            </p>
                        </div>

                    </aside>

                </div>

            </form>

        </c:otherwise>

    </c:choose>

</main>

<jsp:include page="/views/common/footer.jsp" />

</body>
</html>