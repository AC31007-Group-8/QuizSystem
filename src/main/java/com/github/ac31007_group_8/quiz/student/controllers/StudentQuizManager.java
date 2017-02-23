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

        String quizIDString = req.queryParams("quizID");
        if (quizIDString.equals(null) || quizIDString == "")
        {
            //display error page
        }
        int quizID = Integer.parseInt(quizIDString);

        StudentQuizModel quizModel = new StudentQuizModel();
        List<QuizSection> quizSections = quizModel.getQuizSections(quizID);

        HashMap<String, Object> map = new HashMap<>();
        map.put("quizID", quizID);
        map.put("quizSections", quizSections);
        map.put("testKey", "testVal");

        TemplateEngine eng = new MustacheTemplateEngine();
        return eng.render(eng.modelAndView(map, "student/takeQuiz.mustache"));

    }

    public static Object receiveTakeQuiz(Request req, Response res){

        //receive params: quizID, answerIDs, questionIDs
        int quizID = Integer.parseInt(req.queryParams("quizID"));
        int duration = Integer.parseInt(req.queryParams("duration"));

        //retrieve questionIDs in the quiz.
        StudentQuizModel quizModel = new StudentQuizModel();
        List<Question> questions = quizModel.getQuestions(quizID);

        //get ticked answers' answerIDs:
        List<Integer> answerIDs = new ArrayList<Integer>();

        for (Question question: questions) {
            int questionID = question.getQuestionID();
            String[] values = req.queryParamsValues(("question" + Integer.toString(questionID)));

            if (values == null) Logger.getGlobal().info("No answers submitted for question with ID " + questionID);

            for (String value:values) {
                answerIDs.add(Integer.parseInt(value));
                Logger.getGlobal().info("Adding answer with ID: " + value);
            }
        }

        //retrieve quiz from DB
        Quiz quiz = quizModel.getCompleteQuiz(quizID);

        //calculate score
        int score = quiz.calculateScore(answerIDs);

        int studentID = 1;

        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        //ask model to write to database: duration, Score, quiz_id, student_id, date
        //as well as all the answerIDs submitted by user, for this resultID, into the result_to_answer linking table.
        quizModel.writeResult(score, quizID, studentID, sqlDate, duration, answerIDs);

        HashMap<String, Object> map = new HashMap<>();

        TemplateEngine eng = new MustacheTemplateEngine();
        return eng.render(eng.modelAndView(map, "student/endQuiz.mustache"));

    }


}
