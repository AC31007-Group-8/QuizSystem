/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ac31007_group_8.quiz.staff.controllers;

import com.github.mustachejava.DefaultMustacheFactory;
import java.io.File;
import java.util.HashMap;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;
import spark.template.mustache.MustacheTemplateEngine;

/**
 *
 * @author Vlad
 */
public class QuizManager {
    
    
    
    public QuizManager(){
        
        
    }
    
    
    public static Object sendQuizForm(Request req, Response res){
        
        HashMap<String, Object> map = new HashMap<>();
        //TemplateEngine eng = new MustacheTemplateEngine(new DefaultMustacheFactory(new File("./src/main/webapp/WEB-INF")));
        TemplateEngine eng = new MustacheTemplateEngine();
         
        return eng.render(eng.modelAndView(map, "createQuiz.mustache"));
        
        
        
    }
    
    
    
     public static Object saveQuiz(Request req, Response res){
        
       
         res.status(200);
      
        //res.type("application/json");
        return "{\"message\":\"saved successfully\"}";
        
        
    }
}
