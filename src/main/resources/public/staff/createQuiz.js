/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


//http://stackoverflow.com/questions/7477/autosizing-textarea-using-prototype



$(function(){
    
  
    addQuestion();
    
    
    $("#time-column input:checkbox").change(function() {
        if(this.checked) {
            $("#time-column label").addClass("mandatory");
            $("#quiz-timeLimit").prop('disabled', false);
        }
        else{
            $("#time-column label").removeClass("mandatory");
            $("#quiz-timeLimit").prop('disabled', true);
        }
    });
    
    
    
    
});

function addOption(target){
     
     var allOptionsColumn = $(target).closest(".row").find(".allOptions").first();
     
     var n = allOptionsColumn.find(".row").size();
     
     allOptionsColumn.append('<div class="row option-row">'+
                            '<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">'+
                                '<div class="input-group" >'+
                                '<span class="mandatory-option"></span>'+
                                    '<input type="text" class="form-control" placeholder="option '+(n+1)+'">'+
                                    '<span class="input-group-addon shrinkable"> '+
                                        '<input type="checkbox" >'+
                                    '</span>'+
                                    '<span class="input-group-addon shrinkable" >Correct?</span>'+
                                    '<span class="input-group-btn">'+
                                        '<button onclick="removeOption(this)" title="Remove option" class="shrinkable btn btn-default btn-custom" type="button"> <span class="glyphicon glyphicon-remove" ></span></button>'+
                                    '</span>'+
                                '</div>'+
                            '</div>'+
                        '</div>');
}


function removeOption(target){
    
    var rowToRemove = $(target).closest(".row");
    var removedRowIndex = rowToRemove.parent().find(".row").index( rowToRemove );
    
    rowToRemove.nextAll().each(function( index ) {
       $(this).find("input").first().attr("placeholder","option "+(removedRowIndex+index+1));
    });
    
    rowToRemove.remove();
}











//new question html is copied from a hidden "template"
function addQuestion(){
    
    
    var questionsSoFar = $(".panel-question").size()-1;
    
    if (questionsSoFar===0){
        $("#noQuestionMessage").hide();
    }
    
    var clonedQuestion = $("#question0").clone();
    clonedQuestion.find(".question-lable").first().text("Question "+(questionsSoFar+1));
    clonedQuestion.removeAttr('hidden');
    clonedQuestion.removeAttr('id');
   
    var html = clonedQuestion.get(0).outerHTML;
    
   $(".panel-question").last().after(html);
   
   
    
}


function removeQuestion(target){
    
        
    var questionToRemove = $(target).closest(".panel-question");
    var removedRowIndex = $(".panel-question").index( questionToRemove );
    
    questionToRemove.nextAll().each(function( index ) {
       $(this).find(".question-lable").first().text("Question "+(removedRowIndex+index));
    });
    
    questionToRemove.remove();
    
    if ( $(".panel-question").size() ===1){//only hidden template
        $("#noQuestionMessage").show();
        $("#noQuestionMessage").removeClass("error-highlight");
    }
    
}

function submit(){
    
    
    $(".error-highlight").removeClass("error-highlight");
    
    var allData = {
        title:"",
        moduleCode:"",
        timeLimit:"",
        questions:[]
    };
    var valid = true;
    
    
    
    //read title
    allData.title = $("#quiz-title").val().trim();
    if (allData.title.length===0){
        valid = false;
        $("#title-column").addClass("error-highlight");
    }
    
    //read moduleCode
    allData.moduleCode = $("#quiz-module").val().trim();
    if (allData.moduleCode==="-1"){
        valid = false;
        $("#module-column").addClass("error-highlight");
    }
    
    //read time limit 
    
    if (!$("#time-column input:checkbox").is(':checked')){
        allData.timeLimit = "";
    }
    else{
        var limit = $("#quiz-timeLimit").val().trim();
        
      
        if ((limit).match(/^[1-9]\d*(\d+)?$/)){//this ensures: no leading 0; only 0-9 (no dot or minus)
            limit  = parseInt(limit);
            if (limit<=120){
               allData.timeLimit = limit;
            }
            else{
                $("#time-column").addClass("error-highlight");
                valid=false;
            }
        }
        else{
            $("#time-column").addClass("error-highlight");
            valid=false;
        } 
    }
    
    
    //read questions
    
    $(".panel-question").each(function( index ) {
      
      if (index>0){
          
          var nextQuestion = {
              questionText:"",
              explanation:"",
              options:[]
          };
          
          //question text
          nextQuestion.questionText = $(this).find("textarea").eq(0).val().trim();
          if (nextQuestion.questionText.length===0){
              $(this).find(".row").eq(0).addClass("error-highlight");
              valid = false;
          }
          
          //explanation
          nextQuestion.explanation = $(this).find("textarea").eq(1).val().trim();
          
          //options
          $(this).find(".allOptions .option-row").each(function(ind){//this here is an option row
              
              var optObj = {
                  option: $(this).find("input[type=text]").val().trim(),
                  correct: $(this).find("input[type=checkbox]").is(":checked")
              };
              
              if (optObj.option.length===0){
                  $(this).addClass("error-highlight");
                  valid=false;
              }
              nextQuestion.options.push(optObj);
          });
          
          allData.questions.push(nextQuestion);
      }
    });
    
    if (allData.questions.length===0){
        $("#noQuestionMessage").addClass("error-highlight");
        valid = false;
    }
    
    //console.log(JSON.stringify(allData));
    
    if (valid){
        
        
        $.ajax({
               url: "/staff/createQuiz",
               type: "POST",
               data: allData,
               contentType:  "application/json; charset=utf-8",
               success: function( data, textStatus, jqXHR) {
                 
                  alert(data);
                  //and something else :(
               },
               error: function(jqXHR, textStatus, errorMessage) {
                   
                   alert("submit failed");
                   
                   if (jqXHR.status===401){
                       //window.location = contextPath+"/Login";
                   }
                   else{
                       //and even more
                   }
                   
                  
               }
            });
        
        
        
    }
    
    
    
    
}