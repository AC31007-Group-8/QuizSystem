package com.github.ac31007_group_8.quiz.student.controllers;

import com.github.ac31007_group_8.quiz.common.ParameterManager;
import com.github.ac31007_group_8.quiz.staff.controllers.StaffLoginController;
import com.github.ac31007_group_8.quiz.staff.store.User;
import com.github.ac31007_group_8.quiz.student.models.StudentLoginModel;
import com.github.ac31007_group_8.quiz.util.Init;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.logging.Logger;

import static spark.Spark.*;

/**
 * Created by Can on 16/03/2017.
 */
public class StudentLoginController {

    public StudentLoginController(){

    }

    @Init
    public static void init() {
        get("/student/login", StudentLoginController::serveLogin);
        post("/student/login", "application/json", StudentLoginController::receiveLogin);
        before("/student/*", StudentLoginController::filterStudent);
    }

    public static Object serveLogin(Request req, Response res) {

        //Sat up template engine
        TemplateEngine eng = new MustacheTemplateEngine();
        HashMap<String, Object> map = ParameterManager.getAllParameters(req);

        return eng.render(eng.modelAndView(map, "studentLogin.mustache"));
    }

    public static Object receiveLogin(Request req, Response res) {

        String username = req.queryParams("username");
        String password = req.queryParams("password");

        //Set up template engine
        TemplateEngine eng = new MustacheTemplateEngine();
        HashMap<String, Object> map = ParameterManager.getAllParameters(req);

        if (username == null || username.equals("") || password == null || password.equals(""))
        {
            //handle invalid input
            ParameterManager.writeMessage(map, "All of the fields below need to be filled in. Please try again.");
            return eng.render(eng.modelAndView(map, "studentLogin.mustache"));
        }

        StudentLoginModel loginModel = new StudentLoginModel();
        User user = loginModel.getUser(username, password);

        if (user == null)
        {
            //display invalid details message
            ParameterManager.writeMessage(map, "The login details you supplied were incorrect. Please try again.");
            return eng.render(eng.modelAndView(map, "studentLogin.mustache"));
        }

        req.session().attribute("user", user);
        
        map = ParameterManager.getAllParameters(req); //update after login

        res.redirect(map.get("baseURL") + "/student/studentQuizList");
        return null;
        //return eng.render(eng.modelAndView(map, "studentLogin.mustache"));
    }

    public static void filterStudent(Request req, Response res)
    {
        //do not restrict access to the login page.
        if (req.url().endsWith("student/login"))
        {
            return;
        }
        //check if authenticated for all other pages
        User user = req.session().attribute("user");
        if (user == null || user.isStaff())
        {
            //Set up template engine
            TemplateEngine eng = new MustacheTemplateEngine();
            HashMap<String, Object> map = ParameterManager.getAllParameters(req);
            ParameterManager.writeMessage(map, "You must log in as a student to access this functionality!");
            throw halt(401, eng.render(eng.modelAndView(map, "studentLogin.mustache")));
        }
    }

}
