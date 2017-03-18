/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


//http://stackoverflow.com/questions/7477/autosizing-textarea-using-prototype


var allMdes = [];

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
   
   var addedQuestionId ;//relates question to mdeEditor in the list
   if (allMdes.length===0){  
       addedQuestionId = 0; 
   }
   else{ 
       addedQuestionId =  allMdes[allMdes.length - 1].id+1;   
   }
   
   
    $(".panel-question").last().data('id',addedQuestionId );
   
    var newMde = new SimpleMDE({autofocus: true,
                                element: $(".panel-question").last().find("textarea").first()[0],
                               
                                toolbar:["bold", "italic", "|", "quote","unordered-list","ordered-list","|","link","image","|","preview",
                                   
            
                                    {name: "side-by-side",
                                         action:  function(){beforeToggleSidebyside(addedQuestionId);},
                                         className: "fa fa-columns no-disable no-mobile",
                                         title: "Toggle Side By Side"
                                    },
                                    {name: "fullscreen",
                                         action:  function(){beforeToggleFullscreen(addedQuestionId);},
                                         className: "fa fa-arrows-alt no-disable no-mobile",
                                         title: "Toggle Fullscreen"
                                    }],
                                placeholder: "Question content"  
                               
                               });
    newMde.id = addedQuestionId;
    allMdes.push(newMde);
   
   
    
}

//some elements on page have position:absolute, so, when it goes fullscreen they still remain.
//change their position to initial  before going fullscreen and then back
//+ change mutual behavior of sideBySide and fullScreen
function beforeToggleFullscreen(mdeId){

    var targetMde = $.grep(allMdes, function(e){ return e.id === mdeId; })[0];
    
    if (!targetMde.isFullscreenActive() ){
        $(".question-lable").addClass("tempInitial");
        $(".question-remove").addClass("tempInitial");
        $(".mandatory-option").addClass("tempInitial");
    }
    else{
        $(".question-lable").removeClass("tempInitial");
        $(".question-remove").removeClass("tempInitial");
        $(".mandatory-option").removeClass("tempInitial");
    }
    
    targetMde.toggleFullScreen();
   
}

function beforeToggleSidebyside(mdeId){

    var targetMde = $.grep(allMdes, function(e){ return e.id === mdeId; })[0];
    
    
    if (!targetMde.isSideBySideActive() && !targetMde.isFullscreenActive()  ){
        $(".question-lable").addClass("tempInitial");
        $(".question-remove").addClass("tempInitial");
        $(".mandatory-option").addClass("tempInitial");
        targetMde.toggleSideBySide();
        
    }
    else if (targetMde.isFullscreenActive() && !targetMde.isSideBySideActive()){
         targetMde.toggleSideBySide();
    }
    else if (targetMde.isFullscreenActive() && targetMde.isSideBySideActive()){
        $(".question-lable").removeClass("tempInitial");
        $(".question-remove").removeClass("tempInitial");
        $(".mandatory-option").removeClass("tempInitial");
        targetMde.toggleFullScreen();
    }
    
    
}


function removeQuestion(target){
    
        
    var questionToRemove = $(target).closest(".panel-question");
    var removedRowIndex = $(".panel-question").index( questionToRemove );
    
    var idToRemove = questionToRemove.data('id');
    for(var i = 0; i < allMdes.length; i++) {
        if(allMdes[i].id === idToRemove) {
            allMdes.splice(i, 1);
            break;
        }
    }
    
    
    
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
        module_id:"",
        time_limit:null,
        questions:[]
    };
    var valid = true;
    
    
    
    //read title
    allData.title = $("#quiz-title").val().trim();
    if (allData.title.length===0){
        valid = false;
        $("#title-column").addClass("error-highlight");
    }
    
    //read module_id
    allData.module_id = $("#quiz-module").val().trim();
    if (allData.module_id==="-1"){
        valid = false;
        $("#module-column").addClass("error-highlight");
    }
    
    //read time limit 
    
    if ($("#time-column input:checkbox").is(':checked')){
        var limit = $("#quiz-timeLimit").val().trim();
        
      
        if ((limit).match(/^[1-9]\d*(\d+)?$/)){//this ensures: no leading 0; only 0-9 (no dot or minus)
            limit  = parseInt(limit);
            if (limit<=120){
               allData.time_limit = limit;
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
              question:"",
              explanation:"",
              answers:[]
          };
          
          //question text
          var questionId = $(this).data('id');
          nextQuestion.question = $.grep(allMdes, function(e){ return e.id === questionId; })[0].value().trim();
          

       
          if (nextQuestion.question.length===0){
              $(this).find(".row").eq(0).addClass("error-highlight");
              valid = false;
          }
          
          //explanation
          nextQuestion.explanation = $(this).find("textarea").eq(2).val().trim();
        
          //answers
          var hasCorrect = false;
          $(this).find(".allOptions .option-row").each(function(ind){//this here is an option row
              
              var optObj = {
                  answer: $(this).find("input[type=text]").val().trim(),
                  correct: $(this).find("input[type=checkbox]").is(":checked")
              };
              
              if (optObj.correct===true){
                  hasCorrect = true;
              }
              
              if (optObj.answer.length===0){
                  $(this).addClass("error-highlight");
                  valid=false;
              }
              nextQuestion.answers.push(optObj);
          });
          
          if (!hasCorrect){
              valid = false;
              $(this).find(".allOptions").addClass("error-highlight");
          }
          
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
               data: JSON.stringify(allData),
               contentType:  "application/json;",
               dataType:"json",
               success: function( data, textStatus, jqXHR) {
                 
                 
                 
                 $("body").html(''+
                        '<div class="panel panel-default panel-custom">'+
                            '<div class="panel-body">'+
                                '<h2>'+data.message+'</h2>'+
                            '</div>'+
                        '</div>');
                   
                 
               },
               error: function(jqXHR, textStatus, errorMessage) {
                  
                   $("body").html(''+
                        '<div class="panel panel-default panel-custom errorPanel">'+
                            '<div class="panel-body">'+
                                '<h2>'+jqXHR.status+' '+errorMessage+'</h2>'+
                                '<h3>'+JSON.parse(jqXHR.responseText).message+'</h3>'+
                            '</div>'+
                        '</div>');
                   
               }
            });
        
        
        
    }
    
    
    
    
}