const promotedQuestionDiv = $('#promotedQuestion');

function promote(questionId) {
    fetch("/promotion/" + questionId, {
        method: "POST"
    }).then(_ => loadPromotion());
}

function demote() {
    fetch("/promotion", {
        method: "DELETE"
    }).then(_ => loadPromotion());
}


function loadPromotion() {
    fetch("/promotion").then(response => {
        promotedQuestionDiv.empty();
        if (response.status === 200) {
            response.json().then(question => {
                const element = createPromotionElement(question);
                promotedQuestionDiv.append(element);
            });
        } else {
            promotedQuestionDiv.text("No question promoted");
        }
    });
}

function createPromotionElement(question) {
    return $("<li class=\"list-group-item question\">" +
        "    <img src=\"" + question.profileUrl + "\">" +
        "    <div class=\"questionActions\">" +
        "         <a  href='#' onclick='demote(\"" + question.uuid + "\")' title='Demote from live stream'><i class=\"fas fa-bookmark fa-lg\"></i></a>" +
        "    </div>" +
        "    <div class=\"questionUsername\">" + question.userName + "</div>" +
        "    <div class=\"questionText\">" + question.text + "</div>" +
        "</li>")
}

loadPromotion();