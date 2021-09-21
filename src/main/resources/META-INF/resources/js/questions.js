const questionListDiv = $('#questionList');

function loadQuestions() {
    fetch("/questions")
        .then(response => response.json())
        .then(questions => {
            questionListDiv.empty();
            for (const question of questions) {
                questionListDiv.append(createQuestionElement(question));
            }
        })
}

function createQuestionElement(question) {
    return $("<li class=\"list-group-item question\">" +
        "    <img src=\"" + question.profileUrl + "\" alt=\"Profile picture\">" +
        "    <div class=\"actions\">" +
        "         <a href='#' onclick='promote(\"" + question.uuid + "\")' title='Promote to tile stream'><i class=\"far fa-bookmark fa-lg\"></i></a>" +
        "         <a href='#' onclick='toggleAnswered(\"" + question.uuid + "\")' title='Toggle answered'><i class=\"" + (question.answered ? "fas" : "far") + " fa-comments fa-lg\"></i></a>" +
        "         <a href='#' onclick='removeQuestion(\"" + question.uuid + "\")' title='Remove from selected'><i class=\"fas fa-trash-alt fa-lg\"></i></a>" +
        "    </div>" +
        "    <div class=\"username\">" + question.userName + "</div>" +
        "    <div class=\"text\">" + question.text + "</div>" +
        "</li>")
}

function addQuestion() {
    fetch("/questions", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            "userName": $('#questionUsername').val(),
            "text": $('#questionText').val()
        }),
    }).then(_ => loadQuestions());
}

function removeQuestion(questionId) {
    fetch("/questions/" + questionId, {
        method: "DELETE"
    }).then(_ => loadQuestions());
}

const addQuestionForm = $('#addQuestionForm');
addQuestionForm.submit(function (event) {
    addQuestion();
    event.preventDefault();
});

function toggleAnswered(questionId) {
    fetch("/questions/" + questionId + "/answered", {
        method: "PATCH",
    }).then(_ => loadQuestions());
}

loadQuestions();
