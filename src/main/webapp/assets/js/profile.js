/**
 * profile.js - Điều khiển chuyển tab thông tin cá nhân / đổi mật khẩu
 */
function switchTab(tabName) {
    var infoSection = document.getElementById('personalInfoSection');
    var passSection = document.getElementById('changePasswordSection');
    var infoBtn = document.getElementById('tab-info-btn');
    var passBtn = document.getElementById('tab-password-btn');

    if (tabName === 'password') {
        if (infoSection) infoSection.style.display = 'none';
        if (passSection) passSection.style.display = 'block';
        if (infoBtn) infoBtn.classList.remove('active');
        if (passBtn) passBtn.classList.add('active');
        window.location.hash = 'password';
    } else {
        if (infoSection) infoSection.style.display = 'block';
        if (passSection) passSection.style.display = 'none';
        if (infoBtn) infoBtn.classList.add('active');
        if (passBtn) passBtn.classList.remove('active');
        if (window.location.hash) {
            history.replaceState(null, null, window.location.pathname + window.location.search);
        }
    }
}

// Kích hoạt tab nếu URL có hash #password
window.addEventListener('DOMContentLoaded', function () {
    if (window.location.hash === '#password') {
        switchTab('password');
    }
});
