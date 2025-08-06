const emotionToEmoticon = {
    "기쁨": "😊",
    "분노": "😠",
    "불안": "😰",
    "당황": "😳",
    "슬픔": "😢",
    "상처": "💔"
};

function convertEmotionToEmoticon(emotion) {
    if (!emotion) return "";
    const keyword = emotion.split(" ")[0]; // 예: "기쁨 만족스러운" → "기쁨"
    const emoji = emotionToEmoticon[keyword];
    return emoji ? `${emoji}` : emotion;  // 원래 emotionText → emotion 으로 수정
}

window.addEventListener("DOMContentLoaded", function() {
    const { ajaxLoad } = commonLib;

    const tpl = document.getElementById("chat-tpl").innerHTML;
    const targetEl = document.querySelector(".chat-history");
    const domParser = new DOMParser();

    frmChat.addEventListener("submit", function(e) {
        e.preventDefault();
        const formData = new FormData(frmChat);

        const message = formData.get('message');

        let html = tpl.replace(/\[addClass\]/g, 'user')
                      .replace(/\[message\]/g, message);

        const dom = domParser.parseFromString(html, "text/html");
        targetEl.append(dom.querySelector(".message"));

        // 새 메시지 추가 후 스크롤 아래로 자동 이동
        targetEl.scrollTop = targetEl.scrollHeight;

        frmChat.message.value = ""; // 메세지 입력 초기화

        const url = `/chat/api?model=${formData.get('model')}&roomId=${formData.get('roomId')}&message=${message}`;

        ajaxLoad(url, ({ sysMessage, emotion }) => {
            let emotionHtml = "";
            if (emotion && emotion.trim() !== "") {
                const emoticon = convertEmotionToEmoticon(emotion.trim());
                emotionHtml = `${emoticon}`;
            }

            let html = tpl
                .replace(/\[addClass\]/g, 'system')
                .replace(/\[message\]/g, sysMessage)
                .replace(/\[emotionHtml\]/g, emotionHtml);

            const dom = domParser.parseFromString(html, "text/html");
            targetEl.append(dom.querySelector(".message"));

            // 메세지 추가 후 스크롤 이동
            targetEl.scrollTop = targetEl.scrollHeight;
        });
    });
});