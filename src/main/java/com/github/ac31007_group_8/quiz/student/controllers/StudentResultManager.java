package com.github.ac31007_group_8.quiz.student.controllers;

import com.github.ac31007_group_8.quiz.student.models.StudentQuizModel;
import com.github.ac31007_group_8.quiz.student.models.StudentResultModel;
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
 * @author Kerr, Allan (adapted from Can and Allan)
 */
public class StudentResultManager {

    public StudentResultManager(){

    }

    @Init
    public static void init() {
        get("/student/getResult", StudentResultManager::serveGetResult);
        post("/student/getResult", "application/json", StudentResultManager::receiveGetResult); //Don't think this needs a post
    }

    public static Object serveGetResult(Request req, Response res){

        TemplateEngine eng = new MustacheTemplateEngine();
        HashMap<String, Object> map = new HashMap<>();

        String quizIDString = req.queryParams("quizID");
        int resultID = Integer.parseInt(req.queryParams("resultID"));
        if (quizIDString == (null) || quizIDString == "")
        {
            //display error page
            throw halt(400, eng.render(eng.modelAndView(map, "student/invalidQuiz.mustache")));
        }
        int quizID = Integer.parseInt(quizIDString);

        StudentQuizModel quizModel = new StudentQuizModel();
        Quiz quiz = quizModel.getCompleteQuiz(quizID);
        if (quiz == null)
        {
            throw halt(400, eng.render(eng.modelAndView(map, "student/invalidQuiz.mustache")));
        }

        if (!quiz.isPublish_status())
        {
            return eng.render(eng.modelAndView(map, "student/unpublishedQuiz.mustache"));
        }

        StudentResultModel studResMod = new StudentResultModel();
        List<Integer> studAnsIds = studResMod.getResultAnswers(resultID);

        for (QuizSection quizSect: quiz.getQuizSections()) {
            for (Answer answer: quizSect.getAnswers()) {
                if(studAnsIds.contains(answer.getAnswer_id()))
                {
                    answer.setIsStudentAnswer(true);
                }

            }
        }
        int score = quiz.calculateScore(studAnsIds);    //TODO: Do this without re-calc


        map.put("quizID", quizID);
        map.put("quiz", quiz);
        map.put("score", score);



        return eng.render(eng.modelAndView(map, "student/getResult.mustache"));

    }
    //Get rid of POST handler?
    public static Object receiveGetResult(Request req, Response res){

        //TODO: Implement
        System.out.println(req.body());
        res.status(200);
        return "end";
    }


}
