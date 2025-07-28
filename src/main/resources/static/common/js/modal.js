var commonLib = commonLib ?? {}

// 모달
commonLib.modal = {
    // 최초 로딩될 부분 정의, 쓰고 있는 페이지에 동적으로 적용
    init() {
        // 모달 배경 (창이 뜰때 배경을 어둡게)
        const modalBg = document.getElementById("modal-bg");
        if (modalBg) return;

        // 동적 추가
        const div = document.createElement("div");
        div.id = 'modal-bg';

        // 일단 모달창 안보이게 해두기 (나중에 지워야함)
        div.className = 'dn';

        document.body.append(div);

        // 클릭하면 안보이게
        div.addEventListener("click", () => this.close());

    },
    /**
    * close 정의
    * 모달 배경 레이어 - #modal-bg
    * 모달 컨텐츠 레이어 - .modal-content
    */
    close() {
        const layers = document.querySelectorAll("#modal-bg, .modal-content");
        layers.forEach(el => {
            el.classList.remove("dn");
            el.classList.add("dn");
        })
    }
};

window.addEventListener("DOMContentLoaded", function() {
    const { modal } = commonLib;
    modal.init();
});