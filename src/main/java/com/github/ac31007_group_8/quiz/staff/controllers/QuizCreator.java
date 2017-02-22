///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.github.ac31007_group_8.quiz;
//
//import com.github.ac31007_group_8.quiz.staff.models.AnswerModel;
//import com.github.ac31007_group_8.quiz.staff.models.QuestionModel;
//import com.github.ac31007_group_8.quiz.staff.models.QuizModel;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import java.io.IOException;
//import java.io.FileReader;
//
//import com.google.gson.*;
//import com.google.gson.stream.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//
///**
// *
// * @author Temp
// */
//public class QuizCreator {
//    
//    private static final Logger LOGGER = LoggerFactory.getLogger(QuizCreator.class);
//
//    private QuizCreator() {} // Static only
//    
//    private void parseQuiz()
//    {
//        int timeLimit = 60;
//        String moduleCode = "";
//        String title = "";
//        String questionText = "";
//        String questionExplanation = "";
//        String answerText = "";
//        boolean answerIsCorrect = false;
//        
//        QuizModel quiz = null;
//        ArrayList<QuestionModel> questions = null;
//        ArrayList<AnswerModel> answers = null;
//        
//        try{
//            
//           
//            Gson gson = new GsonBuilder().create();
//            String quizJson = gson.toJson(quizSets);
//
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("quizID", quizStr);
//            map.put("questionSets", questionSets);
//            map.put("testKey", "testVal");
//            
//        }
//        catch(IOException e) {
//            e.printStackTrace();
//        }
//    }
//    
//}
