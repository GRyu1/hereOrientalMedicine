const nav = document.querySelector(".nav");
const hamburger = document.querySelector(".more-btn");

const handleHamburgerClicked = () => {
  if (nav.style.display == "none") {
    nav.style.display = "block";
  } else {
    nav.style.display = "none";
  }
};

hamburger.addEventListener("click", handleHamburgerClicked);

/* 대문 이미지 슬라이드 */
document.addEventListener("DOMContentLoaded", function () {
  const slide = new Swiper("#my-swiper", {
    slidesPerView: "auto", // 한 슬라이드에 보여줄 갯수
    // spaceBetween: 6, // 슬라이드 사이 여백
    loop: false, // 슬라이드 반복 여부
    loopAdditionalSlides: 1, // 슬라이드 반복 시 마지막 슬라이드에서 다음 슬라이드가 보여지지 않는 현상 수정
    pagination: false, // pager 여부
    autoplay: {
      // 자동 슬라이드 설정 , 비 활성화 시 false
      delay: 3000, // 시간 설정
      disableOnInteraction: false, // false로 설정하면 스와이프 후 자동 재생이 비활성화 되지 않음
    },
    pagination: {
      el: ".swiper-pagination",
      clickable: true,
    },
  });
});

/* 둘러보기 이미지 슬라이드 */
document.addEventListener("DOMContentLoaded", function () {
  const slide = new Swiper(".swiper-slide2 #my-swiper", {
    slidesPerView: "auto", // 한 슬라이드에 보여줄 갯수
    // spaceBetween: 6, // 슬라이드 사이 여백
    loop: false, // 슬라이드 반복 여부
    loopAdditionalSlides: 1, // 슬라이드 반복 시 마지막 슬라이드에서 다음 슬라이드가 보여지지 않는 현상 수정
    pagination: false, // pager 여부
    autoplay: {
      // 자동 슬라이드 설정 , 비 활성화 시 false
      delay: 3000, // 시간 설정
      disableOnInteraction: false, // false로 설정하면 스와이프 후 자동 재생이 비활성화 되지 않음
    },
    pagination: {
      el: ".swiper-slide2 .swiper-pagination",
      clickable: true,
    },
    navigation: {
      nextEl: ".swiper-slide2 .swiper-button-next",
      prevEl: ".swiper-slide2 .swiper-button-prev",
    },
  });
});

/* 메뉴 스크롤 */
var $menu = $(".nav ul li"),
  $contents = $(".section"),
  $doc = $("html, body");
var isScrolling = false; // 스크롤 이벤트 중인지를 나타내는 변수 추가

$(function () {
  $menu.on("click", "a", function (e) {
    e.preventDefault(); // 기본 링크 이동 동작 방지
    if (isScrolling) return; // 스크롤 이벤트 중인 경우 클릭 이벤트 무시

    isScrolling = true; // 스크롤 이벤트 중으로 플래그 설정
    var $target = $(this).parent(),
      idx = $target.index(),
      section = $contents.eq(idx),
      offsetTop = section.offset().top;

    var correctedOffsetTop = offsetTop - -30;
    $doc.stop().animate(
      {
        scrollTop: correctedOffsetTop,
      },
      500,
      function () {
        isScrolling = false; // 스크롤 애니메이션이 완료된 후 플래그 해제
      }
    );
    $menu.removeClass("on");
    $menu.eq(idx).addClass("on");
  });
});

/* 메뉴 색 변경 */
$(window).scroll(function () {
  if (isScrolling) return; // 스크롤 이벤트 중인 경우 실행하지 않음

  var scltop = $(window).scrollTop();

  $.each($contents, function (idx, item) {
    var $target = $contents.eq(idx),
      i = $target.index(),
      targetTop = $target.offset().top;

    if (targetTop <= scltop) {
      $menu.removeClass("on");
      $menu.eq(idx).addClass("on");
    }
  });
});

/* to top 버튼 */
var btnTop = $(".to_top_btn"); // $('.to_top_btn') 선택자를 사용하여 "To Top" 버튼을 나타내는 요소를 선택하고 btnTop 변수에 할당
btnTop.on("click", function () {
  $doc
    .stop() // $doc 변수에 저장된 문서 요소('html, body')를 애니메이션으로 스크롤
    .animate(
      {
        scrollTop: 0,
      },
      500
    ); // scrollTop 속성을 0으로 설정하여 페이지의 맨 위로 이동하고, 500밀리초 동안 애니메이션을 진행
});

/* 페이드인 효과 */

$(document).ready(function () {
  $(window).scroll(function () {
    var scroll = $(window).scrollTop();
    var windowHeight = $(window).height();

    $(".section").each(function () {
      var position = $(this).offset().top;

      // 화면의 하단부터 섹션의 상단까지의 거리가 2/3 이내에 위치하면 페이드인 클래스를 추가
      if (position < scroll + (windowHeight * 2) / 3) {
        $(this).addClass("fade-in");
      } else {
        $(this).removeClass("fade-in");
      }
    });
  });
});

/* 사이드바 펼쳐지는 효과 */
