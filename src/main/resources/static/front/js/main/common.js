// 슬라이드 처리용
document.addEventListener("DOMContentLoaded", function () {
  const swiper = new Swiper(".main-banner1", {
    loop: true,
    autoplay: {
      delay: 5000,
      disableOnInteraction: false,
    },
    pagination: {
      el: ".main-banner1 .swiper-pagination",
      clickable: true,
    },
    navigation: {
      nextEl: ".main-banner1 .swiper-button-next",
      prevEl: ".main-banner1 .swiper-button-prev",
    }
  });
});