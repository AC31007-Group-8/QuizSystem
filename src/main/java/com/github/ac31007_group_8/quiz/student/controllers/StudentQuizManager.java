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
 * @author Can G, Allan M
 */
public class StudentQuizManager {

    public StudentQuizManager(){

    }

    @Init
    public static void init() {
        get("/student/takeQuiz", StudentQuizManager::serveTakeQuiz);
        post("/student/takeQuiz", "application/json", StudentQuizManager::receiveTakeQuiz);
    }

    public static Object serveTakeQuiz(Request req, Response res){

        int quizID = Integer.parseInt(req.queryParams("quizID"));

        StudentQuizModel quizModel = new StudentQuizModel();
        List<Pair<Question, List<Answer>>> questionSets = quizModel.getQuestionSets(quizID);

        HashMap<String, Object> map = new HashMap<>();
        map.put("quizID", quizID);
        map.put("questionSets", questionSets);
        map.put("testKey", "testVal");

        TemplateEngine eng = new MustacheTemplateEngine();
        return eng.render(eng.modelAndView(map, "student/takeQuiz.mustache"));

    }

    public static Object receiveTakeQuiz(Request req, Response res){

        //receive params

        //calculate Score as: questions answered correctly out of totalquestions, as a percentage

        //ask model to write to database: duration, Score, quiz_id, student_id, date
        //as well as all the answerIDs submitted by user, for this resultID, into the result_to_answer linking table.

    return null;

    }


}
