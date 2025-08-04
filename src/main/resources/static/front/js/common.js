window.addEventListener("DOMContentLoaded", function() {
    const mainLiEls = document.querySelectorAll(".main-menu .menus > li");

    mainLiEls.forEach(li => {
        const mainMenu = li.querySelector(".menu");
        if (mainMenu) {
            mainMenu.addEventListener("click", function(e) {
                mainLiEls.forEach(otherLi => {
                    if (otherLi !== li) {
                        otherLi.classList.remove("on");
                    }
                });

                li.classList.toggle("on");
            });
        }
    });

    const subMenus = document.querySelectorAll(".main-menu .sub-menu");
    subMenus.forEach(el => {
        el.addEventListener("click", function(e) {
            e.stopPropagation();
            const liEls = this.closest("ul").children;

            Array.from(liEls).forEach(li => li.classList.remove("on"));

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

window.addEventListener("DOMContentLoaded", function() {
    const toggles = document.querySelectorAll(".toggle-sub-menu");
    toggles.forEach(function(toggle) {
        toggle.addEventListener("click", function(e) {
            const subMenu = this.nextElementSibling;
            if (subMenu) {
                subMenu.style.display =
                    (subMenu.style.display === "none" || subMenu.style.display === "")
                        ? "block"
                        : "none";
            }
        });
    });
});