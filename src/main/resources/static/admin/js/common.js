window.addEventListener("DOMContentLoaded", function() {

    // 전체 토글 공통 기능 처리
    const chkAlls = document.getElementsByClassName("check-all");
    for (const el of chkAlls) {
        el.addEventListener("click", function() {
            const { targetName } = this.dataset;
            const chks = document.getElementsByName(targetName);
            for (const chk of chks) {
                chk.checked = this.checked;
            }
        });
    }

    // 공통 양식 처리
    const processFormButtons = document.getElementsByClassName("process-form");
    for (const el of processFormButtons) {
        el.addEventListener("click", function () {
            const method = this.classList.contains("delete") ? "DELETE" : "PATCH";
            const { formName } = this.dataset;
            const formEl = document.forms[formName];
            if (!formEl) return;

            formEl._method.value = method;

            Swal.fire({
                title: "정말 처리하겠습니까?",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#d33",
                cancelButtonColor: "#3085d6",
                confirmButtonText: "처리하기",
                cancelButtonText: "취소"
            }).then((result) => {
                if (result.isConfirmed) {
                    formEl.submit();
                }
            });
        });
    }
});