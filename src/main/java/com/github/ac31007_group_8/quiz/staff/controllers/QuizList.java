
package com.github.ac31007_group_8.quiz.staff.controllers;

import com.github.ac31007_group_8.quiz.staff.models.QuizListModel;
import com.github.ac31007_group_8.quiz.util.Init;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.*;
import java.util.logging.Logger;

import com.github.ac31007_group_8.quiz.staff.*;
import com.github.ac31007_group_8.quiz.staff.store.*;

import static spark.Spark.*;
import com.google.gson.*;
import org.apache.commons.lang3.tuple.*;


/**
 * @author Callum N
 */
public class QuizList {

    public QuizList(){

    }

    @Init
    public static void init() {
        get("/staff/quizList", QuizList::getQuizName);
    }

    public static Object getQuizName(Request req, Response res){

        String quizStr = req.queryParams("quizID");
        Logger.getGlobal().info("got param value: " + quizStr);

        QuizListModel quizListModel = new QuizListModel();
        Quiz quizTitles = quizListModel.getQuizAll();

        Gson gson = new GsonBuilder().create();
        String quizTitlesJson = gson.toJson(quizTitles);

        HashMap<String, Object> map = new HashMap<>();
        map.put("title", quizTitlesJson);

        TemplateEngine eng = new MustacheTemplateEngine();
        return eng.render(eng.modelAndView(map, "staff/quizList.mustache"));

    }


}

