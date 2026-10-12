<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <title>Add Product | SportShop Admin</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">

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
                        <a class="active" href="${pageContext.request.contextPath}/admin/products"><span
                                class="admin-nav-icon">▣</span>Products</a>
                        <a href="${pageContext.request.contextPath}/admin/orders"><span
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
                            <h1 class="admin-topbar-title">Add Product</h1>
                            <p class="admin-topbar-subtitle">Workspace / Products / Add</p>
                        </div>
                        <div class="admin-topbar-actions"><span class="admin-date-label">ADMIN</span></div>
                    </header>

                    <section class="admin-content">
                        <div class="page-heading-row">
                            <div>
                                <h2 class="admin-page-title">Product Information</h2>
                                <p class="admin-page-description">Enter information for a new product.</p>
                            </div>
                            <a class="admin-button" href="${pageContext.request.contextPath}/admin/products">Back to
                                Products</a>
                        </div>

                        <section class="admin-panel">
                            <c:if test="${not empty error}">
                                <div class="form-error" role="alert">
                                    <c:out value="${error}" />
                                </div>
                            </c:if>

                            <form class="admin-form" action="${pageContext.request.contextPath}/ProductActionServlet"
                                method="post" enctype="multipart/form-data">
                                <div class="admin-form-grid">
                                    <div class="admin-form-group full">
                                        <label for="productName">Product name *</label>
                                        <input id="productName" name="productName" type="text" maxlength="150" required
                                            value="<c:out value='${param.productName}'/>"
                                            placeholder="Enter product name">
                                    </div>

                                    <div class="admin-form-group">
                                        <label for="brandId">Brand *</label>
                                        <select id="brandId" name="brandId" required>
                                            <option value="">Select a brand</option>
                                            <c:forEach items="${brands}" var="brand">
                                                <option value="${brand.brandId}" <c:if
                                                    test="${param.brandId == brand.brandId}">selected</c:if>>
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
                                                    test="${param.categoryId == category.categoryId}">selected</c:if>>
                                                    <c:out value="${category.name}" />
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="admin-form-group full">
                                        <label for="description">Description</label>
                                        <textarea id="description" name="description"
                                            placeholder="Describe the product"><c:out value="${param.description}"/></textarea>
                                    </div>

                                    <div class="admin-form-group full">
                                        <label for="images">Product images</label>
                                        <input id="images" name="images" type="file" accept="image/*" multiple>
                                        <span class="help-text">Select one or more image files. The existing Servlet
                                            reads the field named <code>images</code>.</span>
                                    </div>
                                </div>

                                <div class="form-actions">
                                    <a class="admin-button"
                                        href="${pageContext.request.contextPath}/admin/products">Cancel</a>
                                    <button class="admin-button primary" type="submit">Create Product</button>
                                </div>
                            </form>
                        </section>
                    </section>
                </main>
            </div>
        </body>

        </html>