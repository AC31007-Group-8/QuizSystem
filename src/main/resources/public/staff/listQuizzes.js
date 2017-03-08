/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function getFiltered(){
    
    var o = {module:'AC', year:2017};
    
    
           $.ajax({
               url: "/staff/quizListFilter",
               type: "GET",
               data: o,
               contentType:  "application/json;",
               dataType:"json",
               success: function( data, textStatus, jqXHR) {
                 
                 
                 
                //display quizzes
                   
                 
               },
               error: function(jqXHR, textStatus, errorMessage) {
                  
//                   $("body").html(''+
//                        '<div class="panel panel-default panel-custom errorPanel">'+
//                            '<div class="panel-body">'+
//                                '<h2>'+jqXHR.status+' '+errorMessage+'</h2>'+
//                                '<h3>'+JSON.parse(jqXHR.responseText).message+'</h3>'+
//                            '</div>'+
//                        '</div>');
                   
               }
            });
    
    
}