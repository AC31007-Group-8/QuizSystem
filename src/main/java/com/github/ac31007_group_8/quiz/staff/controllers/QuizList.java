
package com.github.ac31007_group_8.quiz.staff.controllers;

import com.github.ac31007_group_8.quiz.Database;
import com.github.ac31007_group_8.quiz.common.ParameterManager;
import com.github.ac31007_group_8.quiz.Database;
import com.github.ac31007_group_8.quiz.common.ParameterManager;
import com.github.ac31007_group_8.quiz.staff.models.QuizModel;
import com.github.ac31007_group_8.quiz.util.Init;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.*;

import com.github.ac31007_group_8.quiz.staff.store.*;
import com.google.gson.Gson;

import static spark.Spark.*;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Callum N
 */
public class QuizList {

    public QuizList(){

    }

    private static final Logger LOGGER = LoggerFactory.getLogger(QuizList.class);
    
    @Init
    public static void init() {
        get("/staff/quizList", QuizList::getQuizList);
        get("/staff/quizList/filter", QuizList::getFilteredQuizList);
    }

    public static Object getQuizList(Request req, Response res){

        DSLContext dslCont = Database.getJooq();
        QuizModel quizModel = new QuizModel();

        HashMap<String, Object> map = ParameterManager.getAllParameters(req);
         
        try{
        
            ArrayList<String> moduleNames = quizModel.getModuleList(dslCont);
            map.put("allModules", moduleNames);
            
            ArrayList<QuizInfo> quizTitles = quizModel.getAllQuizInfo(dslCont);
            map.put("quizList", quizTitles);
            
          
            
            res.status(200);
            TemplateEngine eng = new MustacheTemplateEngine();
            return eng.render(eng.modelAndView(map, "quizList.mustache"));

        
        }
        catch (DataAccessException dae){
            LOGGER.error("SQLException", dae);
            res.status(500);
            return "Database error";//or make error page?   
        }
        catch(Exception e){
            LOGGER.error("SQLException", e);
            res.status(500);
            return "Exception occured!";
        }

    }


    public static Object getFilteredQuizList(Request req, Response res){
        
       
        String isPublished = req.queryParams("published");
        String moduleCode = req.queryParams("moduleCode");
        String creator = req.queryParams("creator");
        String sortBy = req.queryParams("sortBy");
        
        if (isPublished==null || moduleCode==null || creator == null || sortBy==null){//no such parameter
            res.status(400);
            return "{\"message\":\"Bad input!\"}";
        }
        
        DSLContext dslCont = Database.getJooq();
        QuizModel quizModel = new QuizModel();
        
        try{

            ArrayList<QuizInfo> quizTitles = quizModel.getQuizzesFiltered(dslCont,moduleCode,isPublished,creator,sortBy);
            String json = new Gson().toJson(quizTitles);
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

