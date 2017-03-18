
package com.github.ac31007_group_8.quiz.student.controllers;

import com.github.ac31007_group_8.quiz.common.ParameterManager;
import com.github.ac31007_group_8.quiz.staff.models.QuizModel;
import com.github.ac31007_group_8.quiz.staff.store.Quiz;
import com.github.ac31007_group_8.quiz.util.Init;
import com.github.ac31007_group_8.quiz.Database;
import com.github.ac31007_group_8.quiz.staff.controllers.QuizManager;
import com.github.ac31007_group_8.quiz.student.models.QuizModelStudent;
import com.github.ac31007_group_8.quiz.staff.store.QuizInfo;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
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
        get("/student/studentQuizList", StudentQuizList::getQuizName);
    }

    public static Object getQuizName(Request req, Response res){

        QuizModelStudent qms = new QuizModelStudent();
        DSLContext dslCont = Database.getJooq();

        try{
            ArrayList<QuizInfo> quizTitles = qms.getAllQuizInfo(dslCont);
            //LOGGER.info("Quiz Count: " + quizTitles.size());
            HashMap<String, Object> map = ParameterManager.getAllParameters(req);
            map.put("quizList", quizTitles);
            res.status(200);
            TemplateEngine eng = new MustacheTemplateEngine();
            return eng.render(eng.modelAndView(map, "quizListStudent.mustache"));
        }
        catch (DataAccessException dae){
            LOGGER.error("SQL exception happened!", dae);
            return "Sql exception";
        }

    }


}

