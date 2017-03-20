
package com.github.ac31007_group_8.quiz.staff.controllers;

import com.github.ac31007_group_8.quiz.Database;
import com.github.ac31007_group_8.quiz.common.ParameterManager;
import com.github.ac31007_group_8.quiz.Database;
import com.github.ac31007_group_8.quiz.common.ParameterManager;
import com.github.ac31007_group_8.quiz.staff.models.QuizModel;
import com.github.ac31007_group_8.quiz.student.models.StudentQuizModel;
import com.github.ac31007_group_8.quiz.util.Init;
import io.github.gitbucket.markedj.Marked;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.*;

import com.github.ac31007_group_8.quiz.staff.*;
import com.github.ac31007_group_8.quiz.staff.store.*;
import com.google.gson.Gson;

import static spark.Spark.*;
import java.sql.SQLException;
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
        get("/staff/previewQuiz", QuizList::servePreviewQuiz);
        post("/staff/changePublishStatus", QuizList::changePublishStatus);
    }

    public static Object servePreviewQuiz(Request req, Response res){
        TemplateEngine eng = new MustacheTemplateEngine();
        HashMap<String, Object> map = ParameterManager.getAllParameters(req);

        //Validate quizID - is valid input provided?
        String quizIDString = req.queryParams("quizID");
        if (quizIDString == (null) || quizIDString.equals(""))
        {
            //display error page
            throw halt(400, eng.render(eng.modelAndView(map, "invalidQuiz.mustache")));
        }
        int quizID = Integer.parseInt(quizIDString);

        StudentQuizModel quizModel = new StudentQuizModel();
        Quiz quiz = quizModel.getCompleteQuiz(quizID);

        String publishStatus = "Not Published";
        String publishTarget = "true";
        if (quiz.isPublish_status()) {
            publishStatus = "Published";
            publishTarget = "false";
        }
        map.put("publishStatus", publishStatus);
        map.put("publishTarget", publishTarget);
        map.put("quizID", quizIDString);

        List<Question> allQuestions = quiz.getQuestions();
        int ind=1;
        for (Question q:allQuestions){

            q.setQuestion(Marked.marked(q.getQuestion()));
            q.setQuestionIndex(ind);
            ind++;
        }

        map.put("quizTitle", quiz.getTitle());
        map.put("moduleCode", quiz.getModule_id());

        Integer inMinutes = quiz.getTime_limit();
        if (inMinutes == null){
            map.put("timeLimitOfQuiz", "-");
        }
        else{
            int h = inMinutes/60;
            int m = inMinutes - h*60;

            map.put("timeLimitOfQuiz",((h+"").length()<2?"0"+h:h)+":"+((m+"").length()<2?"0"+m:m)+":00");
        }
        map.put("timeLeft", inMinutes);

        map.put("allQuestions", allQuestions);

        return eng.render(eng.modelAndView(map, "previewQuiz.mustache"));
    }

    public static Object changePublishStatus(Request req, Response res){

        TemplateEngine eng = new MustacheTemplateEngine();
        HashMap<String, Object> map = ParameterManager.getAllParameters(req);

        boolean targetStatus = false;
        if (req.queryParams("publishTarget").equals("true")) targetStatus = true;

        //Validate quizID - is valid input provided?
        String quizIDString = req.queryParams("quizID");
        if (quizIDString == (null) || quizIDString.equals("") || req.queryParams("publishTarget") == null)
        {
            //display error page
            ParameterManager.writeMessage(map, "Quiz publish status could not be changed - please try again.");
            throw halt(400, eng.render(eng.modelAndView(map, "invalidQuiz.mustache")));
            //return  eng.render(eng.modelAndView(map, "invalidQuiz.mustache"));
        }
        int quizID = Integer.parseInt(quizIDString);

        QuizModel quizModel = new QuizModel();
        quizModel.setPublishStatus( quizID, targetStatus);

        res.redirect("previewQuiz?quizID="+quizIDString);
        return null;

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
            
            for (QuizInfo q:quizTitles){
                System.out.println(q);
            }
            
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

