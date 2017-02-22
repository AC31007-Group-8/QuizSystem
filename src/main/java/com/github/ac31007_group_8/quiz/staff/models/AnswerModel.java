///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.github.ac31007_group_8.quiz.staff.models;
//
//import static org.jooq.impl.DSL.*;
//
//import com.github.ac31007_group_8.quiz.Database;
//import com.github.ac31007_group_8.quiz.staff.store.Answer;
//import java.util.Vector;
//
//import org.jooq.DSLContext;
//import org.jooq.*;
///**
// *
// * @author Erik Jeny
// */
//public class AnswerModel {
//    
//    public AnswerModel(){
//    }  
//    
//    public Answer getAnswer(int question_id)
//    {       
//        DSLContext create = Database.getJooq();
//        
//        String sql = create.select(field("answer.answer_id"), field("answer.question_id"), field("answer.answer"), field("answer.is_correct"))
//                            .from(table("answer"))
//                            .where(field("answer.question_id").equal(question_id))
//                            .getSQL();
//        
//        try{
//        
//            Result<Record> result = create.fetch(sql);
//
//            for(Record r : result){ //Iterates through the returned results
//                
//               // Answer answer = new Answer(r.getValue(answer.answer_id), r.getValue(answer.question_id), r.getValue(answer.answer), r.getValue(answer.is_correct));
//
//                
//                Answer answer = new Answer(r.getValue(answer.answer_id), r.getValue(answer.question_id), r.getValue(answer.answer), r.getValue(answer.is_correct));
//                
//                return answer; //Returns current version of the model
//            }
//        }
//        catch(Exception e)
//        {
//        }
//        return null;
//    }
//    
//    public Vector<Answer> getAnswerAll()
//    {      
//        Vector<Answer> answers = new Vector();
//        DSLContext create = Database.getJooq();
//        
//        String sql = create.select(field("answer.answer_id"), field("answer.question_id"), field("answer.answer"), field("answer.is_correct"))
//                            .from(table("answer"))
//                            .getSQL();
//        
//        try{
//        
//            Result<Record> result = create.fetch(sql);
//
//            for(Record r : result){ //Iterates through the returned results 
//                
//                Answer answer = new Answer(r.getValue(answer.answer_id), r.getValue(answer.question_id), r.getValue(answer.answer), r.getValue(answer.is_correct));
//
//                answers.add(answer); //Adds current state of the model to the vector array
//            }
//        }
//        catch(Exception e)
//        {
//            return null;
//        }
//        return answer;
//    }
//    
//    
//    
//    
//    
//    public boolean addAnswer(int answer_id, int question_id, String answer, boolean correct) 
//    {   
//        DSLContext create = Database.getJooq();
//        
//        try{
//            create.insertInto(answer, answer.answer_id, answer.question_id, answer.answer, answer.is_correct)
//                                .values(answer_id, question_id, answer, correct)
//                                .execute();
//        }
//        catch(Exception e)
//        {
//            return false;
//        }
//        
//        return true;  
//    }
//}
