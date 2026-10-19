/**
 * cart.js - Điều khiển chọn/bỏ chọn sản phẩm và đồng bộ form giỏ hàng
 */
document.addEventListener("DOMContentLoaded", function () {

    const checkboxes =
        document.querySelectorAll(".product-checkbox");

    const updateTotalForm =
        document.getElementById("updateTotalForm");

    const checkoutForm =
        document.getElementById("proceedCheckoutForm");

    const selectAllBtn = document.getElementById("selectAllBtn");

    // Cập nhật trạng thái nút theo checkbox
    function updateSelectAllButton() {
        if (!selectAllBtn) return;

        const checkedCount = Array.from(checkboxes)
            .filter(function (checkbox) {
                return checkbox.checked;
            }).length;

        const allSelected =
            checkboxes.length > 0 &&
            checkedCount === checkboxes.length;

        selectAllBtn.textContent = allSelected
            ? "Bỏ chọn tất cả"
            : "Chọn tất cả";

        selectAllBtn.classList.toggle("selected", allSelected);

        selectAllBtn.setAttribute(
            "aria-pressed",
            String(allSelected)
        );
    }

    // Nhấn nút để chọn hoặc bỏ chọn tất cả
    if (selectAllBtn) {
        selectAllBtn.addEventListener("click", function () {
            const allSelected = Array.from(checkboxes)
                .every(function (checkbox) {
                    return checkbox.checked;
                });

            checkboxes.forEach(function (checkbox) {
                checkbox.checked = !allSelected;
            });

            updateSelectAllButton();
        });
    }

    // Cập nhật nút khi người dùng tự chọn từng sản phẩm
    checkboxes.forEach(function (checkbox) {
        checkbox.addEventListener("change", updateSelectAllButton);
    });

    // Khởi tạo đúng trạng thái khi trang tải xong
    updateSelectAllButton();

    /*
     * Copy checked product IDs into the target form.
     * This is necessary because the checkboxes are outside both forms.
     */
    function syncSelectedIds(form) {
        if (!form) return 0;

        // Remove IDs added by a previous submission.
        form.querySelectorAll("[data-selected-id]")
            .forEach(function (input) {
                input.remove();
            });

        let selectedCount = 0;

        checkboxes.forEach(function (checkbox) {

            if (checkbox.checked) {

                const hiddenInput =
                    document.createElement("input");

                hiddenInput.type = "hidden";
                hiddenInput.name = "selectedVariantIds";
                hiddenInput.value = checkbox.value;
                hiddenInput.setAttribute("data-selected-id", "true");

                form.appendChild(hiddenInput);
                selectedCount++;
            }
        });

        return selectedCount;
    }

    // Submit selected product IDs when clicking Update Total.
    if (updateTotalForm) {
        updateTotalForm.addEventListener("submit", function (event) {

            const selectedCount = syncSelectedIds(updateTotalForm);

            if (selectedCount === 0) {
                event.preventDefault();
                alert("Please select at least one product.");
            }
        });
    }

    // Submit selected product IDs when proceeding to Checkout.
    if (checkoutForm) {
        checkoutForm.addEventListener("submit", function (event) {

            const selectedCount = syncSelectedIds(checkoutForm);

            if (selectedCount === 0) {
                event.preventDefault();
                alert("Please select at least one product.");
            }
        });
    }

    /*
     * When updating quantity, preserve the current checkbox choices
     * in the form submitted to CartServlet.
     */
    document.querySelectorAll(".quantity-form")
        .forEach(function (form) {

            form.addEventListener("submit", function () {

                form.querySelectorAll("[data-selected-id]")
                    .forEach(function (input) {
                        input.remove();
                    });

                checkboxes.forEach(function (checkbox) {

                    if (checkbox.checked) {

                        const hiddenInput =
                            document.createElement("input");

                        hiddenInput.type = "hidden";
                        hiddenInput.name = "selectedVariantIds";
                        hiddenInput.value = checkbox.value;
                        hiddenInput.setAttribute(
                            "data-selected-id", "true"
                        );

                        form.appendChild(hiddenInput);
                    }
                });
            });
        });

});
