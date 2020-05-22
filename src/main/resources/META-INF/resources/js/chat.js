const liveChatList = $('#liveChatList');

function loadChat() {
    fetch("/chat")
        .then(response => response.json())
        .then(chatMessages => {
            liveChatList.empty();
            for (const chatMessage of chatMessages) {
                liveChatList.append(createChatElement(chatMessage));
            }
        })
}

function createChatElement(chatMessage) {
    return $("<li class=\"list-group-item chatMessage\">" +
        "    <img src=\"" + chatMessage.profileUrl + "\">" +
        "    <div class=\"actions\">" +
        "         <a href='#' onclick='select(\"" + chatMessage.uuid + "\")' title='Pin message as question'><i class=\"fas fa-thumbtack fa-lg\"></i></a>" +
        "    </div>" +
        "    <div class=\"username\">" + chatMessage.userName + "</div>" +
        "    <div class=\"text\">" + chatMessage.text + "</div>" +
        "</li>")
}

function select(chatMessageId) {
    fetch("/chat/" + chatMessageId, {
        method: "POST"
    }).then(response => {
        if (response.status === 200) {
            loadQuestions();
        }
    });
}

loadChat();
