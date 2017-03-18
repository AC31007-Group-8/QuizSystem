package com.github.ac31007_group_8.quiz.staff.controllers;

import com.github.ac31007_group_8.quiz.common.ParameterManager;
import com.github.ac31007_group_8.quiz.staff.models.StaffLoginModel;
import com.github.ac31007_group_8.quiz.staff.store.User;
import com.github.ac31007_group_8.quiz.util.Init;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.logging.Logger;

import static spark.Spark.get;
import static spark.Spark.halt;
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
        post("/staff/login", "application/json", StaffLoginController::receiveLogin);
        get("/logout", StaffLoginController::serveLogout);
    }

    public static Object serveLogin(Request req, Response res) {

        //Sat up template engine
        TemplateEngine eng = new MustacheTemplateEngine();
        HashMap<String, Object> map = ParameterManager.getAllParameters(req);

        Logger.getGlobal().info("User is null: " + (req.session().attribute("user")==null));

        return eng.render(eng.modelAndView(map, "staffLogin.mustache"));
    }

    public static Object receiveLogin(Request req, Response res) {

        String username = req.queryParams("username");
        String password = req.queryParams("password");

        //Set up template engine
        TemplateEngine eng = new MustacheTemplateEngine();
        HashMap<String, Object> map = ParameterManager.getAllParameters(req);

        if (username == null || username.equals("") || password == null || password.equals(""))
        {
            //display error page
            throw halt(400, eng.render(eng.modelAndView(map, "badRequest.mustache")));
        }

        StaffLoginModel loginModel = new StaffLoginModel();
        User user = loginModel.getUser(username, password);

        if (user == null)
        {
            //display invalid details page
            map = ParameterManager.getAllParameters(req);
            return eng.render(eng.modelAndView(map, "invalidUser.mustache"));
        }

        req.session().attribute("user", user);

        map = ParameterManager.getAllParameters(req);

        return eng.render(eng.modelAndView(map, "staffLogin.mustache"));
    }

    public static Object serveLogout(Request req, Response res) {

        req.session().removeAttribute("user");
        //Sat up template engine
        TemplateEngine eng = new MustacheTemplateEngine();
        HashMap<String, Object> map = ParameterManager.getAllParameters(req);

        Logger.getGlobal().info("User is null: " + (req.session().attribute("user")==null));

        return eng.render(eng.modelAndView(map, "staffLogin.mustache"));
    }


}
