window.addEventListener("DOMContentLoaded", function() {
    frmChat.addEventListener("submit", function(e) {
        e.preventDefault();
        const formData = new FormData(frmChat);

        const url = `/chat/api?model=${formData.get('model')}&roomId=${formData.get('roomId')}&message=${formData.get('message')}`;

        ajaxLoad(url, (data) => {
            console.log(data)
        });
    });
});