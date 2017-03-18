package com.github.ac31007_group_8.quiz.student.controllers;

import com.github.ac31007_group_8.quiz.common.ParameterManager;
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

        //Sat up template engine
        TemplateEngine eng = new MustacheTemplateEngine();
        HashMap<String, Object> map = new HashMap<>();

        //Validate quizID - is valid input provided?
        String quizIDString = req.queryParams("quizID");
        if (quizIDString == (null) || quizIDString.equals(""))
        {
            //display error page
            throw halt(400, eng.render(eng.modelAndView(map, "invalidQuiz.mustache")));
        }
        int quizID = Integer.parseInt(quizIDString);

        //Validate quizID - does it exist?
        StudentQuizModel quizModel = new StudentQuizModel();
        Quiz quiz = quizModel.getCompleteQuiz(quizID);
        if (quiz == null)
        {
            throw halt(400, eng.render(eng.modelAndView(map, "invalidQuiz.mustache")));
        }

        //Don't show quiz if unpublished
        if (!quiz.isPublish_status())
        {
            return eng.render(eng.modelAndView(map, "unpublishedQuiz.mustache"));
        }

      
        List<Question> allQuestions = quiz.getQuestions();
        map.put("quizID", quizID);
        map.put("allQuestions", allQuestions);

        return eng.render(eng.modelAndView(map, "takeQuiz.mustache"));

    }

    
    
    
    
    public static Object receiveTakeQuiz(Request req, Response res){

        //receive params: quizID, answerIDs, questionIDs
        int quizID = Integer.parseInt(req.queryParams("quizID"));
        int duration = Integer.parseInt(req.queryParams("duration"));

        //retrieve questionIDs in the quiz.
        StudentQuizModel quizModel = new StudentQuizModel();
        List<Question> questions = quizModel.getQuestions(quizID);

        //get ticked answers' answerIDs:
        List<Integer> answerIDs = new ArrayList<>();

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
        int score = calculateScore(answerIDs, quiz);

        //placeholder value until student logins are implemented in sprint 2
        //TODO: Replace with the actual studentID.
        int studentID = 1;

        
        java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());

        //ask model to write to database: duration, Score, quiz_id, student_id, date
        //as well as all the answerIDs submitted by user, for this resultID, into the result_to_answer linking table.
        quizModel.writeResult(score, quizID, studentID, sqlDate, duration, answerIDs);

        HashMap<String, Object> map = ParameterManager.getAllParameters(req);

        TemplateEngine eng = new MustacheTemplateEngine();
        return eng.render(eng.modelAndView(map, "endQuiz.mustache"));

    }
    
    
    
    
    public static int calculateScore(List<Integer> answerIDs, Quiz quiz){

        //calculate Score (type: INT) as: questions answered correctly out of totalquestions, as a percentage
        //count total number of questions in quiz
        //count number of questions answered correctly
        //calculate score

        int numberOfQuestions = 0;
        int numberOfCorrectlyAnsweredQuestions = 0;

        if (quiz.getQuestions()==null)
        {
            Logger.getGlobal().info("Error: cannot mark quiz - Questions are null.");
            return 0;
        }

        for (Question question : quiz.getQuestions()) {
            numberOfQuestions++;
            boolean isCorrect = true;
            for (Answer answer : question.getAnswers()) {
                if (answer.isCorrect()) //we'd like to find it in user's list of answers
                {
                    if (!isIDInList(answerIDs, answer.getAnswer_id())) isCorrect = false;
                }
                else { //we'd like NOT to find it in the user's list of answers
                    if (isIDInList(answerIDs, answer.getAnswer_id())) isCorrect = false;
                }
            }
            if (isCorrect) numberOfCorrectlyAnsweredQuestions++;
        }

        Logger.getGlobal().info("Number of Questions: " + numberOfQuestions + " Number Correctly Answered: " + numberOfCorrectlyAnsweredQuestions);

        return getPercentage(numberOfCorrectlyAnsweredQuestions, numberOfQuestions);

    }

    private static int getPercentage(int small, int large)
    {
        if (large == 0){
            Logger.getGlobal().info("Error: tried to divide by zero while calculating percentage score");
            return 0;
        }

        return Math.round((float)small/(float)large * 100f);
    }

    private static boolean isIDInList(List<Integer> ids, int idToFind){

        for (Integer number: ids) {
            if (number == idToFind) {
                return true;
            }
        }

        return false;

    }
    
    
    


}
