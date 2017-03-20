
package com.github.ac31007_group_8.quiz.student.controllers;

import com.github.ac31007_group_8.quiz.common.ParameterManager;
import com.github.ac31007_group_8.quiz.util.Init;
import com.github.ac31007_group_8.quiz.Database;
import com.github.ac31007_group_8.quiz.staff.controllers.QuizManager;
import com.github.ac31007_group_8.quiz.staff.store.QuizInfoStudent;
import com.github.ac31007_group_8.quiz.student.models.StudentQuizModel;
import java.sql.Connection;
import java.sql.SQLException;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.LoggerFactory;

import static spark.Spark.*;


/**
 * @author Callum N
 */
public class StudentQuizList {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(QuizManager.class);

    public StudentQuizList(){

    }

    @Init
    public static void init() {
        get("/student/relevantQuizzes", StudentQuizList::sendRelevantQuizzes);//relevant to student. displayed when page visited
     
       
        get("/student/quizList/filter", StudentQuizList::getFilteredQuizList);
        
    }

    
    //SEND RELEVANT QUIZZES
    public static Object sendRelevantQuizzes(Request req, Response res){
        
        
        HashMap<String, Object> map = ParameterManager.getAllParameters(req); 
        TemplateEngine eng = new MustacheTemplateEngine();
        
        int studentId = 1; //get from session!
        
        Connection conn = Database.getConnection();
        StudentQuizModel qms = new StudentQuizModel();
        
        try{
            
            ArrayList<QuizInfoStudent> allQuizInfo = qms.getRelevantQuizzes(studentId, conn);


         
            map.put("quizList", allQuizInfo);
            res.status(200);
           
            return eng.render(eng.modelAndView(map, "quizListStudent.mustache"));
            
            
        }
        catch(SQLException sqle){
            LOGGER.error("SQL exception happened !", sqle);
            return "sql exception";
        }
        

        
    }
    
    
    
    
   
    
    
    public static Object getFilteredQuizList(Request req, Response res){
        
        res.status(200);
        return null;
    }

}

