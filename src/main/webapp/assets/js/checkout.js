/**
 * checkout.js - Điều khiển chọn voucher, đồng bộ form đặt hàng và chọn địa chỉ
 */

// Chọn voucher trong danh sách
function selectVoucher(button) {
    var voucherCodeInput = document.getElementById("voucherCode");
    var voucherForm = document.getElementById("voucherForm");

    if (voucherCodeInput && button && button.dataset) {
        voucherCodeInput.value = button.dataset.code;
    }
    if (voucherForm) {
        voucherForm.requestSubmit();
    }
}

document.addEventListener("DOMContentLoaded", function () {
    var voucherForm = document.getElementById("voucherForm");

    // Đồng bộ dữ liệu giao hàng sang form voucher
    if (voucherForm) {
        voucherForm.addEventListener("submit", function () {
            var fullName = document.getElementById("fullName");
            var phone = document.getElementById("phone");
            var note = document.getElementById("note");
            var selectedAddress = document.querySelector('input[name="addressId"]:checked');
            var paymentMethod = document.querySelector('input[name="paymentMethod"]:checked');

            var vFullName = document.getElementById("voucherFullName");
            var vPhone = document.getElementById("voucherPhone");
            var vNote = document.getElementById("voucherNote");
            var vAddressId = document.getElementById("voucherAddressId");
            var vPaymentMethod = document.getElementById("voucherPaymentMethod");

            if (vFullName) vFullName.value = fullName ? fullName.value : "";
            if (vPhone) vPhone.value = phone ? phone.value : "";
            if (vNote) vNote.value = note ? note.value : "";
            if (vAddressId) vAddressId.value = selectedAddress ? selectedAddress.value : "";
            if (vPaymentMethod) vPaymentMethod.value = paymentMethod ? paymentMethod.value : "";
        });
    }

});
