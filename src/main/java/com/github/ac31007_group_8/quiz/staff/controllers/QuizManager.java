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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Vlad
 */
public class QuizManager {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(QuizManager.class);
    
    
    public QuizManager(){
        
        
    }
    
    
    public static Object sendQuizForm(Request req, Response res){
        
        //here can take e.g. module list from DB or staff username
        HashMap<String, Object> map = new HashMap<>();
        
        TemplateEngine eng = new MustacheTemplateEngine();//TemplateEngine eng = new MustacheTemplateEngine(new DefaultMustacheFactory(new File("./src/main/webapp/WEB-INF")));
         
        return eng.render(eng.modelAndView(map, "staff/createQuiz.mustache"));
        
        
        
    }
    
    
    
     public static Object saveQuiz(Request req, Response res){
        
//        data:{
//            timeLimit:"",
//            moduleCode:"",
//            title:"",
//            questions:[
//                    {
//                            questionText:"",
//                            explanation:"",
//                            options:[{option:"", correct:true}, {option:"",correct:false},...]
//                    },
//                    {...}
//            ]
//        } 

        //convert this (and tell me how you did it) into properly formatted json string (as shown above) and then use GSON to parse into java beans
        System.out.println(req.body());   
       
        res.status(200);
      
       
        return "{\"message\":\"saved successfully by ERiiic\"}";
        
        
    }
}
