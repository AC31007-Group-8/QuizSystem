///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.github.ac31007_group_8.quiz.staff.models;
//
//import com.github.ac31007_group_8.quiz.Database;
//import com.github.ac31007_group_8.quiz.staff.store.Question;
//import java.util.Vector;
//import org.jooq.DSLContext;
//import org.jooq.Record;
//import org.jooq.Result;
//import static org.jooq.impl.DSL.field;
//import static org.jooq.impl.DSL.table;
//
///**
// *
// * @author Erik Jeny
// */
//public class QuestionModel {
//
//    public QuestionModel(){   
//    }
//    
//    public Question getQuestion(int quiz_id){
//        
//        DSLContext create = Database.getJooq(); //Connects to the database
//        
//        String sql = create.select(field("question.question_id"), field("question.quiz_id"), field("question.question"), field("question.explanation"))
//                            .from(table("question"))
//                            .where(field("question.quiz_id").equal(quiz_id))
//                            .getSQL();
//        
//        try{
//            Result<Record> result = create.fetch(sql);
//
//            for(Record r : result){ //Iterates through the returned results
//                Question question = new Question(r.getValue(question.question_id), r.getValue(question.quiz_id), r.getValue(question.question), r.getValue(question.explanation));
//
//                return question; //Returns current version of the model
//            }
//        }
//        catch(Exception e)
//        {
//        }
//        return null;
//    }
//    
//    public Vector<Question> getAnswerAll()
//    {      
//        Vector<Question> questions = new Vector();
//        DSLContext create = Database.getJooq();
//        
//        String sql = create.select(field("question.question_id"), field("question.quiz_id"), field("question.question"), field("question.explanation"))
//                            .from(table("question"))
//                            .getSQL();
//        
//        try{
//        
//            Result<Record> result = create.fetch(sql);
//
//            for(Record r : result){ //Iterates through the returned results 
//                Question question = new Question(r.getValue(question.question_id), r.getValue(question.quiz_id), r.getValue(question.question), r.getValue(question.explanation));
//
//                questions.add(question); //Adds current state of the model to the vector array
//            }
//        }
//        catch(Exception e)
//        {
//            return null;
//        }
//        return questions;
//    }
//    
//    public boolean addQuestion(int question_id, int quiz_id, String question, String explanation) 
//    {   
//        DSLContext create = Database.getJooq();
//        
//        try{
//            create.insertInto(question, question.question_id, question.quiz_id, question.question, question.explanation)
//                                .values(question_id, quiz_id, question, explanation)
//                                .execute();
//        }
//        catch(Exception e)
//        {
//            return false;
//        }
//        
//        return true;  
//    }
//    
//    
//    
//}
