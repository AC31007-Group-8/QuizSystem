package com.github.ac31007_group_8.quiz.staff.controllers;

import com.github.ac31007_group_8.quiz.common.ParameterManager;
import com.github.ac31007_group_8.quiz.staff.models.StaffLoginModel;
import com.github.ac31007_group_8.quiz.staff.store.User;
import com.github.ac31007_group_8.quiz.student.controllers.StudentLoginController;
import com.github.ac31007_group_8.quiz.util.Init;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.logging.Logger;

import static spark.Spark.*;

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
        before("/staff/*", StaffLoginController::filterStaff);

    }

    public static Object serveLogin(Request req, Response res) {

        //Sat up template engine
        TemplateEngine eng = new MustacheTemplateEngine();
        HashMap<String, Object> map = ParameterManager.getAllParameters(req);

        User user = (User)req.session().attribute("user");
        if (user != null)
        {
            String roleText = "Student";
            if (user.isStaff()) roleText = "Staff Member";
            ParameterManager.writeMessage(map, "You are already logged in as " + user.getName() + " (" + roleText + "). Signing into a different account below will log you out of your current session.");
        }

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
            ParameterManager.writeMessage(map, "The login details you supplied were incorrect. Please try again.");
            return eng.render(eng.modelAndView(map, "staffLogin.mustache"));
        }

        req.session().attribute("user", user);
        map = ParameterManager.getAllParameters(req);

        res.redirect(ParameterManager.getBaseURL(req) + "/staff/quizList");
        return null;
        //return eng.render(eng.modelAndView(map, "staffLogin.mustache"));
    }

    public static Object serveLogout(Request req, Response res) {

        //Remove user data from session and note if they were a staff member (so we can redirect accordingly)
        User user = (User)req.session().attribute("user");
        boolean wasStaff = false;
        if (user != null) wasStaff = user.isStaff();
        req.session().removeAttribute("user");

        //Sat up template engine
        TemplateEngine eng = new MustacheTemplateEngine();
        HashMap<String, Object> map = ParameterManager.getAllParameters(req);

        ParameterManager.writeMessage(map, "You have been logged out of your account.");

        if (wasStaff) res.redirect(ParameterManager.getBaseURL(req) + "staff/login");
        if (!wasStaff) res.redirect(ParameterManager.getBaseURL(req) + "student/login");
        return null;
        //return eng.render(eng.modelAndView(map, "staffLogin.mustache"));
    }

    public static void filterStaff(Request req, Response res)
    {
        //do not restrict access to the login page.
        if (req.url().endsWith("staff/login"))
        {
            return;
        }
        //check if authenticated for all other pages
        User user = req.session().attribute("user");
        if (user == null || !user.isStaff())
        {
            //Set up template engine
            TemplateEngine eng = new MustacheTemplateEngine();
            HashMap<String, Object> map = ParameterManager.getAllParameters(req);
            ParameterManager.writeMessage(map, "You must log in as a staff member to access this functionality!");
            halt(401, eng.render(eng.modelAndView(map, "staffLogin.mustache")));
        }
    }


}
