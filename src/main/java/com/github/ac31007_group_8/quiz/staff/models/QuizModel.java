///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.github.ac31007_group_8.quiz.staff.models;
//
//import com.github.ac31007_group_8.quiz.Database;
//import com.github.ac31007_group_8.quiz.staff.store.Quiz;
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
//public class QuizModel {
//    
//    public QuizModel(){
//    }
//    
//    public Quiz getQuiz(int quiz_id){
//        
//        DSLContext create = Database.getJooq(); //Connects to the database
//        
//        String sql = create.select()
//                            .from(table("quiz"))
//                            .where(field("quiz.quiz_id").equal(quiz_id))
//                            .getSQL();
//        
//        try{
//        
//            Result<Record> result = create.fetch(sql);
//
//            for(Record r : result){ //Iterates through the returned results
//                Quiz quiz = new Quiz(r.getValue(quiz.quiz_id), r.getValue(quiz.staff_id), r.getValue(quiz.time_limit), r.getValue(quiz.module_id), r.getValue(quiz.title), r.getValue(quiz.publish_status)); 
//
//                return quiz; //Returns current version of the model
//            }
//        }
//        catch(Exception e)
//        {
//        }
//        return null;
//    }
//    
//    public Vector<Quiz> getQuizAll()
//    {      
//        Vector<Quiz> quizzes = new Vector();
//        DSLContext create = Database.getJooq(); //Connects to the database
//        
//        /**Creates SQL Statement**/
//        String sql = create.select()
//                        .from(table("quiz"))
//                        .getSQL();
//        
//        try{
//        
//            Result<Record> result = create.fetch(sql);
//
//            for(Record r : result){ //Iterates through the returned results 
//                Quiz quiz = new Quiz(r.getValue(quiz.quiz_id), r.getValue(quiz.staff_id), r.getValue(quiz.time_limit), r.getValue(quiz.module_id), r.getValue(quiz.title), r.getValue(quiz.publish_status));
//
//                quizzes.add(quiz); //Adds current state of the model to the vector array
//            }
//        }
//        catch(Exception e)
//        {
//            return null;
//        }
//        return quizzes;
//    }
//    
//    public Vector<Quiz> getQuizAllPerStaff(int staff_id)
//    {      
//        Vector<Quiz> quizzes = new Vector();
//        DSLContext create = Database.getJooq(); //Connects to the database
//        
//        /**Creates SQL Statement**/
//        String sql = create.select()
//                        .from(table("quiz"))
//                        .where(field("quiz.staff_id").equal(staff_id))
//                        .getSQL();
//        
//        try{
//        
//            Result<Record> result = create.fetch(sql);
//
//            for(Record r : result){ //Iterates through the returned results 
//                Quiz quiz = new Quiz(r.getValue(quiz.quiz_id), r.getValue(quiz.staff_id), r.getValue(quiz.time_limit), r.getValue(quiz.module_id), r.getValue(quiz.title), r.getValue(quiz.publish_status));
//
//                quizzes.add(quiz); //Adds current state of the model to the vector array
//            }
//        }
//        catch(Exception e)
//        {
//            return null;
//        }
//        return quizzes;
//    }
//    
//    public Vector<Quiz> getQuizAllPerModule(String module_code)
//    {      
//        Vector<Quiz> quizzes = new Vector();
//        DSLContext create = Database.getJooq(); //Connects to the database
//        
//        /**Creates SQL Statement**/
//        String sql = create.select()
//                        .from(table("quiz"))
//                        .where(field("quiz.module_code").equal(module_code))
//                        .getSQL();
//        
//        try{
//            Result<Record> result = create.fetch(sql);
//
//            for(Record r : result){ //Iterates through the returned results 
//                Quiz quiz = new Quiz(r.getValue(quiz.quiz_id), r.getValue(quiz.staff_id), r.getValue(quiz.time_limit), r.getValue(quiz.module_id), r.getValue(quiz.title), r.getValue(quiz.publish_status));
//
//                quizzes.add(quiz); //Adds current state of the model to the vector array
//            }
//        }
//        catch(Exception e)
//        {
//            return null;
//        }
//        return quizzes;
//    }
//    
//    public boolean addQuiz(int quiz_id, int staff_id, int time_limit, String module_id, String title, boolean publish_status) 
//    {   
//        DSLContext create = Database.getJooq();
//        
//        int sql = 5; //Get number of entries for table from DB to index properly                    
//        
//        try{
//            create.insertInto(quiz, quiz.quiz_id, quiz.staff_id, quiz.time_limit, quiz.module_id, quiz.title, quiz.publish_status)
//                                .values(sql++, staff_id, time_limit,module_id, title, publish_status)
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
//}
