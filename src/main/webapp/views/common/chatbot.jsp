<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>

<!-- Link CSS của Chatbot -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/chatbot.css">

<!-- 1. Icon nổi cố định ở góc dưới phải màn hình -->
<button type="button" class="chatbot-toggler" id="chatToggler" title="Trợ lý tư vấn SportShop">
    <i class="fa-solid fa-comments"></i>
</button>

<!-- 2. Hộp thoại Chatbox -->
<div class="chatbot-box" id="chatBox">
    
    <!-- Tiêu đề Chat -->
    <div class="chatbot-header">
        <div class="chatbot-header-title">
            <i class="fa-solid fa-robot"></i>
            <span>Trợ lý SportShop</span>
        </div>
        <!-- Nút đóng khung chat -->
        <button type="button" class="chatbot-close-btn" id="chatCloseBtn" title="Đóng">&times;</button>
    </div>

    <!-- Vùng hiển thị tin nhắn -->
    <div class="chatbot-messages" id="chatMessages">
        <div class="chat-msg bot">
            Xin chào! 👋 Tôi là trợ lý ảo SportShop. Bạn cần hỗ trợ gì về sản phẩm hoặc đơn hàng ạ?
        </div>
    </div>

    <!-- Ô nhập tin nhắn bọc trong thẻ Form chuẩn HTML -->
    <form class="chatbot-input-area" id="chatForm">
        <input type="text" id="chatInput" placeholder="Nhập câu hỏi..." autocomplete="off" required>
        <button type="submit" id="chatSendBtn">
            <i class="fa-solid fa-paper-plane"></i>
        </button>
    </form>

</div>

<!-- 3. Nhúng file JavaScript điều khiển -->
<script src="${pageContext.request.contextPath}/assets/js/chatbot.js" defer></script>
