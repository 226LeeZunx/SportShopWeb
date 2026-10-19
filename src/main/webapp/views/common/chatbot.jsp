<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>

<!-- Link CSS của Chatbot -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/chatbot.css">

<!-- 1. Icon nổi CỐ ĐỊNH ở góc dưới phải màn hình -->
<button type="button" class="chatbot-toggler" id="chatToggler" title="Trợ lý tư vấn SportShop">
    <i class="fa-solid fa-comments"></i>
</button>

<!-- 2. Hộp thoại Chatbox CỐ ĐỊNH ngay phía trên icon -->
<div class="chatbot-box" id="chatBox">
    
    <!-- Tiêu đề Chat -->
    <div class="chatbot-header">
        <div class="chatbot-header-title">
            <i class="fa-solid fa-robot"></i>
            <span>Trợ lý SportShop (RAG AI)</span>
        </div>
        <!-- Nút đóng khung chat -->
        <button type="button" class="chatbot-close-btn" id="chatCloseBtn" title="Đóng">&times;</button>
    </div>

    <!-- Vùng hiển thị tin nhắn -->
    <div class="chatbot-messages" id="chatMessages">
        <div class="chat-msg bot">
            Xin chào! Tôi là trợ lý ảo SportShop. Bạn cần hỗ trợ gì về sản phẩm hoặc đơn hàng ạ?
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

<!-- 3. JavaScript điều khiển Bật/Tắt CỐ ĐỊNH và kết nối AJAX tới Servlet RAG -->
<script>
(function() {
    var toggler = document.getElementById('chatToggler');
    var box = document.getElementById('chatBox');
    var closeBtn = document.getElementById('chatCloseBtn');
    var input = document.getElementById('chatInput');
    var form = document.getElementById('chatForm');
    var messages = document.getElementById('chatMessages');

    if (!toggler || !box) return;

    // Bật / Tắt khung chat cố định
    toggler.addEventListener('click', function(e) {
        e.stopPropagation();
        var isOpen = box.classList.toggle('show');
        if (isOpen && input) {
            setTimeout(function() {
                input.focus();
            }, 150);
        }
    });

    // Nút đóng khung chat
    if (closeBtn) {
        closeBtn.addEventListener('click', function(e) {
            e.stopPropagation();
            box.classList.remove('show');
        });
    }

    // Gửi tin nhắn bằng Fetch API kết nối tới Java Servlet (/rag-chat)
    function sendMessage() {
        var text = input.value.trim();
        if (!text) return;

        // 1. Hiển thị tin nhắn của người dùng lên khung chat
        var userMsg = document.createElement('div');
        userMsg.className = 'chat-msg user';
        userMsg.innerText = text;
        messages.appendChild(userMsg);
        input.value = '';
        messages.scrollTop = messages.scrollHeight;

        // 2. Tạo hiệu ứng "Đang tìm kiếm..."
        var loadingMsg = document.createElement('div');
        loadingMsg.className = 'chat-msg bot loading';
        loadingMsg.innerText = 'Đang tìm kiếm thông tin sản phẩm... ⏳';
        messages.appendChild(loadingMsg);
        messages.scrollTop = messages.scrollHeight;

        // 3. Gửi yêu cầu ngầm bằng Fetch tới Servlet RAGChatServlet
        fetch('${pageContext.request.contextPath}/rag-chat', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
            },
            body: 'query=' + encodeURIComponent(text)
        })
        .then(function(response) {
            return response.text();
        })
        .then(function(answerText) {
            if (loadingMsg.parentNode) {
                messages.removeChild(loadingMsg);
            }
            var botMsg = document.createElement('div');
            botMsg.className = 'chat-msg bot';
            botMsg.style.whiteSpace = 'pre-line';
            botMsg.innerText = answerText ? answerText.trim() : "Xin lỗi, hệ thống không thể phản hồi lúc này.";
            messages.appendChild(botMsg);
            messages.scrollTop = messages.scrollHeight;
        })
        .catch(function(error) {
            if (loadingMsg.parentNode) {
                messages.removeChild(loadingMsg);
            }
            var errorMsg = document.createElement('div');
            errorMsg.className = 'chat-msg bot';
            errorMsg.innerText = 'Lỗi kết nối tới máy chủ AI.';
            messages.appendChild(errorMsg);
            messages.scrollTop = messages.scrollHeight;
            console.error('Error:', error);
        });
    }

    if (form) {
        form.addEventListener('submit', function(e) {
            e.preventDefault();
            sendMessage();
        });
    }
})();
</script>
