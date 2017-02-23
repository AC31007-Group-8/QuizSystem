/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ac31007_group_8.quiz.student.controllers;
import com.github.ac31007_group_8.quiz.Database;

import com.github.ac31007_group_8.quiz.student.models.QuizModelStudent;
import com.github.ac31007_group_8.quiz.student.stores.QuizInfo;
import com.github.ac31007_group_8.quiz.util.Init;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import spark.Request;
import spark.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static spark.Spark.get;
import static spark.Spark.post;


/**
 *
 * @author Vlad
 */
public class QuizManagerStudent {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(QuizManagerStudent.class);
    
    
    @Init
    public static void init() {
         get("/student/relevantQuizzes", QuizManagerStudent::sendRelevantQuizzes);
    }
    
    public static Object sendRelevantQuizzes(Request req, Response res){
        
        int studentId = 1; //get from session!
        
        Connection conn = Database.getConnection();
        QuizModelStudent qms = new QuizModelStudent();
        
        try{
            
            ArrayList<QuizInfo> allQuizzes = qms.getRelevantQuizzes(studentId, conn);


            //for Callum:
            //instead of this make a page and fill it with data (no need to convert to gson).
            //mustache stuff
            
            String json = new Gson().toJson(allQuizzes);
            System.out.println(json);
            res.status(200);
            return json;
            
            
        }
        catch(SQLException sqle){
            System.out.println("SQL exception happened :(");
        }
        
        
        return null;
        
        
        
    }
    
  
      
    
    
}







