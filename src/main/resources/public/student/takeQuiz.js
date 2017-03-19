/**
 * Created by Can on 21/02/2017 Edited by Allan
 */

var elapsedTimeInSec = 0;
var timeLeftInSec;
var intervalID; //for timer, needed for stopping

$(function(){
    
    var  $window = $(window);
    var  $stickyEl = $('#counterContainer');
    var  elTop = $stickyEl.offset().top;

    $window.scroll(function() {
        $stickyEl.toggleClass('sticky', $window.scrollTop() > elTop);
    }); 
    
   
    
    var timeLimit = $("#counterContainer").data("tlimit");
    
    if (timeLimit===""){//timeLimit was null
        timeLeftInSec= null;
    }
    else{
        timeLeftInSec = timeLimit*60;
    }
    
    
    $('#theForm').submit(function() {
        
        $(".error-highlight").removeClass("error-highlight");
        
        //check if each question has at least 1 answer
        var valid = true;
        
        $('input[name="subscribe"]').is(':checked') 
        
        var questionId, isAnyAnswChecked ;
        $(".panel-question").each(function(ind){
            
            questionId = $(this).data("qid");
            
            isAnyAnswChecked = $('.panel-question input[name="question'+questionId+'"]').is(':checked'); 
            
            console.log(questionId+ " "+ isAnyAnswChecked);
            
            if (!isAnyAnswChecked){
                valid = false;
                $(this).find(".optionsRow").eq(0).addClass("error-highlight");
            }
            
        });
        
        
        if (valid){
            //display Questions and submit button
            document.getElementById("questionContainer").style.display = "none";
            document.getElementById("submitButton").style.display = "none";
            //no reason to hide counter, so we're keeping it on-screen.
            document.getElementById("FormCounter").value = elapsedTimeInSec;

            clearInterval(intervalID);
        }
        
        return valid;//if false form is not submitted
        
    });
    
    
});



function startQuiz(){

    //display Questions and submit button
    document.getElementById("questionContainer").style.display = "block";
    document.getElementById("counterContainer").style.display = "block";
    document.getElementById("submitButton").style.display = "block";
    document.getElementById("startButton").style.display = "none";

    intervalID = setInterval(updateTime, 1000);

}







function updateTime(){
    
    elapsedTimeInSec++;
    
    if (timeLeftInSec!==null){
        timeLeftInSec--;
        
        if (timeLeftInSec<0){
            clearInterval(intervalID);
            alert("Time is up!");
            window.location.reload(); 
        }
        else{
            $("#leftCounter").text(convertSecondsToTime(timeLeftInSec));
        }
    }
    
}

function convertSecondsToTime(sec){
    var h = Math.floor(sec/3600);
    var m = Math.floor((sec - h*3600)/60);
    var s = sec - h*3600-m*60;
    
    if ((h+"").length<2){h="0"+h;}
    if ((m+"").length<2){m="0"+m;}
    if ((s+"").length<2){s="0"+s;}
    
    return (h+":"+m+":"+s);
}

