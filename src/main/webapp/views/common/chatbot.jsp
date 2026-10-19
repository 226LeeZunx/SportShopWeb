<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>

<!-- Link CSS của Chatbot -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/chatbot.css">

<!-- 1. Icon nổi có thể kéo thả di chuyển -->
<div class="chatbot-toggler" id="chatToggler" title="Bấm để chat, giữ chuột để kéo di chuyển">
    <i class="fa-solid fa-comments"></i>
</div>

<!-- 2. Hộp thoại Chatbox -->
<div class="chatbot-box" id="chatBox">
    
    <!-- Tiêu đề Chat -->
    <div class="chatbot-header">
        <div class="chatbot-header-title">
            <i class="fa-solid fa-robot"></i>
            <span>Trợ lý SportShop</span>
        </div>
        <button type="button" class="chatbot-close-btn" id="chatCloseBtn">&times;</button>
    </div>

    <!-- Vùng hiển thị tin nhắn -->
    <div class="chatbot-messages" id="chatMessages">
        <div class="chat-msg bot">
            Xin chào! 👋 Tôi là trợ lý ảo SportShop. Bạn cần hỗ trợ gì về sản phẩm hoặc đơn hàng ạ?
        </div>
    </div>

    <!-- Ô nhập tin nhắn -->
    <div class="chatbot-input-area">
        <input type="text" id="chatInput" placeholder="Nhập câu hỏi..." autocomplete="off">
        <button type="button" id="chatSendBtn">
            <i class="fa-solid fa-paper-plane"></i>
        </button>
    </div>

</div>

<!-- 3. JavaScript điều khiển kéo thả và gửi tin nhắn (Viết thuần đơn giản) -->
<script>
(function() {
    var toggler = document.getElementById('chatToggler');
    var box = document.getElementById('chatBox');
    var closeBtn = document.getElementById('chatCloseBtn');
    var input = document.getElementById('chatInput');
    var sendBtn = document.getElementById('chatSendBtn');
    var messages = document.getElementById('chatMessages');

    // ==========================================
    // LOGIC KÉO THẢ ICON ĐƠN GIẢN (DRAG & DROP)
    // ==========================================
    var isDragging = false;
    var startX = 0, startY = 0;
    var initialLeft = 0, initialTop = 0;
    var moved = false;

    toggler.addEventListener('mousedown', function(e) {
        if (e.button !== 0) return; // Chỉ nhận chuột trái
        isDragging = true;
        moved = false;

        startX = e.clientX;
        startY = e.clientY;

        var rect = toggler.getBoundingClientRect();
        initialLeft = rect.left;
        initialTop = rect.top;

        document.addEventListener('mousemove', onMouseMove);
        document.addEventListener('mouseup', onMouseUp);
    });

    function onMouseMove(e) {
        if (!isDragging) return;

        var dx = e.clientX - startX;
        var dy = e.clientY - startY;

        // Nếu di chuyển quá 5px thì mới tính là Kéo
        if (Math.abs(dx) > 5 || Math.abs(dy) > 5) {
            moved = true;

            var newLeft = initialLeft + dx;
            var newTop = initialTop + dy;

            // Giữ icon không lọt ra ngoài màn hình
            var maxLeft = window.innerWidth - toggler.offsetWidth - 10;
            var maxTop = window.innerHeight - toggler.offsetHeight - 10;

            if (newLeft < 10) newLeft = 10;
            if (newLeft > maxLeft) newLeft = maxLeft;
            if (newTop < 10) newTop = 10;
            if (newTop > maxTop) newTop = maxTop;

            toggler.style.left = newLeft + 'px';
            toggler.style.top = newTop + 'px';
            toggler.style.bottom = 'auto';
            toggler.style.right = 'auto';
        }
    }

    function onMouseUp(e) {
        if (!isDragging) return;
        isDragging = false;

        document.removeEventListener('mousemove', onMouseMove);
        document.removeEventListener('mouseup', onMouseUp);

        // Nếu KHÔNG kéo (di chuyển < 5px) -> Tính là bấm CLICK để bật/tắt chatbox
        if (!moved) {
            toggleChatBox();
        }
    }

    // Hỗ trợ cả chạm tay trên màn hình điện thoại (Touch)
    toggler.addEventListener('touchstart', function(e) {
        isDragging = true;
        moved = false;
        startX = e.touches[0].clientX;
        startY = e.touches[0].clientY;
        var rect = toggler.getBoundingClientRect();
        initialLeft = rect.left;
        initialTop = rect.top;
    }, {passive: true});

    toggler.addEventListener('touchmove', function(e) {
        if (!isDragging) return;
        var dx = e.touches[0].clientX - startX;
        var dy = e.touches[0].clientY - startY;
        if (Math.abs(dx) > 5 || Math.abs(dy) > 5) {
            moved = true;
            toggler.style.left = (initialLeft + dx) + 'px';
            toggler.style.top = (initialTop + dy) + 'px';
            toggler.style.bottom = 'auto';
            toggler.style.right = 'auto';
        }
    }, {passive: true});

    toggler.addEventListener('touchend', function() {
        isDragging = false;
        if (!moved) {
            toggleChatBox();
        }
    });

    // ==========================================
    // LOGIC MỞ / ĐÓNG VÀ GỬI TIN NHẮN
    // ==========================================
    function toggleChatBox() {
        box.classList.toggle('show');
        if (box.classList.contains('show')) {
            // Canh vị trí hộp chat gần icon
            var rect = toggler.getBoundingClientRect();
            var boxWidth = box.offsetWidth || 340;
            var boxHeight = box.offsetHeight || 450;

            var left = rect.right - boxWidth;
            var top = rect.top - boxHeight - 10;

            if (top < 10) top = rect.bottom + 10;
            if (left < 10) left = 10;

            box.style.left = left + 'px';
            box.style.top = top + 'px';
            box.style.bottom = 'auto';
            box.style.right = 'auto';

            input.focus();
        }
    }

    closeBtn.addEventListener('click', function() {
        box.classList.remove('show');
    });

    // Gửi tin nhắn
    function sendMessage() {
        var text = input.value.trim();
        if (!text) return;

        // 1. Hiện tin nhắn của người dùng
        var userMsg = document.createElement('div');
        userMsg.className = 'chat-msg user';
        userMsg.innerText = text;
        messages.appendChild(userMsg);
        input.value = '';
        messages.scrollTop = messages.scrollHeight;

        // 2. Tự động trả lời đơn giản sau 500ms
        setTimeout(function() {
            var botMsg = document.createElement('div');
            botMsg.className = 'chat-msg bot';
            botMsg.innerText = 'Cảm ơn bạn đã nhắn tin! Hiện tại hệ thống đang kết nối tư vấn viên, bạn có thể tham khảo danh mục ở menu bên trái nhé.';
            messages.appendChild(botMsg);
            messages.scrollTop = messages.scrollHeight;
        }, 500);
    }

    sendBtn.addEventListener('click', sendMessage);

    input.addEventListener('keydown', function(e) {
        if (e.key === 'Enter') {
            sendMessage();
        }
    });

})();
</script>
