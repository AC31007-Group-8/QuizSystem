
package com.github.ac31007_group_8.quiz.student.controllers;

import com.github.ac31007_group_8.quiz.Database;
import com.github.ac31007_group_8.quiz.student.models.QuizModelStudent;
import com.github.ac31007_group_8.quiz.staff.store.QuizInfo;
import com.github.ac31007_group_8.quiz.util.Init;
import java.sql.SQLException;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import org.jooq.DSLContext;

import static spark.Spark.*;


/**
 * @author Callum N
 */
public class StudentQuizList {

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

            HashMap<String, Object> map = new HashMap<>();
            map.put("quizList", quizTitles);
            res.status(200);
            TemplateEngine eng = new MustacheTemplateEngine();
            return eng.render(eng.modelAndView(map, "student/quizListStudent.mustache"));
        }
        catch (SQLException sqle){
            return null;
        }
        
        

    }


}

