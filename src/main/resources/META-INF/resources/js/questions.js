const questionListDiv = $('#questionList');


function loadQuestions() {
    fetch("questions")
        .then(response => response.json())
        .then(questions => {
            for (const question of questions) {
                questionListDiv.append(createQuestionElement(question));
            }
        })
}

function createQuestionElement(question) {
    return $("<li class=\"list-group-item question\">" +
        "    <img src=\"" + question.profileUrl + "\">" +
        "    <div class=\"questionActions\">" +
        "         <i class=\"far fa-bookmark fa-lg\"></i>" +
        "         <i class=\"fas fa-trash-alt fa-lg\"></i>" +
        "    </div>" +
        "    <div class=\"questionUsername\">" + question.userName + "</div>" +
        "    <div class=\"questionText\">" + question.text + "</div>" +
        "</li>")
}

loadQuestions();