/**
 * Created by Can on 21/02/2017 Edited by Allan
 */


function startQuiz(){

    //display Questions and submit button
    document.getElementById("questionContainer").style.display = "block";
    document.getElementById("counterContainer").style.display = "block";
    document.getElementById("submitButton").style.display = "block";
}

function endQuiz(){

    //display Questions and submit button
    document.getElementById("questionContainer").style.display = "none";
    document.getElementById("submitButton").style.display = "none";
    //no reason to hide counter, so we're keeping it on-screen.

    submitResults();
}

function submitResults() {
    //TODO: Implement
}