<script>
document.addEventListener("DOMContentLoaded", function () {
    const buttons = document.querySelectorAll(".form_action");

    buttons.forEach(function (btn) {
        btn.addEventListener("click", function () {
            const mode = btn.dataset.mode;
            const formName = btn.dataset.formName;
            const form = document.forms[formName];

            if (!form) {
                alert("폼을 찾을 수 없습니다.");
                return;
            }

            if (mode === "delete") {
                if (!confirm("정말 삭제하시겠습니까?")) return;
                form.action = "/admin/banner/list";
                form.method = "post";
                addMethodInput(form, "DELETE");
            } else if (mode === "edit") {
                form.action = "/admin/banner/list";
                form.method = "post";
                addMethodInput(form, "PATCH");
            }

            form.submit();
        });
    });

    function addMethodInput(form, method) {
        let input = form.querySelector("input[name=_method]");
        if (!input) {
            input = document.createElement("input");
            input.type = "hidden";
            input.name = "_method";
            form.appendChild(input);
        }
        input.value = method;
    }
});
</script>
