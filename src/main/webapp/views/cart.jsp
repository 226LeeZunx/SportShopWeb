
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

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/common.css?v=1">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/cart.css?v=6">
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

        <%-- Empty cart --%>
        <c:when test="${empty cartItems}">

            <section class="empty-cart">
                <div class="empty-cart-icon" aria-hidden="true">🛒</div>

                <h2>Your cart is empty.</h2>

                <p>
                    You don't have any products in your cart yet.
                    Discover our sports collection and find something you love!
                </p>

                <a class="btn btn-primary"
                   href="${pageContext.request.contextPath}/products">
                    Explore Products
                </a>
            </section>

        </c:when>

        <%-- Cart contains items --%>
        <c:otherwise>

            <div class="cart-layout">

                <%-- Cart table --%>
                <section class="cart-panel">

                    
                    <div class="panel-heading">
                        <div>
                            <h2>Shopping Cart Items</h2>
                            <p>Select the products you want to purchase.</p>
                        </div>

                        <div class="cart-heading-actions">
                            <button type="button"
                                    id="selectAllBtn"
                                    class="btn-select-all">
                                Chọn tất cả
                            </button>

                            <span class="item-count">
                                <c:out value="${cartItems.size()}" />
                                item types
                            </span>
                        </div>
                    </div>


                    <div class="cart-table-wrapper">

                        <table class="cart-table">

                            <thead>
                                <tr>
                                    <th>Choose</th>
                                    <th class="product-column">Product</th>
                                    <th class="price-column">Unit Price</th>
                                    <th class="quantity-column">Quantity</th>
                                    <th class="subtotal-column">Subtotal</th>
                                    <th class="actions-column">Actions</th>
                                </tr>
                            </thead>

                            <tbody>

                                <c:forEach var="item" items="${cartItems}">

                                    <tr>

                                        <%-- Checkbox --%>
                                        <td>
                                            <input
                                                type="checkbox"
                                                class="product-checkbox"
                                                name="selectedVariantIds"
                                                value="${item.productVariant.variantId}"
                                                <c:if test="${selectedVariantIds != null
                                                    and selectedVariantIds.contains(item.productVariant.variantId)}">
                                                    checked
                                                </c:if>>
                                        </td>

                                        <%-- Product information --%>
                                        <td class="product-cell">
                                            <div class="product-info">

                                                <div class="product-image">
                                                    <c:if test="${not empty item.productVariant.product.images}">
                                                        <img
                                                            src="<c:out value='${item.productVariant.product.images[0].imageUrl}'/>"
                                                            alt="<c:out value='${item.productVariant.variantName}'/>"
                                                            class="product-img"
                                                            onerror="this.style.display='none';">
                                                    </c:if>
                                                </div>

                                                <div class="product-details">
                                                    <h3>
                                                        <c:out value="${item.productVariant.variantName}" />
                                                    </h3>
                                                </div>

                                            </div>
                                        </td>

                                        <%-- Unit price --%>
                                        <td class="price-cell">
                                            <fmt:formatNumber
                                                value="${item.productVariant.price}"
                                                type="number"
                                                groupingUsed="true"
                                                maxFractionDigits="0" />
                                            ₫
                                        </td>

                                        <%-- Update quantity --%>
                                        <td class="quantity-cell">
                                            <form class="quantity-form"
                                                  action="${pageContext.request.contextPath}/cart"
                                                  method="post">

                                                <input type="hidden"
                                                       name="action"
                                                       value="update">

                                                <input type="hidden"
                                                       name="variantId"
                                                       value="${item.productVariant.variantId}">

                                                <%-- Preserve selected IDs when updating quantity --%>
                                                <c:forEach var="selectedId"
                                                           items="${selectedVariantIds}">
                                                    <input type="hidden"
                                                           name="selectedVariantIds"
                                                           value="${selectedId}">
                                                </c:forEach>

                                                <label class="visually-hidden"
                                                       for="quantity-${item.productVariant.variantId}">
                                                    Product quantity
                                                </label>

                                                <input
                                                    class="quantity-input"
                                                    id="quantity-${item.productVariant.variantId}"
                                                    type="number"
                                                    name="quantity"
                                                    value="${item.quantity}"
                                                    min="1"
                                                    max="99"
                                                    required>

                                                <button class="btn-quantity"
                                                        type="submit">
                                                    Update
                                                </button>
                                            </form>
                                        </td>

                                        <%-- Item subtotal --%>
                                        <td class="subtotal-cell">
                                            <fmt:formatNumber
                                                value="${item.productVariant.price * item.quantity}"
                                                type="number"
                                                groupingUsed="true"
                                                maxFractionDigits="0" />
                                            ₫
                                        </td>

                                        <%-- Delete item --%>
                                        <td class="actions-cell">
                                            <form action="${pageContext.request.contextPath}/cart"
                                                  method="post">

                                                <input type="hidden"
                                                       name="action"
                                                       value="delete">

                                                <input type="hidden"
                                                       name="variantId"
                                                       value="${item.productVariant.variantId}">

                                                <button class="btn-remove"
                                                        type="submit"
                                                        aria-label="Remove product"
                                                        title="Remove product">
                                                    🗑
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
                           href="${pageContext.request.contextPath}/HomeServlet">
                            <span aria-hidden="true">←</span>
                            Continue Shopping
                        </a>
                    </div>

                </section>

                <%-- Right column --%>
                <aside class="summary-column">

                    <section class="summary-card">

                        <div class="summary-heading">
                            <span class="summary-badge">YOUR ORDER</span>
                            <h2>Order Summary</h2>
                        </div>

                        <%-- Update Total form --%>
                        <form action="${pageContext.request.contextPath}/cart"
                              method="post"
                              id="updateTotalForm">

                            <input type="hidden"
                                   name="action"
                                   value="updateTotal">

                            <div class="summary-row">
                                <span>Subtotal</span>
                                <strong>
                                    <fmt:formatNumber
                                        value="${total}"
                                        type="number"
                                        groupingUsed="true"
                                        maxFractionDigits="0" /> ₫
                                </strong>
                            </div>

                            <div class="summary-row">
                                <span>Shipping Fee</span>
                                <span class="free-shipping">
                                    Calculated at checkout
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
                                        maxFractionDigits="0" /> ₫
                                </strong>
                            </div>

                            <button type="submit"
                                    class="btn btn-outline update-total-btn">
                                Update Total
                            </button>
                        </form>

                        <%-- Checkout form --%>
                        <form action="${pageContext.request.contextPath}/checkout"
                              method="post"
                              id="proceedCheckoutForm">

                            <input type="hidden"
                                   name="action"
                                   value="review">

                            <button type="submit"
                                    class="btn btn-primary checkout-btn">
                                Proceed to Checkout
                            </button>
                        </form>

                        <p class="secure-note">
                            <span aria-hidden="true">🔒</span>
                            Your information is secure and protected.
                        </p>

                    </section>

                    <%-- Shipping information --%>
                    <section class="benefit-card">
                        <div class="benefit-icon" aria-hidden="true">
                            🚚
                        </div>

                        <div>
                            <h3>Shipping Information</h3>
                            <p>
                                Shipping fees will be confirmed at checkout.
                            </p>
                        </div>
                    </section>

                    <%-- Return policy --%>
                    <section class="benefit-card">
                        <div class="benefit-icon" aria-hidden="true">
                            ↻
                        </div>

                        <div>
                            <h3>Returns &amp; Exchanges</h3>
                            <p>
                                Contact our store for assistance with returns
                                and exchanges.
                            </p>
                        </div>
                    </section>

                </aside>

            </div>

        </c:otherwise>

    </c:choose>

</main>

<jsp:include page="/views/common/footer.jsp" />

<script>
document.addEventListener("DOMContentLoaded", function () {

    const checkboxes =
        document.querySelectorAll(".product-checkbox");

    const updateTotalForm =
        document.getElementById("updateTotalForm");

    const checkoutForm =
        document.getElementById("proceedCheckoutForm");

    
const selectAllBtn = document.getElementById("selectAllBtn");

// Cập nhật trạng thái nút theo checkbox
function updateSelectAllButton() {
    const checkedCount = Array.from(checkboxes)
        .filter(function (checkbox) {
            return checkbox.checked;
        }).length;

    const allSelected =
        checkboxes.length > 0 &&
        checkedCount === checkboxes.length;

    selectAllBtn.textContent = allSelected
        ? "Bỏ chọn tất cả"
        : "Chọn tất cả";

    selectAllBtn.classList.toggle("selected", allSelected);

    selectAllBtn.setAttribute(
        "aria-pressed",
        String(allSelected)
    );
}

// Nhấn nút để chọn hoặc bỏ chọn tất cả
selectAllBtn.addEventListener("click", function () {
    const allSelected = Array.from(checkboxes)
        .every(function (checkbox) {
            return checkbox.checked;
        });

    checkboxes.forEach(function (checkbox) {
        checkbox.checked = !allSelected;
    });

    updateSelectAllButton();
});

// Cập nhật nút khi người dùng tự chọn từng sản phẩm
checkboxes.forEach(function (checkbox) {
    checkbox.addEventListener("change", updateSelectAllButton);
});

// Khởi tạo đúng trạng thái khi trang tải xong
updateSelectAllButton();


    /*
     * Copy checked product IDs into the target form.
     * This is necessary because the checkboxes are outside both forms.
     */
    function syncSelectedIds(form) {

        // Remove IDs added by a previous submission.
        form.querySelectorAll("[data-selected-id]")
            .forEach(function (input) {
                input.remove();
            });

        let selectedCount = 0;

        checkboxes.forEach(function (checkbox) {

            if (checkbox.checked) {

                const hiddenInput =
                    document.createElement("input");

                hiddenInput.type = "hidden";
                hiddenInput.name = "selectedVariantIds";
                hiddenInput.value = checkbox.value;
                hiddenInput.setAttribute("data-selected-id", "true");

                form.appendChild(hiddenInput);
                selectedCount++;
            }
        });

        return selectedCount;
    }

    // Submit selected product IDs when clicking Update Total.
    updateTotalForm.addEventListener("submit", function (event) {

        const selectedCount = syncSelectedIds(updateTotalForm);

        if (selectedCount === 0) {
            event.preventDefault();
            alert("Please select at least one product.");
        }
    });

    // Submit selected product IDs when proceeding to Checkout.
    checkoutForm.addEventListener("submit", function (event) {

        const selectedCount = syncSelectedIds(checkoutForm);

        if (selectedCount === 0) {
            event.preventDefault();
            alert("Please select at least one product.");
        }
    });

    /*
     * When updating quantity, preserve the current checkbox choices
     * in the form submitted to CartServlet.
     */
    document.querySelectorAll(".quantity-form")
        .forEach(function (form) {

            form.addEventListener("submit", function () {

                form.querySelectorAll("[data-selected-id]")
                    .forEach(function (input) {
                        input.remove();
                    });

                checkboxes.forEach(function (checkbox) {

                    if (checkbox.checked) {

                        const hiddenInput =
                            document.createElement("input");

                        hiddenInput.type = "hidden";
                        hiddenInput.name = "selectedVariantIds";
                        hiddenInput.value = checkbox.value;
                        hiddenInput.setAttribute(
                            "data-selected-id", "true"
                        );

                        form.appendChild(hiddenInput);
                    }
                });
            });
        });

});
</script>

</body>
</html>
