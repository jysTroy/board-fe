window.addEventListener("DOMContentLoaded", function() {
    const mainMenus = document.querySelectorAll(".main-menu .menu");
    const mainLiEls = document.querySelectorAll(".main-menu .menus > li");
    mainMenus.forEach(el => {
        el.addEventListener("click", function() {
            mainLiEls.forEach(li => li.classList.remove("on"));

            this.parentElement.classList.add("on");
        });
    });

    const subMenus = document.querySelectorAll(".main-menu .sub-menu");
    subMenus.forEach(el => {
        el.addEventListener("click", function() {
            const liEls = this.parentElement.parentElement.children;
            for(const el of liEls) {
                el.classList.remove("on");
            }

            this.parentElement.classList.add("on");
        });
    });

    floatSideMenu();
});

/**
* 사이드 메뉴 플로팅 처리
*
*/
function floatSideMenu() {
    const el = document.querySelector("aside.main-menu");

    window.addEventListener("scroll", function() {
        const breakPoint = 100;
        let span = 0;
        if (pageYOffset > breakPoint) {
            span = pageYOffset - breakPoint + 10;
        } else {
            span = 0;
        }

        el.style.marginTop = span + "px";
    });

}