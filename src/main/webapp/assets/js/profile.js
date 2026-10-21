/**
 * profile.js - Chuyển đổi giữa 2 tab: "Thông tin cá nhân" và "Đổi mật khẩu"
 */
function switchTab(tabName) {
    const isPass = (tabName === 'password');

    const infoSection = document.getElementById('personalInfoSection');
    const passSection = document.getElementById('changePasswordSection');
    const infoBtn = document.getElementById('tab-info-btn');
    const passBtn = document.getElementById('tab-password-btn');

    // 1. Ẩn/Hiện nội dung của từng phần
    if (infoSection) infoSection.style.display = isPass ? 'none' : 'block';
    if (passSection) passSection.style.display = isPass ? 'block' : 'none';

    // 2. Đánh dấu tab nào đang được chọn (active)
    if (infoBtn) infoBtn.classList.toggle('active', !isPass);
    if (passBtn) passBtn.classList.toggle('active', isPass);

    // 3. Cập nhật URL (#password) để khi F5 tải lại trang vẫn giữ nguyên tab đó
    if (isPass) {
        window.location.hash = 'password';
    } else if (window.location.hash) {
        history.replaceState(null, null, window.location.pathname + window.location.search);
    }
}

// Tự động mở tab Đổi mật khẩu nếu người dùng truy cập link có đuôi #password
document.addEventListener('DOMContentLoaded', function () {
    if (window.location.hash === '#password') {
        switchTab('password');
    }
});
