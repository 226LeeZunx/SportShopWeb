/**
 * verify-otp.js - Xử lý đếm ngược thời gian và ràng buộc chỉ nhập số cho mã OTP
 */
document.addEventListener("DOMContentLoaded", function () {
    const timerElem = document.getElementById("countdownTimer");
    const otpInput = document.getElementById("otp");

    // 1. Chỉ cho phép nhập ký tự số (0-9) trong ô OTP
    if (otpInput) {
        otpInput.addEventListener("input", function () {
            this.value = this.value.replace(/[^0-9]/g, "");
        });
    }

    // 2. Đồng hồ đếm ngược thời gian hiệu lực của OTP
    if (timerElem) {
        let remaining = parseInt(timerElem.getAttribute("data-remaining") || "300", 10);

        function updateDisplay() {
            if (remaining <= 0) {
                timerElem.textContent = "Expired";
                timerElem.style.color = "#dc2626";
                return;
            }

            const minutes = Math.floor(remaining / 60);
            const seconds = remaining % 60;
            timerElem.textContent =
                (minutes < 10 ? "0" + minutes : minutes) + ":" +
                (seconds < 10 ? "0" + seconds : seconds);
            remaining--;
        }

        updateDisplay();
        const interval = setInterval(function () {
            if (remaining < 0) {
                clearInterval(interval);
            } else {
                updateDisplay();
            }
        }, 1000);
    }
});
