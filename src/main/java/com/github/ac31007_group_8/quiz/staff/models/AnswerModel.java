//
///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.github.ac31007_group_8.quiz.staff.models;
//
//import static com.github.ac31007_group_8.quiz.generated.Tables.*;
//
//import com.github.ac31007_group_8.quiz.Database;
//import com.github.ac31007_group_8.quiz.staff.store.Answer;
//import java.util.ArrayList;
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
//        DSLContext create = Database.getJooq(); //Connects to the database
//        String sql = create.select() //Selects all fields if empty
//                            .from(ANSWER)
//                            .where(ANSWER.QUESTION_ID.equal(question_id))
//                            .getSQL();
//        
//        try{
//        
//            Result<Record> result = create.fetch(sql);
//
//            for(Record r : result){ //Iterates through the returned results     
//                
//                Answer answer = new Answer(r.get(ANSWER.ANSWER_ID), r.get(ANSWER.QUESTION_ID), r.get(ANSWER.ANSWER_), r.get(ANSWER.IS_CORRECT)!=0); //!=0 Converts a single bit to boolean
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
//    public ArrayList<Answer> getAnswerAll()
//    {      
//        ArrayList<Answer> answers = new ArrayList();
//        DSLContext create = Database.getJooq(); //Connects to the database
//        String sql = create.select(ANSWER.ANSWER_ID, ANSWER.QUESTION_ID, ANSWER.ANSWER_, ANSWER.IS_CORRECT) //Selects field specified (could've been empty)
//                            .from(ANSWER)
//                            .getSQL();
//        
//        try{
//        
//            Result<Record> result = create.fetch(sql);
//
//            for(Record r : result){ //Iterates through the returned results 
//                
//                Answer answer = new Answer(r.get(ANSWER.ANSWER_ID), r.get(ANSWER.QUESTION_ID), r.get(ANSWER.ANSWER_), r.get(ANSWER.IS_CORRECT)!=0); //!=0 Converts a single bit to boolean
//
//                answers.add(answer); //Adds current state of the model to the vector array
//            }
//        }
//        catch(Exception e)
//        {
//            return null;
//        }
//        return answers; 
//    }
//    
//    public boolean addAnswer(int answer_id, int question_id, String answer, boolean correct) 
//    {   
//        DSLContext create = Database.getJooq(); //Connects to the database
//        
//        try{
//            create.insertInto(ANSWER, ANSWER.ANSWER_ID, ANSWER.QUESTION_ID, ANSWER.ANSWER_, ANSWER.IS_CORRECT)
//                    .values(answer_id, question_id, answer, (byte)(correct?1:0)) //(byte)(x?1:0) Converts boolean x to byte
//                    .execute();
//        }
//        catch(Exception e)
//        {
//            return false;
//        }
//        
//        return true;  
//    }
//}
//
