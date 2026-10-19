/**
 * orders.js - Điều khiển bộ lọc trạng thái đơn hàng (Tabs: Tất cả, Chờ xử lý, v.v.)
 */
document.addEventListener("DOMContentLoaded", function () {
    const tabs = document.querySelectorAll(".order-tab");
    const cards = document.querySelectorAll(".order-card");
    const emptyMessage = document.getElementById("filteredEmpty");

    function filterOrders(filter) {
        let visibleCount = 0;

        cards.forEach(function (card) {
            const status = card.dataset.status;

            let visible;

            if (filter === "ALL") {
                visible = true;
            } else {
                visible = status === filter;
            }

            card.style.display = visible ? "" : "none";

            if (visible) {
                visibleCount++;
            }
        });

        tabs.forEach(function (tab) {
            const isActive = tab.dataset.filter === filter;
            tab.classList.toggle("active", isActive);
        });

        if (emptyMessage) {
            emptyMessage.style.display =
                visibleCount === 0 ? "block" : "none";
        }
    }

    tabs.forEach(function (tab) {
        tab.addEventListener("click", function () {
            filterOrders(tab.dataset.filter);
        });
    });

    if (tabs.length > 0) {
        filterOrders("ALL");
    }
});
