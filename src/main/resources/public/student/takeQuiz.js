/**
 * Created by Can on 21/02/2017 Edited by Allan
 */





var elapsedTime = 0;
var intervalID; //for timer, needed for stopping


function startQuiz(){

    //display Questions and submit button
    document.getElementById("questionContainer").style.display = "block";
    document.getElementById("counterContainer").style.display = "block";
    document.getElementById("submitButton").style.display = "block";
    document.getElementById("startButton").style.display = "none";

    startCounter();

}

function endQuiz(){

    //display Questions and submit button
    document.getElementById("questionContainer").style.display = "none";
    document.getElementById("submitButton").style.display = "none";
    //no reason to hide counter, so we're keeping it on-screen.

    stopCounter();
    submitResults();
}

function startCounter() {
    intervalID = setInterval(addSecond, 1000);
}

function stopCounter() {
    clearInterval(intervalID);
}

function addSecond(){
    elapsedTime++;
    updateTime();
}

function updateTime(){
    var time = elapsedTime.toString();
    document.getElementById("counter").innerHTML = time;
    document.getElementById("FormCounter").value = time;
}

function submitResults() {
    //TODO: Implement
}