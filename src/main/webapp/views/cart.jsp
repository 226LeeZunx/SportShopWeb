
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shopping Cart | SportShopWeb</title>

    <c:url var="cartCss" value="/assets/css/cart.css"/>
    <link rel="stylesheet" href="${cartCss}">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/common.css">
</head>
<body>

<jsp:include page="/views/common/header.jsp" />

<main class="page-container">

    <section class="page-heading">
        

        <div>
            <p class="eyebrow">SPORTSHOPWEB / SHOPPING CART</p>
            <h1>Your Cart</h1>
            <p class="heading-description">
                Review your items before proceeding to checkout.
            </p>
        </div>
    </section>

    <c:choose>
        <c:when test="${empty cartItems}">
            <section class="empty-cart">
                <div class="empty-cart-icon" aria-hidden="true">
                    &#128722;
                </div>

                <h2>Your cart is empty.</h2>
                <p>
                    You don't have any products in your cart yet.
                    Discover our sports collection and find something you love!
                </p>

                <a class="btn btn-primary"
                   href="${pageContext.request.contextPath}/products">
                   <i class="fa-solid fa-bag-shopping"></i>
                    Explore Products
                </a>
            </section>
        </c:when>

        <c:otherwise>
            <div class="cart-layout">

                <section class="cart-panel">
                    <div class="panel-heading">
                        <div>
                            <h2>Selected Items</h2>
                            <p>Review your product details and quantities.</p>
                        </div>

                        <span class="item-count">
                            <c:out value="${cartItems.size()}"/> item types
                        </span>
                    </div>

                    <div class="cart-table-wrapper">
                        <table class="cart-table">
                            <thead>
                                <tr>
                                    <th class="product-column">Product</th>
                                    <th>Unit Price</th>
                                    <th>Quantity</th>
                                    <th>Subtotal</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>

                            <tbody>
                                <c:forEach var="item" items="${cartItems}">
                                    <tr>
                                        <td>
                                            <div class="product-info">
                                                <div class="product-image">
                                                    <span aria-hidden="true">
                                                        &#9917;
                                                    </span>
                                                </div>

                                                <div class="product-details">
                                                    <h3>
                                                        <c:out value="${item.productVariant.product.description}"/>
                                                    </h3>

                                                    <p class="product-meta">
                                                        Variant ID:
                                                        <c:out value="${item.productVariant.variantId}"/>
                                                    </p>
                                                </div>
                                            </div>
                                        </td>

                                        <td class="price-cell">
                                            <fmt:formatNumber
                                                value="${item.productVariant.product.price}"
                                                type="number"
                                                groupingUsed="true"
                                                maxFractionDigits="0"/>
                                            ₫
                                        </td>

                                        <td>
                                            <form class="quantity-form"
                                                  action="${pageContext.request.contextPath}/cart"
                                                  method="post">
                                                <input type="hidden"
                                                       name="action"
                                                       value="update"/>

                                                <input type="hidden"
                                                       name="variantId"
                                                       value="${item.productVariant.variantId}"/>

                                                <label class="visually-hidden"
                                                       for="quantity-${item.productVariant.variantId}">
                                                    Product quantity
                                                </label>

                                                <input class="quantity-input"
                                                       id="quantity-${item.productVariant.variantId}"
                                                       type="number"
                                                       name="quantity"
                                                       value="${item.quantity}"
                                                       min="1"
                                                       max="99"
                                                       required/>

                                                <button class="btn-quantity"
                                                        type="submit"
                                                        title="Update quantity">
                                                    Update
                                                </button>
                                            </form>
                                        </td>

                                        <td class="subtotal-cell">
                                            <fmt:formatNumber
                                                value="${item.productVariant.product.price * item.quantity}"
                                                type="number"
                                                groupingUsed="true"
                                                maxFractionDigits="0"/>
                                            ₫
                                        </td>

                                        <td>
                                            <form action="${pageContext.request.contextPath}/cart"
                                                  method="post">
                                                <input type="hidden"
                                                       name="action"
                                                       value="remove"/>

                                                <input type="hidden"
                                                       name="variantId"
                                                       value="${item.productVariant.variantId}"/>

                                                <button class="btn-remove"
                                                        type="submit"
                                                        aria-label="Remove product"
                                                        title="Remove product">
                                                    &#128465;
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <div class="cart-footer">
                        <a class="btn btn-outline"
                           href="${pageContext.request.contextPath}/products">
                            <span aria-hidden="true">&larr;</span>
                            Continue Shopping
                        </a>
                    </div>
                </section>

                <aside class="summary-column">
                    <section class="summary-card">
                        <div class="summary-heading">
                            <h2>Order Summary</h2>
                            <span class="summary-badge">ORDER SUMMARY</span>
                        </div>

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
                            <span class="free-shipping">Calculated at checkout</span>
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

                        <a class="btn btn-primary checkout-btn"
                           href="${pageContext.request.contextPath}/checkout">
                            Proceed to Checkout
                            <span aria-hidden="true">&rarr;</span>
                        </a>

                        <p class="secure-note">
                            <span aria-hidden="true">&#128274;</span>
                            Your information is secure and protected.
                        </p>
                    </section>

                    <section class="benefit-card">
                        <div class="benefit-icon" aria-hidden="true">
                            &#128666;
                        </div>
                        <div>
                            <h3>Shipping Information</h3>
                            <p>Shipping fees will be confirmed at checkout.</p>
                        </div>
                    </section>

                    <section class="benefit-card">
                        <div class="benefit-icon" aria-hidden="true">
                            &#8635;
                        </div>
                        <div>
                            <h3>Returns & Exchanges</h3>
                            <p>Contact our store for assistance with returns and exchanges.</p>
                        </div>
                    </section>
                </aside>
            </div>
        </c:otherwise>
    </c:choose>

</main>

<jsp:include page="/views/common/footer.jsp" />

</body>
</html>
