/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ac31007_group_8.quiz.staff.controllers;

import com.github.ac31007_group_8.quiz.Database;
import com.github.ac31007_group_8.quiz.staff.models.QuizModel;
import com.github.ac31007_group_8.quiz.staff.store.Answer;
import com.github.ac31007_group_8.quiz.staff.store.Question;
import com.github.ac31007_group_8.quiz.staff.store.Quiz;
import java.sql.SQLException;
import java.util.ArrayList;
import com.github.ac31007_group_8.quiz.util.Init;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;
import java.util.List;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;
import spark.template.mustache.MustacheTemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.*;

/**
 *
 * @author Vlad
 */
public class QuizManager {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(QuizManager.class);
    
    
    public QuizManager(){
        
        
    }

    @Init
    public static void init() {
        get("/staff/createQuiz", QuizManager::sendQuizForm);
        post("/staff/createQuiz","application/json", QuizManager::saveQuiz);
    }
    
    
    public static Object sendQuizForm(Request req, Response res){
       
        QuizModel qm = new QuizModel();
        HashMap<String, Object> map = new HashMap<>();
        
        try{
            DSLContext dslCont = Database.getJooq(); //Connects to the database
       
            ArrayList<String> allModules = qm.getModuleList(dslCont);
        
        
            System.out.println(allModules);
            
            
            map.put("allModules", allModules);
            TemplateEngine eng = new MustacheTemplateEngine();//TemplateEngine eng = new MustacheTemplateEngine(new DefaultMustacheFactory(new File("./src/main/webapp/WEB-INF")));

            return eng.render(eng.modelAndView(map, "staff/createQuiz.mustache"));
            
        }
        catch (DataAccessException dae){
            LOGGER.error("Sql Exception occured!", dae);
            res.status(500);
            return "{\"message\":\"error occured!\"}";
        }
        

    }
    
    
    
     public static Object saveQuiz(Request req, Response res){
      
     
         long start =  System.currentTimeMillis();
         
//    String json = 
//        "{"
//	+ "'time_limit':'',"
//	+ "'module_code':'',"
//	+ "'title':'gavno',"
//	+ "'questions':["
//	+	"{"
//	+		"'question':'',"
//	+		"'explanation':'',"
//	+		"'answers':["
//        +                   "{'answer':'', 'correct':''}"
//        +                "]"
//	+	"}"
//	+ "]"
//        + "}";
        Gson gson = new GsonBuilder().serializeNulls().create();

        try{

            Quiz quizToSave = gson.fromJson(req.body(), Quiz.class); 
            DSLContext dslCont = Database.getJooq(); 
            QuizModel qm = new QuizModel();
            
            
            boolean isValid = validateQuiz(quizToSave);

            if (isValid){
                int staffId = 1; // get from session, if not logged in -- 401
                quizToSave.setStaff_id(staffId);

                qm.saveQuiz(quizToSave,dslCont );


                res.status(200);
                System.out.println( System.currentTimeMillis()-start);
                return "{\"message\":\"Saved successfully!\"}";
            }
            else{
                res.status(400);
                return "{\"message\":\"Bad input! This shouldn't happen if you don't mess with javascript!\"}";
            }
        
        }
        catch(JsonSyntaxException e ){
            LOGGER.error("Could not parse json", e);
            res.status(400);
            return "{\"message\":\"Bad input! This shouldn't happen if you don't mess with javascript!\"}";
        }      
        catch (DataAccessException e){
            LOGGER.error("Data access violation", e);
            res.status(400);
            return "{\"message\":\"Bad input! This shouldn't happen if you don't mess with javascript!\"}";
        }
       

    }
     
     
     
     private static boolean validateQuiz(Quiz quizToSave) {

        //time_limit            
        Integer timeLimit = quizToSave.getTime_limit();
        if (timeLimit!=null && (timeLimit>120 || timeLimit<1))return false;//if time_limit not integer, jsonParseException
        
        //module_code 
        //if modified module_code values => foreign constraint fails => DataAccessException
         
        //title
        String title = quizToSave.getTitle();
        if (title == null || title.isEmpty()) return false;
        
        //questions + answers
        List<Question> allQuestions = quizToSave.getQuestions();
        if (allQuestions.isEmpty()) return false;//at least 1 question
        
        for (Question nextQuestion:allQuestions){
            
            String qText = nextQuestion.getQuestion();
            if (qText == null || qText.isEmpty()) return false;// has question text
            
            ArrayList<Answer> answers = nextQuestion.getAnswers();
           
            boolean hasCorrect=false;
            for (Answer a: answers){
                String aText = a.getAnswer();
                if (aText == null || aText.isEmpty()) return false; // each answer has text
                if (a.isCorrect()){hasCorrect = true;}//at least 1 correct answer for each question
            }
            if (!hasCorrect)return false;
        }
         
        return true;
     }
}
