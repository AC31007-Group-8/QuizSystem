
package com.github.ac31007_group_8.quiz.student.controllers;

import com.github.ac31007_group_8.quiz.common.ParameterManager;
import com.github.ac31007_group_8.quiz.util.Init;
import com.github.ac31007_group_8.quiz.Database;
import static com.github.ac31007_group_8.quiz.Database.getConnection;
import com.github.ac31007_group_8.quiz.staff.controllers.QuizManager;
import com.github.ac31007_group_8.quiz.staff.models.QuizModel;
import com.github.ac31007_group_8.quiz.staff.store.QuizInfo;
import com.github.ac31007_group_8.quiz.staff.store.QuizInfoStudent;
import com.github.ac31007_group_8.quiz.staff.store.User;
import com.github.ac31007_group_8.quiz.student.models.StudentQuizModel;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.SQLException;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;

import org.slf4j.LoggerFactory;

import static spark.Spark.*;


/**
 * @Deprecated
 * @author Callum N
 * 
 * @author Vlad
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
        
        
        User currentUser = (User)map.get("user");
        if (currentUser==null){
            LOGGER.error("impossible happened");
            res.status(401);
            return null ;
        }
        
        int studentId = currentUser.getUserid();
        
        
        Connection conn = Database.getConnection();
        StudentQuizModel sqm = new StudentQuizModel();
        
        try{
            
            //module list wants dslcontext
            QuizModel quizModel = new QuizModel();
            ArrayList<String> moduleNames = quizModel.getModuleList(DSL.using(conn, SQLDialect.MYSQL));
            map.put("allModules", moduleNames);
            
            //old getRelevantQuizzes wants connection :)
            ArrayList<QuizInfoStudent> allQuizInfo = sqm.getRelevantQuizzes(studentId, conn);


         
            map.put("quizList", allQuizInfo);
            res.status(200);
           
            return eng.render(eng.modelAndView(map, "quizListStudent.mustache"));
            
            
        }
        catch(SQLException sqle){
            LOGGER.error("SQL exception happened !", sqle);
            res.status(500);
            return null;
        }
        

        
    }
    
    
    
    
   
    
    
    public static Object getFilteredQuizList(Request req, Response res){
        
        HashMap<String, Object> map = ParameterManager.getAllParameters(req); 
        
        String relevant = req.queryParams("relevant");
        String taken = req.queryParams("taken");
        String moduleCode = req.queryParams("moduleCode");
        String sortBy = req.queryParams("sortBy");
        
        if (relevant==null || moduleCode==null || taken == null || sortBy==null){//no such parameter
            res.status(400);
            return "{\"message\":\"Bad input!\"}";
        }
        
        User currentUser = (User)map.get("user");
        if (currentUser==null){
            LOGGER.error("impossible happened");
            res.status(401);
            return null ;
        }
        
        int studentId = currentUser.getUserid();
        
        
        
        DSLContext dslCont = Database.getJooq();
        StudentQuizModel quizModel = new StudentQuizModel();
        
        try{

            ArrayList<QuizInfoStudent> quizTitles = quizModel.getFilteredQuizInfo(dslCont,moduleCode,sortBy, taken, relevant, studentId);
            String json = new Gson().toJson(quizTitles);
            System.out.println(json);
            res.status(200);  
            return json;
          
        }
        catch (DataAccessException dae){
            LOGGER.error("SQL Exception occured!", dae);
            res.status(500);
            return "{\"message\":\"Exception occured\"}";
        }
        catch (Exception e){
            LOGGER.error("Exception :(", e);
            res.status(500);
            return "{\"message\":\"Exception occured\"}";
        }
        
  
    }

}

