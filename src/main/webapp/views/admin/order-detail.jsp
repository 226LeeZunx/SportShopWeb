<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1">
                <title>Order Details | SportShop Admin</title>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">
                <style>
                    .page-heading-row {
                        display: flex;
                        justify-content: space-between;
                        align-items: center;
                        gap: 14px;
                        flex-wrap: wrap;
                        margin-bottom: 24px
                    }

                    .admin-button {
                        display: inline-flex;
                        padding: 10px 14px;
                        border: 1px solid #292929;
                        border-radius: 9px;
                        background: #1b1b1b;
                        color: #f5f5f5 !important
                    }

                    .detail-grid {
                        display: grid;
                        grid-template-columns: repeat(2, minmax(0, 1fr));
                        gap: 18px;
                        margin-bottom: 20px
                    }

                    .detail-row {
                        display: flex;
                        justify-content: space-between;
                        gap: 16px;
                        padding: 12px 0;
                        border-bottom: 1px solid #292929
                    }

                    .detail-label {
                        color: #929292
                    }

                    .detail-value {
                        text-align: right;
                        overflow-wrap: anywhere
                    }

                    .empty-message {
                        padding: 24px;
                        text-align: center;
                        color: #929292
                    }

                    @media(max-width:700px) {
                        .detail-grid {
                            grid-template-columns: 1fr
                        }

                        .detail-row {
                            flex-direction: column;
                            gap: 5px
                        }

                        .detail-value {
                            text-align: left
                        }
                    }
                </style>
            </head>

            <body class="admin-body">
                <div class="admin-layout">
                    <aside class="admin-sidebar">
                        <div class="admin-brand"><span class="admin-brand-mark">S</span>
                            <div>
                                <div class="admin-brand-name">SPORTSHOP</div><span
                                    class="admin-brand-caption">Administration</span>
                            </div>
                        </div>
                        <div class="admin-nav-label">Workspace</div>
                        <nav class="admin-nav">
                            <a href="${pageContext.request.contextPath}/admin/dashboard"><span
                                    class="admin-nav-icon">▦</span>Dashboard</a>
                            <a href="${pageContext.request.contextPath}/admin/products"><span
                                    class="admin-nav-icon">▣</span>Products</a>
                            <a class="active" href="${pageContext.request.contextPath}/admin/orders"><span
                                    class="admin-nav-icon">⇄</span>Orders</a>
                            <a href="${pageContext.request.contextPath}/admin/customers"><span
                                    class="admin-nav-icon">♙</span>Customers</a>
                        </nav>
                        <div class="admin-sidebar-bottom">
                            <nav class="admin-nav"><a href="${pageContext.request.contextPath}/HomeServlet"><span
                                        class="admin-nav-icon">↗</span>Back to Store</a></nav>
                        </div>
                    </aside>

                    <main class="admin-main">
                        <header class="admin-topbar">
                            <div>
                                <h1 class="admin-topbar-title">Order Details</h1>
                                <p class="admin-topbar-subtitle">Workspace / Orders / Details</p>
                            </div>
                            <div class="admin-topbar-actions"><span class="admin-date-label">ADMIN</span></div>
                        </header>
                        <section class="admin-content">
                            <div class="page-heading-row">
                                <div>
                                    <h2 class="admin-page-title">
                                        <c:choose>
                                            <c:when test="${not empty order}">Order #
                                                <c:out value="${order.orderId}" />
                                            </c:when>
                                            <c:otherwise>Order unavailable</c:otherwise>
                                        </c:choose>
                                    </h2>
                                    <p class="admin-page-description">Order and customer information.</p>
                                </div>
                                <a class="admin-button" href="${pageContext.request.contextPath}/admin/orders">Back to
                                    Orders</a>
                            </div>

                            <c:choose>
                                <c:when test="${not empty order}">
                                    <div class="detail-grid">
                                        <section class="admin-panel">
                                            <div class="admin-panel-header">
                                                <h2 class="admin-panel-title">Customer Information</h2>
                                            </div>
                                            <div class="detail-row"><span class="detail-label">Customer ID</span><span
                                                    class="detail-value">
                                                    <c:out value="${order.customer.customerId}" default="—" />
                                                </span></div>
                                            <div class="detail-row"><span class="detail-label">Name</span><span
                                                    class="detail-value">
                                                    <c:out value="${order.customer.firstName}" default="" />
                                                    <c:out value="${order.customer.lastName}" default="" />
                                                </span></div>
                                            <div class="detail-row"><span class="detail-label">Email</span><span
                                                    class="detail-value">
                                                    <c:out value="${order.customer.user.email}" default="—" />
                                                </span></div>
                                            <div class="detail-row"><span class="detail-label">Phone</span><span
                                                    class="detail-value">
                                                    <c:out value="${order.customer.user.phone}" default="—" />
                                                </span></div>
                                        </section>
                                        <section class="admin-panel">
                                            <div class="admin-panel-header">
                                                <h2 class="admin-panel-title">Order Information</h2>
                                            </div>
                                            <div class="detail-row"><span class="detail-label">Order ID</span><span
                                                    class="detail-value">#
                                                    <c:out value="${order.orderId}" />
                                                </span></div>
                                            <div class="detail-row"><span class="detail-label">Item lines</span><span
                                                    class="detail-value">
                                                    <c:choose>
                                                        <c:when test="${not empty order.orderItems}">
                                                            <c:out value="${order.orderItems.size()}" />
                                                        </c:when>
                                                        <c:otherwise>0</c:otherwise>
                                                    </c:choose>
                                                </span></div>
                                            <div class="detail-row"><span class="detail-label">Order date</span><span
                                                    class="detail-value">Not available in current model</span></div>
                                            <div class="detail-row"><span class="detail-label">Status</span><span
                                                    class="detail-value">Not available in current model</span></div>
                                        </section>
                                    </div>

                                    <section class="admin-panel">
                                        <div class="admin-panel-header">
                                            <h2 class="admin-panel-title">Order Items</h2>
                                        </div>
                                        <div class="admin-table-wrap">
                                            <table class="admin-table">
                                                <thead>
                                                    <tr>
                                                        <th>PRODUCT</th>
                                                        <th>VARIANT</th>
                                                        <th>UNIT PRICE</th>
                                                        <th>QUANTITY</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:choose>
                                                        <c:when test="${not empty order.orderItems}">
                                                            <c:forEach items="${order.orderItems}" var="item">
                                                                <tr>
                                                                    <td>
                                                                        <c:out
                                                                            value="${item.productVariant.product.productName}"
                                                                            default="Product unavailable" />
                                                                    </td>
                                                                    <td>
                                                                        <c:out
                                                                            value="${item.productVariant.variantName}"
                                                                            default="—" />
                                                                    </td>
                                                                    <td>
                                                                        <fmt:formatNumber
                                                                            value="${item.productVariant.price}"
                                                                            type="number" /> VND
                                                                    </td>
                                                                    <td>
                                                                        <c:out value="${item.quantity}" />
                                                                    </td>
                                                                </tr>
                                                            </c:forEach>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <tr>
                                                                <td colspan="4">
                                                                    <div class="empty-message">No order items available.
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </tbody>
                                            </table>
                                        </div>
                                    </section>
                                </c:when>
                                <c:otherwise>
                                    <section class="admin-panel">
                                        <div class="empty-message">The Admin Controller must provide the request
                                            attribute <code>order</code>.</div>
                                    </section>
                                </c:otherwise>
                            </c:choose>
                        </section>
                    </main>
                </div>
            </body>

            </html>