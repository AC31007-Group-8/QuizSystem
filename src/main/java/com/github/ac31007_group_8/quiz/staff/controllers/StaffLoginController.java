package com.github.ac31007_group_8.quiz.staff.controllers;

import com.github.ac31007_group_8.quiz.util.Init;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by Can on 07/03/2017.
 */
public class StaffLoginController {

    public StaffLoginController(){

    }

    @Init
    public static void init() {
        get("/staff/login", StaffLoginController::serveLogin);
        //post("/staff/login", "application/json", StaffLoginController::receiveTakeQuiz);
    }

    public static Object serveLogin(Request req, Response res) {

        //Sat up template engine
        TemplateEngine eng = new MustacheTemplateEngine();
        HashMap<String, Object> map = new HashMap<>();

        return eng.render(eng.modelAndView(map, "staff/login.mustache"));
    }

}
