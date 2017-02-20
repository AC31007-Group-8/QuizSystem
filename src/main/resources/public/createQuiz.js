/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


//http://stackoverflow.com/questions/7477/autosizing-textarea-using-prototype



$(function(){
    
  
    addQuestion();
    
    
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
    
}

function submit(){
    
    
    
    
}