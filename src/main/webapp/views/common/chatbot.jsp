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
            <span>Trợ lý SportShop (RAG AI)</span>
        </div>
        <!-- Nút đóng khung chat -->
        <button type="button" class="chatbot-close-btn" id="chatCloseBtn" title="Đóng">&times;</button>
    </div>

    <!-- Vùng hiển thị tin nhắn -->
    <div class="chatbot-messages" id="chatMessages">
        <div class="chat-msg bot">
            Xin chào!  Tôi là trợ lý ảo SportShop. Bạn cần hỗ trợ gì về sản phẩm hoặc đơn hàng ạ?
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

<!-- 3. JavaScript điều khiển kéo thả và kết nối AJAX tới Servlet RAG -->
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

        if (Math.abs(dx) > 5 || Math.abs(dy) > 5) {
            moved = true;

            var newLeft = initialLeft + dx;
            var newTop = initialTop + dy;

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

        if (!moved) {
            toggleChatBox();
        }
    }

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
    // LOGIC MỞ / ĐÓNG VÀ GỬI TIN NHẮN QUA AJAX (SERVLET RAG)
    // ==========================================
    function toggleChatBox() {
        box.classList.toggle('show');
        if (box.classList.contains('show')) {
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

        // 2. Tạo hiệu ứng "Đang trả lời..." (Typing / Loading)
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
        .then(response => response.text())
        .then(html => {
            // Xóa thông báo đang chờ
            messages.removeChild(loadingMsg);

            // Vì Servlet trả về toàn bộ trang HTML (hoặc có thể tối ưu trả về JSON), 
            // ta dùng DOMParser để trích xuất phần kết quả trả lời từ Servlet
            var parser = new DOMParser();
            var doc = parser.parseFromString(html, 'text/html');
            
            // Tìm thẻ chứa câu trả lời từ response của Servlet
            // (Dựa theo cấu trúc trang rag-chat.jsp cũ hoặc ta lấy đoạn text kết quả)
            var answerContainer = doc.querySelector('.alert-success p') || doc.querySelector('.alert-secondary p');
            var answerText = answerContainer ? answerContainer.innerText.trim() : "Xin lỗi, hệ thống không thể phản hồi lúc này.";

            // 4. Hiển thị câu trả lời thực tế từ RAG AI lên khung chat
            var botMsg = document.createElement('div');
            botMsg.className = 'chat-msg bot';
            botMsg.style.whiteSpace = 'pre-line';
            botMsg.innerText = answerText;
            messages.appendChild(botMsg);
            messages.scrollTop = messages.scrollHeight;
        })
        .catch(error => {
            messages.removeChild(loadingMsg);
            var errorMsg = document.createElement('div');
            errorMsg.className = 'chat-msg bot';
            errorMsg.innerText = 'Lỗi kết nối tới máy chủ AI.';
            messages.appendChild(errorMsg);
            messages.scrollTop = messages.scrollHeight;
            console.error('Error:', error);
        });
    }

    sendBtn.addEventListener('click', sendMessage);

    input.addEventListener('keydown', function(e) {
        if (e.key === 'Enter') {
            sendMessage();
        }
    });
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
    .then(response => response.text())
    .then(answerText => {
        // Xóa thông báo đang chờ
        messages.removeChild(loadingMsg);

        // 4. Hiển thị trực tiếp câu trả lời từ RAG AI
        var botMsg = document.createElement('div');
        botMsg.className = 'chat-msg bot';
        botMsg.style.whiteSpace = 'pre-line';
        botMsg.innerText = answerText ? answerText : "Xin lỗi, hệ thống không thể phản hồi lúc này.";
        messages.appendChild(botMsg);
        messages.scrollTop = messages.scrollHeight;
    })
    .catch(error => {
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

})();
</script>
