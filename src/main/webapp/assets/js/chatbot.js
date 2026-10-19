/**
 * chatbot.js - Điều khiển giao diện AI Chatbot SportShop & Lưu trữ phiên bằng sessionStorage
 */
(function () {
    function initChatbot() {
        var toggler = document.getElementById('chatToggler');
        var box = document.getElementById('chatBox');
        var closeBtn = document.getElementById('chatCloseBtn');
        var clearBtn = document.getElementById('chatClearBtn');
        var input = document.getElementById('chatInput');
        var form = document.getElementById('chatForm');
        var messages = document.getElementById('chatMessages');

        if (!toggler || !box) return;

        var DEFAULT_GREETING = 'Xin chào! Tôi là trợ lý ảo SportShop. Bạn cần hỗ trợ gì về sản phẩm hoặc đơn hàng ạ?';
        var STORAGE_KEY_HISTORY = 'sportshop_chat_history';
        var STORAGE_KEY_OPEN = 'sportshop_chat_open';

        // Lấy endpoint từ data-action của form hoặc mặc định
        var chatEndpoint = (form && form.getAttribute('data-action')) ? form.getAttribute('data-action') : '/rag-chat';

        // Hàm thêm tin nhắn vào giao diện và tùy chọn lưu vào sessionStorage
        function appendMessage(sender, text, saveToStorage) {
            var msgDiv = document.createElement('div');
            msgDiv.className = 'chat-msg ' + sender;
            if (sender === 'bot') {
                msgDiv.style.whiteSpace = 'pre-line';
            }
            msgDiv.innerText = text;
            messages.appendChild(msgDiv);
            messages.scrollTop = messages.scrollHeight;

            if (saveToStorage) {
                var history = getStoredHistory();
                history.push({ sender: sender, text: text });
                try {
                    sessionStorage.setItem(STORAGE_KEY_HISTORY, JSON.stringify(history));
                } catch (e) {
                    console.warn('Không thể lưu tin nhắn vào sessionStorage:', e);
                }
            }
            return msgDiv;
        }

        // Lấy danh sách lịch sử tin nhắn từ sessionStorage
        function getStoredHistory() {
            try {
                var raw = sessionStorage.getItem(STORAGE_KEY_HISTORY);
                return raw ? JSON.parse(raw) : [];
            } catch (e) {
                return [];
            }
        }

        // Khôi phục toàn bộ tin nhắn khi tải trang
        function renderChatHistory() {
            messages.innerHTML = '';
            var history = getStoredHistory();

            if (!history || history.length === 0) {
                appendMessage('bot', DEFAULT_GREETING, false);
            } else {
                history.forEach(function (item) {
                    appendMessage(item.sender, item.text, false);
                });
            }
            messages.scrollTop = messages.scrollHeight;
        }

        // 1. Khôi phục trạng thái khi chuyển trang / F5
        renderChatHistory();

        if (sessionStorage.getItem(STORAGE_KEY_OPEN) === 'true') {
            box.classList.add('show');
        }

        // 2. Bật / Tắt khung chat cố định
        toggler.addEventListener('click', function (e) {
            e.stopPropagation();
            var isOpen = box.classList.toggle('show');
            sessionStorage.setItem(STORAGE_KEY_OPEN, isOpen ? 'true' : 'false');
            if (isOpen && input) {
                setTimeout(function () {
                    input.focus();
                }, 150);
            }
        });

        // 3. Nút đóng khung chat
        if (closeBtn) {
            closeBtn.addEventListener('click', function (e) {
                e.stopPropagation();
                box.classList.remove('show');
                sessionStorage.setItem(STORAGE_KEY_OPEN, 'false');
            });
        }

        // 4. Nút làm mới (xóa lịch sử chat)
        if (clearBtn) {
            clearBtn.addEventListener('click', function (e) {
                e.stopPropagation();
                if (confirm('Bạn có muốn làm mới cuộc trò chuyện với trợ lý AI?')) {
                    sessionStorage.removeItem(STORAGE_KEY_HISTORY);
                    renderChatHistory();
                }
            });
        }

        // 5. Gửi tin nhắn bằng Fetch API kết nối tới Servlet RAG
        function sendMessage() {
            var text = input.value.trim();
            if (!text) return;

            // Hiển thị và lưu tin nhắn của người dùng
            appendMessage('user', text, true);
            input.value = '';

            // Hiển thị trạng thái đang xử lý
            var loadingMsg = document.createElement('div');
            loadingMsg.className = 'chat-msg bot loading';
            loadingMsg.innerText = 'Đang tìm kiếm thông tin sản phẩm... ⏳';
            messages.appendChild(loadingMsg);
            messages.scrollTop = messages.scrollHeight;

            // Gửi AJAX tới servlet
            fetch(chatEndpoint, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
                },
                body: 'query=' + encodeURIComponent(text)
            })
            .then(function (response) {
                return response.text();
            })
            .then(function (answerText) {
                if (loadingMsg.parentNode) {
                    messages.removeChild(loadingMsg);
                }
                var reply = answerText ? answerText.trim() : 'Xin lỗi, hệ thống không thể phản hồi lúc này.';
                appendMessage('bot', reply, true);
            })
            .catch(function (error) {
                if (loadingMsg.parentNode) {
                    messages.removeChild(loadingMsg);
                }
                appendMessage('bot', 'Lỗi kết nối tới máy chủ AI.', false);
                console.error('Error:', error);
            });
        }

        if (form) {
            form.addEventListener('submit', function (e) {
                e.preventDefault();
                sendMessage();
            });
        }
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initChatbot);
    } else {
        initChatbot();
    }
})();
