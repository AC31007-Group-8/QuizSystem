
package com.github.ac31007_group_8.quiz.student.controllers;

import com.github.ac31007_group_8.quiz.common.ParameterManager;
import com.github.ac31007_group_8.quiz.staff.models.QuizModel;
import com.github.ac31007_group_8.quiz.staff.store.Quiz;
import com.github.ac31007_group_8.quiz.util.Init;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

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

        QuizModel quizModel = new QuizModel();
        ArrayList<Quiz> quizTitles = quizModel.getQuizAll();

        HashMap<String, Object> map = ParameterManager.getAllParameters(req);
        map.put("quizList", quizTitles);
        res.status(200);
        TemplateEngine eng = new MustacheTemplateEngine();
        return eng.render(eng.modelAndView(map, "quizListStudent.mustache"));

    }


}

