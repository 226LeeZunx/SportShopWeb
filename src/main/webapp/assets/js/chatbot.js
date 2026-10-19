/**
 * chatbot.js - Điều khiển giao diện Chatbot SportShop & Gửi tin nhắn
 */
(function () {
    var toggler = document.getElementById('chatToggler');
    var box = document.getElementById('chatBox');
    var closeBtn = document.getElementById('chatCloseBtn');
    var chatForm = document.getElementById('chatForm');
    var input = document.getElementById('chatInput');
    var messages = document.getElementById('chatMessages');

    if (!toggler || !box) return;

    // 1. Bật / Tắt khung chat
    toggler.addEventListener('click', function (e) {
        e.stopPropagation();
        var isOpen = box.classList.toggle('show');
        if (isOpen && input) {
            setTimeout(function () {
                input.focus();
            }, 100);
        }
    });

    // 2. Đóng khung chat khi bấm nút ×
    if (closeBtn) {
        closeBtn.addEventListener('click', function (e) {
            e.stopPropagation();
            box.classList.remove('show');
        });
    }

    // 3. Xử lý gửi tin nhắn qua Form
    if (chatForm && input && messages) {
        chatForm.addEventListener('submit', function (e) {
            e.preventDefault();

            var text = input.value.trim();
            if (!text) return;

            // Tin nhắn của khách hàng
            var userMsg = document.createElement('div');
            userMsg.className = 'chat-msg user';
            userMsg.textContent = text;
            messages.appendChild(userMsg);

            input.value = '';
            messages.scrollTop = messages.scrollHeight;

            // Phản hồi tự động sau 500ms
            setTimeout(function () {
                var botMsg = document.createElement('div');
                botMsg.className = 'chat-msg bot';
                botMsg.textContent = 'Cảm ơn bạn đã nhắn tin! Hiện tại tư vấn viên đang bận, bạn vui lòng xem thêm thông tin sản phẩm trên website hoặc để lại số điện thoại nhé.';
                messages.appendChild(botMsg);
                messages.scrollTop = messages.scrollHeight;
            }, 500);
        });
    }
})();
