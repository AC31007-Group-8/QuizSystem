
package com.github.ac31007_group_8.quiz.staff.controllers;

import com.github.ac31007_group_8.quiz.Database;
import com.github.ac31007_group_8.quiz.staff.models.QuizModel;
import com.github.ac31007_group_8.quiz.util.Init;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.*;

import com.github.ac31007_group_8.quiz.staff.*;
import com.github.ac31007_group_8.quiz.staff.store.*;

import static spark.Spark.*;
import java.sql.SQLException;
import org.jooq.DSLContext;
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
        HashMap<String, Object> map = new HashMap<>();
         
        try{
        
            ArrayList<String> moduleNames = quizModel.getModuleList(dslCont);
            map.put("allModules", moduleNames);
            
            ArrayList<QuizInfo> quizTitles = quizModel.getAllQuizInfo(dslCont);
            map.put("quizList", quizTitles);
            
            for (QuizInfo q:quizTitles){
                System.out.println(q);
            }

           
            
            res.status(200);
            TemplateEngine eng = new MustacheTemplateEngine();
            return eng.render(eng.modelAndView(map, "staff/quizList.mustache"));

        
        }
        catch (SQLException sqle){
            LOGGER.error("SQLException", sqle);
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
        
        System.out.println(isPublished+" "+moduleCode+" "+creator+" "+sortBy);
        
      
        
        DSLContext dslCont = Database.getJooq();
        QuizModel quizModel = new QuizModel();
        
        
        ArrayList<Quiz> quizTitles = quizModel.getQuizzesFiltered(dslCont,moduleCode,isPublished,creator,sortBy);
        
        //Consider filtering list by module, year, abc. Name the quizes, date aswell. whcih ones are active.
        
        
           
        
        
       
          res.status(200);
        
          return "Hello World!";
    }
    
}

