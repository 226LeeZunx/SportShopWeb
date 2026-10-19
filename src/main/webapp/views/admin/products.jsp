<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>

        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <title>Products | SportShop Admin</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">
        </head>

        <body class="admin-body">
            <div class="admin-layout">

                <aside class="admin-sidebar">
                    <div class="admin-brand">
                        <span class="admin-brand-mark">S</span>
                        <div>
                            <div class="admin-brand-name">SPORTSHOP</div>
                            <span class="admin-brand-caption">Administration</span>
                        </div>
                    </div>

                    <div class="admin-nav-label">Workspace</div>

                    <nav class="admin-nav">
                        <a href="${pageContext.request.contextPath}/admin/dashboard">
                            <span class="admin-nav-icon">▦</span>Dashboard
                        </a>

                        <a class="active" href="${pageContext.request.contextPath}/admin/products">
                            <span class="admin-nav-icon">▣</span>Products
                        </a>

                        <a href="${pageContext.request.contextPath}/admin/orders">
                            <span class="admin-nav-icon">⇄</span>Orders
                        </a>

                        <a href="${pageContext.request.contextPath}/admin/customers">
                            <span class="admin-nav-icon">♙</span>Customers
                        </a>
                    </nav>

                    <div class="admin-sidebar-bottom">
                        <nav class="admin-nav">
                            <a href="${pageContext.request.contextPath}/HomeServlet">
                                <span class="admin-nav-icon">↗</span>Back to Store
                            </a>
                        </nav>
                    </div>
                </aside>

                <main class="admin-main">

                    <header class="admin-topbar">
                        <div>
                            <h1 class="admin-topbar-title">Products</h1>
                            <p class="admin-topbar-subtitle">
                                Workspace / Product inventory
                            </p>
                        </div>

                        <div class="admin-topbar-actions">
                            <span class="admin-date-label">ADMIN</span>
                        </div>
                    </header>

                    <section class="admin-content">

                        <div class="page-heading-row">
                            <div>
                                <div class="admin-eyebrow">Inventory</div>
                                <h2 class="admin-page-title">Product Management</h2>
                                <p class="admin-page-description">
                                    View products, brands and categories.
                                </p>
                            </div>

                            <a class="admin-button primary"
                                href="${pageContext.request.contextPath}/ProductActionServlet">
                                + Add Product
                            </a>
                        </div>

                        <%-- Create result messages --%>
                            <c:if test="${param.createSuccess == '1'}">
                                <div class="admin-message success" role="status">
                                    Product created successfully.
                                </div>
                            </c:if>

                            <%-- Update result messages --%>
                                <c:if test="${param.updateSuccess == '1'}">
                                    <div class="admin-message success" role="status">
                                        Product information updated successfully.
                                    </div>
                                </c:if>

                                <%-- Delete result messages --%>
                                    <c:if test="${param.deleteSuccess == '1'}">
                                        <div class="admin-message success" role="status">
                                            Product inventory updated successfully. All variants
                                            of this product now have quantity zero.
                                        </div>
                                    </c:if>

                                    <c:if test="${param.deleteError == 'notFound'}">
                                        <div class="admin-message error" role="alert">
                                            The product could not be found.
                                        </div>
                                    </c:if>

                                    <c:if test="${param.deleteError == 'server'}">
                                        <div class="admin-message error" role="alert">

                                            An error occurred while updating product inventory.
                                            Please try again.
                                        </div>
                                    </c:if>

                                    <div class="admin-stats-grid">

                                        <article class="admin-stat-card">
                                            <div class="admin-stat-top">
                                                <span class="admin-stat-label">TOTAL PRODUCTS</span>
                                            </div>

                                            <div class="admin-stat-value">
                                                <c:out value="${totalProducts}" default="—" />
                                            </div>

                                            <div class="admin-stat-foot">
                                                Products matching the current filter
                                            </div>
                                        </article>

                                        <article class="admin-stat-card">
                                            <div class="admin-stat-top">
                                                <span class="admin-stat-label">CATEGORIES</span>
                                            </div>

                                            <div class="admin-stat-value">
                                                <c:choose>
                                                    <c:when test="${not empty categories}">
                                                        <c:out value="${categories.size()}" />
                                                    </c:when>
                                                    <c:otherwise>—</c:otherwise>
                                                </c:choose>
                                            </div>

                                            <div class="admin-stat-foot">
                                                Available categories
                                            </div>
                                        </article>

                                        <article class="admin-stat-card">
                                            <div class="admin-stat-top">
                                                <span class="admin-stat-label">INVENTORY VALUE</span>
                                            </div>

                                            <div class="admin-stat-value">—</div>

                                            <div class="admin-stat-foot">
                                                Not available in Product model
                                            </div>
                                        </article>

                                    </div>

                                    <section class="admin-panel">

                                        <div class="admin-panel-header">
                                            <div>
                                                <h2 class="admin-panel-title">Product List</h2>
                                                <p class="admin-panel-description">
                                                    Product details from the current model.
                                                </p>
                                            </div>
                                        </div>

                                        <%-- Search and category filter --%>
                                            <form class="toolbar"
                                                action="${pageContext.request.contextPath}/admin/products" method="get">

                                                <input type="search" name="keyword" placeholder="Search product name..."
                                                    value="<c:out value='${keyword}'/>">

                                                <select name="category">
                                                    <option value="0" <c:if test="${selectedCategory == 0}">
                                                        selected
                                                        </c:if>>
                                                        All categories
                                                    </option>

                                                    <c:forEach items="${categories}" var="category">
                                                        <option value="${category.categoryId}" <c:if
                                                            test="${selectedCategory == category.categoryId}">
                                                            selected
                                                            </c:if>>
                                                            <c:out value="${category.name}" />
                                                        </option>
                                                    </c:forEach>
                                                </select>

                                                <button class="admin-button primary" type="submit">
                                                    Search
                                                </button>

                                                <a class="admin-button"
                                                    href="${pageContext.request.contextPath}/admin/products">
                                                    Reset
                                                </a>
                                            </form>

                                            <%-- Product table --%>
                                                <div class="admin-table-wrap">
                                                    <table class="admin-table">

                                                        <thead>
                                                            <tr>
                                                                <th>PRODUCT</th>
                                                                <th>ID</th>
                                                                <th>BRAND</th>
                                                                <th>CATEGORY</th>
                                                                <th>QUANTITY</th>
                                                                <th>DESCRIPTION</th>
                                                                <th>ACTIONS</th>
                                                            </tr>
                                                        </thead>

                                                        <tbody>
                                                            <c:choose>

                                                                <c:when test="${not empty products}">
                                                                    <c:forEach items="${products}" var="product">
                                                                        <tr>

                                                                            <td>
                                                                                <div class="product-cell">
                                                                                    <c:choose>
                                                                                        <c:when
                                                                                            test="${not empty product.images}">
                                                                                            <img class="product-thumbnail"
                                                                                                src="<c:out value='${product.images[0].imageUrl}'/>"
                                                                                                alt="Product image">
                                                                                        </c:when>

                                                                                        <c:otherwise>
                                                                                            <div
                                                                                                class="product-thumbnail">
                                                                                            </div>
                                                                                        </c:otherwise>
                                                                                    </c:choose>

                                                                                    <span class="product-name">
                                                                                        <c:out
                                                                                            value="${product.productName}"
                                                                                            default="Unnamed product" />
                                                                                    </span>
                                                                                </div>
                                                                            </td>

                                                                            <td>
                                                                                <c:out value="${product.productId}" />
                                                                            </td>

                                                                            <td>
                                                                                <c:out value="${product.brand.name}"
                                                                                    default="—" />
                                                                            </td>

                                                                            <td>
                                                                                <c:out value="${product.category.name}"
                                                                                    default="—" />
                                                                            </td>
                                                                            <td>
                                                                                <c:out value="${product.quantity}"
                                                                                    default="10" />
                                                                            </td>
                                                                            <td>
                                                                                <c:out value="${product.description}"
                                                                                    default="—" />
                                                                            </td>

                                                                            <td>
                                                                                <a class="admin-button"
                                                                                    href="${pageContext.request.contextPath}/ProductActionServlet?action=edit&amp;id=${product.productId}">
                                                                                    Edit
                                                                                </a>

                                                                                <form
                                                                                    action="${pageContext.request.contextPath}/admin/products"
                                                                                    method="post"
                                                                                    onsubmit="return confirm('Set all variants of this product quantity to zero?');">

                                                                                    <input type="hidden" name="action"
                                                                                        value="delete">

                                                                                    <input type="hidden"
                                                                                        name="productId"
                                                                                        value="${product.productId}">

                                                                                    <button class="admin-button"
                                                                                        type="submit">
                                                                                        Delete
                                                                                    </button>
                                                                                </form>
                                                                            </td>

                                                                        </tr>
                                                                    </c:forEach>
                                                                </c:when>

                                                                <c:otherwise>
                                                                    <tr>
                                                                        <td colspan="7">
                                                                            <div class="empty-message">
                                                                                No products found matching your search.
                                                                            </div>
                                                                        </td>
                                                                    </tr>
                                                                </c:otherwise>

                                                            </c:choose>
                                                        </tbody>

                                                    </table>
                                                </div>

                                                <%-- Pagination --%>
                                                    <div class="pagination">

                                                        <span>
                                                            Page
                                                            <c:out value="${currentPage}" />
                                                            of
                                                            <c:out value="${totalPages}" />
                                                            —
                                                            <c:out value="${totalProducts}" /> products
                                                        </span>

                                                        <div class="pagination-actions">

                                                            <c:if test="${currentPage > 1}">
                                                                <c:url var="previousPageUrl" value="/admin/products">
                                                                    <c:param name="page" value="${currentPage - 1}" />
                                                                    <c:param name="keyword" value="${keyword}" />
                                                                    <c:param name="category"
                                                                        value="${selectedCategory}" />
                                                                </c:url>

                                                                <a class="admin-button"
                                                                    href="${pageContext.request.contextPath}${previousPageUrl}">
                                                                    Previous
                                                                </a>
                                                            </c:if>

                                                            <c:if test="${currentPage < totalPages}">
                                                                <c:url var="nextPageUrl" value="/admin/products">
                                                                    <c:param name="page" value="${currentPage + 1}" />
                                                                    <c:param name="keyword" value="${keyword}" />
                                                                    <c:param name="category"
                                                                        value="${selectedCategory}" />
                                                                </c:url>

                                                                <a class="admin-button"
                                                                    href="${pageContext.request.contextPath}${nextPageUrl}">
                                                                    Next
                                                                </a>
                                                            </c:if>

                                                        </div>
                                                    </div>

                                    </section>
                    </section>
                </main>
            </div>
        </body>

        </html>