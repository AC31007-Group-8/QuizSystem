///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.github.ac31007_group_8.quiz;
//
//import com.github.ac31007_group_8.quiz.models.AnswerModel;
//import com.github.ac31007_group_8.quiz.models.QuestionModel;
//import com.github.ac31007_group_8.quiz.models.QuizModel;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import java.io.IOException;
//import java.io.FileReader;
//
//import com.google.gson.*;
//import com.google.gson.stream.*;
//import java.util.Vector;
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
//        Vector<QuestionModel> questions = null;
//        Vector<AnswerModel> answers = null;
//        
//        try{
//            
//            JsonReader reader = new JsonReader(new FileReader("insert.your.url.here")); //Gets JSON from specified url/path
//            
//            reader.beginObject();
//            
//            while(reader.hasNext()){
//                String label = reader.nextName();
//                
//                if(label.equals("timeLimit"))
//                {
//                    timeLimit=reader.nextInt();
//                }
//                else if(label.equals("moduleCode"))
//                {
//                    moduleCode=reader.nextString();
//                }
//                else if(label.equals("title"))
//                {
//                    title=reader.nextString();
//                }
//                else if(label.equals("questions"))
//                {
//                    reader.beginArray();
//                    
//                    while(reader.hasNext()){
//                        
//                        if(label.equals("questionText"))
//                        {
//                            questionText=reader.nextString();
//                        }
//                        else if(label.equals("explanation"))
//                        {
//                            questionExplanation=reader.nextString();
//                        }
//                        else if(label.equals("options"))
//                        {
//                            reader.beginArray();
//                    
//                            while(reader.hasNext()){
//                                
//                                if(label.equals("option"))
//                                {
//                                    answerText=reader.nextString();
//                                }
//                                else if(label.equals("correct"))
//                                {
//                                    answerIsCorrect=reader.nextBoolean();
//                                }
//                            }
//                        }
//                        
//                    }
//                }
//            }
//        }
//        catch(IOException e) {
//            e.printStackTrace();
//        }
//    }
//    
//}
