/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function getFiltered(contextPath){
    
    
    var choices = {
        relevant : $("#relevanrSelect").val(),
        moduleCode: $("#quizModule").val(),
        taken : $("#takenSelect").val(),
        sortBy: $("#sortBy").val()
 
    };
    
    $.ajax({
        url: contextPath+"/student/quizList/filter",
        type: "GET",
        data: choices,
        contentType:  "application/json;",
        dataType:"json",
        success: function( data, textStatus, jqXHR) {

           createTable(data);

        },
        error: function(jqXHR, textStatus, errorMessage) {
            $("#contentHolder").html('');
            $("#contentHolder").append('<h2>'+jqXHR.status+' '+errorMessage+'</h2>');
        }
     });
    
    
    function createTable(data){
        
        $("#contentHolder").html('');
        
        
        if (data.length > 0) {
            $("#contentHolder").append('<table class="table">' +
                    '<thead>' +
                    '<tr>' +
                    '<th>Relevant?</th>' +
                    '<th>Taken?</th>' +
                    '<th>Module code</th>' +
                    '<th>Module name</th>' +
                    '<th>Title</th>' +
                    '<th>Time limit</th>' +
                    '<th>Take</th>' +
                    '</tr>' +
                    '</thead>' +
                    '<tbody>' +
                    '</tbody>' +
                    '</table>');



            data.forEach(function (nextQuizInfo, index) {

              

                var timeL = (typeof nextQuizInfo.time_limit === 'undefined')?"":nextQuizInfo.time_limit;

                $("tbody").append('<tr>' +
                        '<td>' + (nextQuizInfo.isRelevant === true ? '<span class="glyphicon glyphicon-ok-circle" ></span>' : '<span class="glyphicon glyphicon-remove" ></span>') + '</td>' +
                        '<td>' + (nextQuizInfo.isTaken === true ? '<span class="glyphicon glyphicon-ok-circle" ></span>' : '<span class="glyphicon glyphicon-remove" ></span>') + '</td>' +
                        '<td>' + nextQuizInfo.module_id + '</td>' +
                        '<td>' + nextQuizInfo.module_name + '</td>' +
                        '<td>' + nextQuizInfo.title + '</td>' +
                        '<td>' + timeL + '</td>' +
                        '<td><a class="btn btn-primary btn-sm" href="'+contextPath+'/student/takeQuiz?quizID=' + nextQuizInfo.quizId + '">Take</a></td>' +
                        
                        '</tr>'
                        );
            });
        }
        else{
            
            $("#contentHolder").append('<table class="table">' +
                    '<thead>' +
                    '<tr>' +
                    '<th>Relevant?</th>' +
                    '<th>Taken?</th>' +
                    '<th>Module code</th>' +
                    '<th>Module name</th>' +
                    '<th>Title</th>' +
                    '<th>Time limit</th>' +
                    '<th>Take</th>' +
                    '</tr>' +
                    '</thead>' +
                    '<tbody>' +
                    '<tr><td><h2>No results found</h2></td></tr>'+
                    '</tbody>' +
                    '</table>');
 
        }
        
        
 
 
    }
    
}