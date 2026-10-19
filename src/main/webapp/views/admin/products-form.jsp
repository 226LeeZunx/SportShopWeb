```jsp
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>

        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <title>
                <c:choose>
                    <c:when test="${editMode}">Edit Product</c:when>
                    <c:otherwise>Add Product</c:otherwise>
                </c:choose>
                | SportShop Admin
            </title>
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
                            <h1 class="admin-topbar-title">
                                <c:choose>
                                    <c:when test="${editMode}">Edit Product</c:when>
                                    <c:otherwise>Add Product</c:otherwise>
                                </c:choose>
                            </h1>

                            <p class="admin-topbar-subtitle">
                                Workspace / Products /
                                <c:choose>
                                    <c:when test="${editMode}">Edit</c:when>
                                    <c:otherwise>Add</c:otherwise>
                                </c:choose>
                            </p>
                        </div>

                        <div class="admin-topbar-actions">
                            <span class="admin-date-label">ADMIN</span>
                        </div>
                    </header>

                    <section class="admin-content">

                        <div class="page-heading-row">
                            <div>
                                <h2 class="admin-page-title">
                                    <c:choose>
                                        <c:when test="${editMode}">Update Product Information</c:when>
                                        <c:otherwise>Product Information</c:otherwise>
                                    </c:choose>
                                </h2>

                                <p class="admin-page-description">
                                    <c:choose>
                                        <c:when test="${editMode}">
                                            Update the selected product's information.
                                        </c:when>
                                        <c:otherwise>
                                            Enter information for a new product.
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                            </div>

                            <a class="admin-button" href="${pageContext.request.contextPath}/admin/products">
                                Back to Products
                            </a>
                        </div>

                        <section class="admin-panel">

                            <c:if test="${not empty error}">
                                <div class="form-error" role="alert">
                                    <c:out value="${error}" />
                                </div>
                            </c:if>

                            <form class="admin-form" action="${pageContext.request.contextPath}/ProductActionServlet"
                                method="post" enctype="multipart/form-data">

                                <%-- Xác định thao tác Create hay Update. --%>
                                    <input type="hidden" name="action" value="${editMode ? 'update' : 'create'}">

                                    <%-- Chỉ cần productId khi đang chỉnh sửa. --%>
                                        <c:if test="${editMode}">
                                            <input type="hidden" name="productId" value="${product.productId}">
                                        </c:if>

                                        <div class="admin-form-grid">

                                            <div class="admin-form-group full">
                                                <label for="productName">Product name *</label>

                                                <input id="productName" name="productName" type="text" maxlength="150"
                                                    required
                                                    value="<c:out value='${param.productName != null ? param.productName : product.productName}'/>"
                                                    placeholder="Enter product name">
                                            </div>

                                            <div class="admin-form-group">
                                                <label for="brandId">Brand *</label>

                                                <select id="brandId" name="brandId" required>
                                                    <option value="">Select a brand</option>

                                                    <c:forEach items="${brands}" var="brand">
                                                        <option value="${brand.brandId}" <c:if
                                                            test="${param.brandId != null ? param.brandId == brand.brandId : product.brand.brandId == brand.brandId}">
                                                            selected
                                                            </c:if>>
                                                            <c:out value="${brand.name}" />
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                            </div>

                                            <div class="admin-form-group">
                                                <label for="categoryId">Category *</label>

                                                <select id="categoryId" name="categoryId" required>
                                                    <option value="">Select a category</option>

                                                    <c:forEach items="${categories}" var="category">
                                                        <option value="${category.categoryId}" <c:if
                                                            test="${param.categoryId != null ? param.categoryId == category.categoryId : product.category.categoryId == category.categoryId}">
                                                            selected
                                                            </c:if>>
                                                            <c:out value="${category.name}" />
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                            </div>

                                            <div class="admin-form-group">
                                                <label for="quantity">Quantity *</label>

                                                <input id="quantity" name="quantity" type="number" min="0" step="1"
                                                    required
                                                    value="${param.quantity != null ? param.quantity : product.quantity}"
                                                    placeholder="Enter product quantity">

                                                <span class="help-text">
                                                    Enter a non-negative whole number.
                                                </span>
                                            </div>

                                            <div class="admin-form-group full">
                                                <label for="description">Description</label>

                                                <textarea id="description" name="description"
                                                    placeholder="Describe the product"><c:out value="${param.description != null ? param.description : product.description}"/></textarea>
                                            </div>

                                            <div class="admin-form-group full">
                                                <label for="images">Product images</label>

                                                <input id="images" name="images" type="file" accept="image/*" multiple>

                                                <span class="help-text">
                                                    <c:choose>
                                                        <c:when test="${editMode}">
                                                            Select new images only if you want to upload additional
                                                            images.
                                                            Leaving this field empty will not request new uploads.
                                                        </c:when>
                                                        <c:otherwise>
                                                            Select one or more image files.
                                                        </c:otherwise>
                                                    </c:choose>
                                                </span>
                                            </div>

                                        </div>

                                        <div class="form-actions">
                                            <a class="admin-button"
                                                href="${pageContext.request.contextPath}/admin/products">
                                                Cancel
                                            </a>

                                            <button class="admin-button primary" type="submit">
                                                <c:choose>
                                                    <c:when test="${editMode}">Save Changes</c:when>
                                                    <c:otherwise>Create Product</c:otherwise>
                                                </c:choose>
                                            </button>
                                        </div>

                            </form>
                        </section>
                    </section>
                </main>
            </div>
        </body>

        </html>
        ```