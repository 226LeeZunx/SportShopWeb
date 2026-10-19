/**
 * password-toggle.js - Bật/tắt ẩn hiện mật khẩu (Eye Toggle) cho form Đăng nhập & Đăng ký
 */
document.addEventListener("DOMContentLoaded", function () {
    const toggleButtons = document.querySelectorAll(".btn-toggle-password");

    toggleButtons.forEach(function (button) {
        button.addEventListener("click", function (e) {
            e.preventDefault();

            // Tìm ô input nằm cùng trong khối .input-with-icon
            const container = button.closest(".input-with-icon");
            if (!container) return;

            const passwordInput = container.querySelector("input");
            const icon = button.querySelector("i");
            if (!passwordInput || !icon) return;

            // Đổi qua lại giữa text và password
            if (passwordInput.type === "password") {
                passwordInput.type = "text";
                icon.classList.remove("fa-eye");
                icon.classList.add("fa-eye-slash");
                button.setAttribute("title", "Hide password");
            } else {
                passwordInput.type = "password";
                icon.classList.remove("fa-eye-slash");
                icon.classList.add("fa-eye");
                button.setAttribute("title", "Show password");
            }

            // Giữ focus vào ô input sau khi click
            passwordInput.focus();
        });
    });
});
