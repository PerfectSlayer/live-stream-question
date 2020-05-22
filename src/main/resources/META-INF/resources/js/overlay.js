const questionDiv = $('#promotedQuestion');
let currentQuestion;

function loadQuestion() {
    fetch("/promotion")
        .then(response => {
            if (response.status === 200) {
                response.json().then(processQuestion);
            } else if (response.status === 404) {
                clearQuestion();
            }
        })
}

function processQuestion(question) {
    if (currentQuestion) {
        if (currentQuestion.uuid !== question.uuid) {
            clearQuestion(() => processQuestion(question));
        }
        return;
    }
    currentQuestion = question;
    questionDiv.empty();
    questionDiv.append(createQuestionElement(question));
    questionDiv.fadeIn('slow');
}

function createQuestionElement(question) {
    return $('<div class="img-column">' +
        '        <img src="' + question.profileUrl + '" alt="user avatar">' +
        '    </div>' +
        '    <div class="text-column">' +
        '        <div class="username">' + question.userName + '</div>' +
        '        <div class="text">' + question.text + '</div>' +
        '    </div>')
}

function clearQuestion(callback) {
    if (!currentQuestion) {
        return;
    }
    currentQuestion = null;
    questionDiv.fadeOut('fast', callback);
}

window.setInterval(loadQuestion, 2000);
