package com.github.ac31007_group_8.quiz.staff.controllers;

import com.github.ac31007_group_8.quiz.staff.models.QuizListModel;
import com.github.ac31007_group_8.quiz.util.Init;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.*;
import java.util.logging.Logger;

import com.github.ac31007_group_8.quiz.student.*;
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
        get("/student/quizList", quizList::getQuizName);
    }

    public static Object getQuizName(Request req, Response res){

        String quizStr = req.queryParams("quizID");
        Logger.getGlobal().info("got param value: " + quizStr);

        QuizListModel quizModel = new QuizListModel();
        List<Title> questionTitles = quizModel.getQuizAll();

        Gson gson = new GsonBuilder().create();
        String quizTitlesJson = gson.toJson(quizTitles);

        HashMap<String, Object> map = new HashMap<>();
        map.put("quizTitle", quizTitlesJson);

        TemplateEngine eng = new MustacheTemplateEngine();
        return eng.render(eng.modelAndView(map, "student/quizList.mustache"));

    }


}
