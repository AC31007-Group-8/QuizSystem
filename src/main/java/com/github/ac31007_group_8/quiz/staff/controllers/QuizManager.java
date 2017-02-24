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
import com.github.mustachejava.DefaultMustacheFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;

import java.util.HashMap;
import org.jooq.DSLContext;
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
            //here can take e.g. module list from DB or staff username
            
            map.put("allModules", allModules);
            TemplateEngine eng = new MustacheTemplateEngine();//TemplateEngine eng = new MustacheTemplateEngine(new DefaultMustacheFactory(new File("./src/main/webapp/WEB-INF")));

            return eng.render(eng.modelAndView(map, "staff/createQuiz.mustache"));
            
        }
        catch (SQLException sqle){
            System.out.println("Sqlexception in sendQuizForm :(");
               return "{\"message\":\"error occured!\"}";
        }
        
        
        
        
        
        
    }
    
    
    
     public static Object saveQuiz(Request req, Response res){
      
    String json = 
        "{"
	+ "'timeLimit':'',"
	+ "'moduleCode':'',"
	+ "'title':'',"
	+ "'questions':["
	+	"{"
	+		"'questionText':'',"
	+		"'explanation':'',"
	+		"'options':["
        +                   "{'option':'', 'correct':''}"
        +                "]"
	+	"}"
	+ "]"
        + "}";
        
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.fromJson(json, Quiz.class); //Copied from http://stackoverflow.com/questions/5128442/how-to-convert-a-string-to-jsonobject-using-gson-library 2nd Answer

        //convert this (and tell me how you did it) into properly formatted json string (as shown above) and then use GSON to parse into java beans
        System.out.println(req.body());   
       
        res.status(200);
      
       
        return "{\"message\":\"saved successfully by ERiiic\"}";
        
    }
}
