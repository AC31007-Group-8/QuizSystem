package com.github.ac31007_group_8.quiz.student.controllers;

import com.github.ac31007_group_8.quiz.student.models.StudentQuizModel;
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
 * @author Kerr (adapted from Can and Allan)
 */
public class StudentResultManager {

    public StudentResultManager(){

    }

    @Init
    public static void init() {
        get("/student/getResult", StudentResultManager::serveGetResult);
        post("/student/getResult", "application/json", StudentResultManager::receiveGetResult);
    }

    public static Object serveGetResult(Request req, Response res){

        int quizID = 1;
        String quizStr = req.queryParams("quizID");
        Logger.getGlobal().info("got param value: " + quizStr);

        StudentQuizModel quizModel = new StudentQuizModel();
        List<QuizSection> questionSets = quizModel.getQuizSections(quizID);

        Gson gson = new GsonBuilder().create();
        String questionSetsJson = gson.toJson(questionSets);

        HashMap<String, Object> map = new HashMap<>();
        map.put("quizID", quizStr);
        map.put("questionSets", questionSetsJson);
        map.put("testKey", "testVal");

        TemplateEngine eng = new MustacheTemplateEngine();
        return eng.render(eng.modelAndView(map, "student/getResult.mustache"));

    }

    public static Object receiveGetResult(Request req, Response res){

        //TODO: Implement
        System.out.println(req.body());
        res.status(200);
        return "end";
    }


}
