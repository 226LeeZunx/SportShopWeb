/**
 * home.js - Điều khiển trình chiếu Banner (Hero Slider) trên trang chủ
 */
let currentHeroIndex = 0;

// Hàm chuyển đến slide theo số thứ tự (index)
function showHeroSlide(index) {
    const slides = document.querySelectorAll('.album-hero-slide');
    if (!slides || slides.length <= 1) return;

    // Ẩn tất cả các slide
    slides.forEach(slide => slide.classList.remove('active'));

    // Tính chỉ số slide kế tiếp (dùng toán tử chia lấy dư % để quay vòng tròn)
    currentHeroIndex = (index + slides.length) % slides.length;

    // Hiển thị slide mới
    slides[currentHeroIndex].classList.add('active');
}

// Chuyển sang banner trước đó
function prevHeroSlide() {
    showHeroSlide(currentHeroIndex - 1);
}

// Chuyển sang banner tiếp theo
function nextHeroSlide() {
    showHeroSlide(currentHeroIndex + 1);
}

// Tự động chuyển banner sau mỗi 6 giây
setInterval(function () {
    const slides = document.querySelectorAll('.album-hero-slide');
    if (slides && slides.length > 1) {
        nextHeroSlide();
    }
}, 6000);
