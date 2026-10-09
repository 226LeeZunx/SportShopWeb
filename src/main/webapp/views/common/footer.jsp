<%-- 
    Document   : footer
    Created on : 9 Oct 2026, 11:44:54 AM
    Author     : Anh Tuan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Khung Footer dùng chung - Phong cách thể thao Dark Luxury -->
<footer class="site-footer">
    <div class="footer-container">
        
        <div class="footer-top">
            <!-- Cột 1: Thông tin thương hiệu & Slogan -->
            <div class="footer-col">
                <h4>SPORTSHOP</h4>
                <p>Elevate your performance. Authentic athletic footwear, premium sportswear, and training gear from the world's leading sports brands.</p>
                <p class="footer-highlight">Authentic. Premium. Performance.</p>
            </div>

            <!-- Cột 2: Danh mục sản phẩm nhanh -->
            <div class="footer-col">
                <h4>Categories</h4>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/HomeServlet?cat=running">Running & Training</a></li>
                    <li><a href="${pageContext.request.contextPath}/HomeServlet?cat=football">Football Gear</a></li>
                    <li><a href="${pageContext.request.contextPath}/HomeServlet?cat=basketball">Basketball</a></li>
                    <li><a href="${pageContext.request.contextPath}/HomeServlet?cat=gym">Gym & Fitness</a></li>
                    <li><a href="${pageContext.request.contextPath}/HomeServlet?cat=sale">Special Offers</a></li>
                </ul>
            </div>

            <!-- Cột 3: Hỗ trợ khách hàng -->
            <div class="footer-col">
                <h4>Customer Care</h4>
                <ul>
                    <li><a href="#">Size Guide</a></li>
                    <li><a href="#">7-Day Return Policy</a></li>
                    <li><a href="#">Shipping & Delivery</a></li>
                    <li><a href="#">Track Your Order</a></li>
                    <li><a href="#">Help & FAQs</a></li>
                </ul>
            </div>

            <!-- Cột 4: Đăng ký nhận tin khuyến mãi -->
            <div class="footer-col">
                <h4>Stay In The Game</h4>
                <p>Subscribe to get 10% off your first kit and early access to limited edition drops.</p>
                <form class="newsletter-form" onsubmit="event.preventDefault();">
                    <input type="email" class="newsletter-input" placeholder="Enter your email..." required>
                    <button type="submit" class="newsletter-btn">Subscribe</button>
                </form>
            </div>
        </div>

        <!-- Dòng bản quyền & chính sách dưới cùng -->
        <div class="footer-bottom">
            <div>&copy; 2026 SPORTSHOP. All rights reserved.</div>
            <div>Privacy Policy &bull; Terms of Service &bull; Secure Checkout</div>
        </div>

    </div>
</footer>
