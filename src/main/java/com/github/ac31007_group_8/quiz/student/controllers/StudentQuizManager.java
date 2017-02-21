package com.github.ac31007_group_8.quiz.student.controllers;

import spark.Request;
import spark.Response;
import spark.TemplateEngine;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

/**
 * @author Can G, Allan M
 */
public class StudentQuizManager {

    public StudentQuizManager(){

    }

    public static Object serveTakeQuiz(Request req, Response res){

        HashMap<String, Object> map = new HashMap<>();

        TemplateEngine eng = new MustacheTemplateEngine();
        return eng.render(eng.modelAndView(map, "student/takeQuiz.mustache"));

    }

    public static Object receiveTakeQuiz(Request req, Response res){

        //TODO: Implement
        System.out.println(req.body());
        res.status(200);
        return "end";
    }


}
