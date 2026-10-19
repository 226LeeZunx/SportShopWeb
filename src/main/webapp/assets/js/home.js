/**
 * home.js - Script điều khiển trượt Hero Banner trang chủ
 */
var currentHeroIndex = 0;

function showHeroSlide(index) {
    var slides = document.querySelectorAll('.album-hero-slide');
    if (!slides || slides.length <= 1) return;
    for (var i = 0; i < slides.length; i++) {
        slides[i].classList.remove('active');
    }
    currentHeroIndex = (index + slides.length) % slides.length;
    slides[currentHeroIndex].classList.add('active');
}

function prevHeroSlide() {
    showHeroSlide(currentHeroIndex - 1);
}

function nextHeroSlide() {
    showHeroSlide(currentHeroIndex + 1);
}

// Tự động chuyển slide nhẹ nhàng sau mỗi 6 giây nếu có nhiều hơn 1 slide
setInterval(function() {
    var slides = document.querySelectorAll('.album-hero-slide');
    if (slides && slides.length > 1) {
        nextHeroSlide();
    }
}, 6000);
