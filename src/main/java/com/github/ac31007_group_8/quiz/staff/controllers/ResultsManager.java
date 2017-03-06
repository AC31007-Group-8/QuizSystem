package com.github.ac31007_group_8.quiz.staff.controllers;

import com.github.ac31007_group_8.quiz.student.models.StudentQuizModel;
import com.github.ac31007_group_8.quiz.util.Init;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.*;
import java.util.logging.Logger;

import com.github.ac31007_group_8.quiz.student.*;
import com.github.ac31007_group_8.quiz.staff.store.*;

import static spark.Spark.*;
import com.google.gson.*;
import org.apache.commons.lang3.tuple.*;
/**
 *
 * @author ronan (adapted from Can, Allan and Vlad)
 */
public class ResultsManager {
    
    
    public ResultsManager(){
        
    
    }
     
        
        @Init
    public static void init() {
        get("/staff/viewResults", ResultsManager::serveViewResults);
        post("/staff/viewResults", "application/json", ResultsManager::receiveViewResults);
    }
    
    
    
    
    public static Object serveViewResults(Request req, Response res){
        
        //read parameter
        int quizID = Integer.parseInt(req.queryParams("quizID"));
        int studentID = Integer.parseInt(req.queryParams("studentID"));
        studentID = 1; //Place holder no login atm

        //specify values to be displayed on page:
        HashMap<String, Object> map = new HashMap<>();
        map.put("quiz", quizID); //passing values to mustache to be displayed on the page by putting them in a hashmap
        map.put("testKey", "testVal");

        //render page:
        TemplateEngine eng = new MustacheTemplateEngine();
        return eng.render(eng.modelAndView(map, "staff/viewResults.mustache"));


    }
    
    public static Object receiveViewResults(Request req, Response res){
    
    
    return null; 
    
    
    }
    
    
    
}
