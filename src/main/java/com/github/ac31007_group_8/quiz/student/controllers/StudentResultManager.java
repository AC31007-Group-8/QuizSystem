package com.github.ac31007_group_8.quiz.student.controllers;

import com.github.ac31007_group_8.quiz.common.ParameterManager;
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

/**
 * @author Kerr, Allan (adapted from Can and Allan)
 */
public class StudentResultManager {

    public StudentResultManager(){

    }

    @Init
    public static void init() {
        get("/student/getResult", StudentResultManager::serveGetResult);
    }

    public static Object serveGetResult(Request req, Response res){

        TemplateEngine eng = new MustacheTemplateEngine();
        HashMap<String, Object> map = ParameterManager.getAllParameters(req);

        String quizIDString = req.queryParams("quizID");//don't need this
        int resultID = Integer.parseInt(req.queryParams("resultID"));
        if (quizIDString == (null) || quizIDString.equals(""))
        {
            //display error page
            throw halt(400, eng.render(eng.modelAndView(map, "invalidQuiz.mustache")));
        }
        int quizID = Integer.parseInt(quizIDString);

        StudentQuizModel quizModel = new StudentQuizModel();
        Quiz quiz = quizModel.getCompleteQuiz(quizID);
        if (quiz == null)
        {
            throw halt(400, eng.render(eng.modelAndView(map, "invalidQuiz.mustache")));
        }

        if (!quiz.isPublish_status())
        {
            return eng.render(eng.modelAndView(map, "unpublishedQuiz.mustache"));
        }

        StudentResultModel studResMod = new StudentResultModel();
        List<Integer> studAnsIds = studResMod.getResultAnswers(resultID);

        for (Question nextQuestion: quiz.getQuestions()) {
            for (Answer answer: nextQuestion.getAnswers()) {
                if(studAnsIds.contains(answer.getAnswer_id()))
                {
                    answer.setIsStudentAnswer(true);
                }

            }
        } 
        //TODO: Do this without re-calc
        int score = StudentQuizManager.calculateScore(studAnsIds,quiz);//I think we need to merge this class into StudentQuizManager (but can keep separate files for now)  


        map.put("quizID", quizID);
        map.put("quiz", quiz);
        map.put("score", score);



        return eng.render(eng.modelAndView(map, "getResult.mustache"));

    }
 


}
