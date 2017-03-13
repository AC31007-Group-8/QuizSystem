/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function getFiltered(){
    
    
    var choices = {
        published : $("#publishStatusSelect").val(),
        moduleCode: $("#quizModule").val(),
        creator: $("#creatorNameSurname").val(),
        sortBy: $("#sortBy").val()
 
    };
    
    $.ajax({
        url: "/staff/quizList/filter",
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
                    '<th>Published?</th>' +
                    '<th>Module code</th>' +
                    '<th>Module name</th>' +
                    '<th>Title</th>' +
                    '<th>Time limit</th>' +
                    '<th>Creator</th>' +
                    '<th>Edit</th>' +
                    '<th>View</th>' +
                    '</tr>' +
                    '</thead>' +
                    '<tbody>' +
                    '</tbody>' +
                    '</table>');

            data.forEach(function (nextQuizInfo, index) {

                $("tbody").append('<tr>' +
                        '<td>' + (nextQuizInfo.publish_status === true ? '<span class="glyphicon glyphicon-ok-circle" ></span>' : '<span class="glyphicon glyphicon-remove" ></span>') + '</td>' +
                        '<td>' + nextQuizInfo.module_id + '</td>' +
                        '<td>' + nextQuizInfo.module_name + '</td>' +
                        '<td>' + nextQuizInfo.title + '</td>' +
                        '<td>' + nextQuizInfo.time_limit + '</td>' +
                        '<td>' + nextQuizInfo.first_name + ' ' + nextQuizInfo.second_name + '</td>' +
                        '<td><a class="btn btn-primary btn-sm" href="/staff/viewResults?quizID=' + nextQuizInfo.quizId + '">View</a></td>' +
                        '<td><a class="btn btn-primary btn-sm" href="/staff/EDITQUIZ?quizID=' + nextQuizInfo.quizId + '">Edit</a></td>' +
                        '</tr>'
                        );
            });
        }
        else{
            
            $("#contentHolder").append('<table class="table">' +
                    '<thead>' +
                    '<tr>' +
                    '<th>Published?</th>' +
                    '<th>Module code</th>' +
                    '<th>Module name</th>' +
                    '<th>Title</th>' +
                    '<th>Time limit</th>' +
                    '<th>Creator</th>' +
                    '<th>Edit</th>' +
                    '<th>View</th>' +
                    '</tr>' +
                    '</thead>' +
                    '<tbody>' +
                    '<tr><td><h2>No results found</h2></td></tr>'+
                    '</tbody>' +
                    '</table>');
 
        }
        
        
 
 
    }
    
}