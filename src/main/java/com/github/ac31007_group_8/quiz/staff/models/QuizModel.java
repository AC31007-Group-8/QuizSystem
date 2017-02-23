/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ac31007_group_8.quiz.staff.models;

import com.github.ac31007_group_8.quiz.Database;
import static com.github.ac31007_group_8.quiz.generated.Tables.*;
import com.github.ac31007_group_8.quiz.staff.store.Quiz;
import java.sql.SQLException;
import java.util.ArrayList;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

/**
 *
 * @author Erik Jeny
 */
public class QuizModel {
    
    public QuizModel(){
    }
    
    public Quiz getQuiz(int quiz_id){
        
        DSLContext create = Database.getJooq(); //Connects to the database
        
        String sql = create.select()
                            .from(QUIZ)
                            .where(QUIZ.QUIZ_ID.equal(quiz_id))
                            .getSQL();
        
        try{
        
            Result<Record> result = create.fetch(sql);

            for(Record r : result){ //Iterates through the returned results
                Quiz quiz = new Quiz(r.getValue(QUIZ.QUIZ_ID), r.getValue(QUIZ.STAFF_ID), r.getValue(QUIZ.TIME_LIMIT), r.getValue(QUIZ.MODULE_ID), r.getValue(QUIZ.TITLE), r.getValue(QUIZ.PUBLISH_STATUS)!=0); 

                return quiz; //Returns current version of the model
            }
        }
        catch(Exception e)
        {
        }
        return null;
    }
    
    public ArrayList<Quiz> getQuizAll()
    {      
        ArrayList<Quiz> quizzes = new ArrayList();
        DSLContext create = Database.getJooq(); //Connects to the database
        
        /**Creates SQL Statement**/
        String sql = create.select()
                        .from(QUIZ)
                        .getSQL();
        
        try{
        
            Result<Record> result = create.fetch(sql);

            for(Record r : result){ //Iterates through the returned results 
                Quiz quiz = new Quiz(r.getValue(QUIZ.QUIZ_ID), r.getValue(QUIZ.STAFF_ID), r.getValue(QUIZ.TIME_LIMIT), r.getValue(QUIZ.MODULE_ID), r.getValue(QUIZ.TITLE), r.getValue(QUIZ.PUBLISH_STATUS)!=0);

                quizzes.add(quiz); //Adds current state of the model to the vector array
            }
        }
        catch(Exception e)
        {
            return null;
        }
        return quizzes;
    }
    
    public ArrayList<Quiz> getQuizAllPerStaff(int staff_id)
    {      
        ArrayList<Quiz> quizzes = new ArrayList();
        DSLContext create = Database.getJooq(); //Connects to the database
        
        /**Creates SQL Statement**/
        String sql = create.select()
                        .from(QUIZ)
                        .where(QUIZ.STAFF_ID.equal(staff_id))
                        .getSQL();
        
        try{
        
            Result<Record> result = create.fetch(sql);

            for(Record r : result){ //Iterates through the returned results 
                Quiz quiz = new Quiz(r.getValue(QUIZ.QUIZ_ID), r.getValue(QUIZ.STAFF_ID), r.getValue(QUIZ.TIME_LIMIT), r.getValue(QUIZ.MODULE_ID), r.getValue(QUIZ.TITLE), r.getValue(QUIZ.PUBLISH_STATUS)!=0);

                quizzes.add(quiz); //Adds current state of the model to the vector array
            }
        }
        catch(Exception e)
        {
            return null;
        }
        return quizzes;
    }
    
    public ArrayList<Quiz> getQuizAllPerModule(String module_id)
    {      
        ArrayList<Quiz> quizzes = new ArrayList();
        DSLContext create = Database.getJooq(); //Connects to the database
        
        /**Creates SQL Statement**/
        String sql = create.select()
                        .from(QUIZ)
                        .where(QUIZ.MODULE_ID.equal(module_id))
                        .getSQL();
        
        try{
            Result<Record> result = create.fetch(sql);

            for(Record r : result){ //Iterates through the returned results 
                Quiz quiz = new Quiz(r.getValue(QUIZ.QUIZ_ID), r.getValue(QUIZ.STAFF_ID), r.getValue(QUIZ.TIME_LIMIT), r.getValue(QUIZ.MODULE_ID), r.getValue(QUIZ.TITLE), r.getValue(QUIZ.PUBLISH_STATUS)!=0);

                quizzes.add(quiz); //Adds current state of the model to the vector array
            }
        }
        catch(Exception e)
        {
            return null;
        }
        return quizzes;
    }
    
    public boolean addQuiz(int quiz_id, int staff_id, int time_limit, String module_id, String title, boolean publish_status) 
    {   
        DSLContext create = Database.getJooq();
        
        int sql = 5; //Get number of entries for table from DB to index properly                    
        
        try{
            create.insertInto(QUIZ, QUIZ.QUIZ_ID, QUIZ.STAFF_ID, QUIZ.TIME_LIMIT, QUIZ.MODULE_ID, QUIZ.TITLE, QUIZ.PUBLISH_STATUS)
                                .values(sql++, staff_id, time_limit,module_id, title, (byte)(publish_status?1:0))
                                .execute();
        }
        catch(Exception e)
        {
            return false;
        }
        
        return true;  
    }
    
public ArrayList<String> getModuleList(DSLContext dslCont) throws SQLException{
        String sql = dslCont.select(field("module_id"))
                        .from(table("Module"))
                        .getSQL();
        
        Result<Record> result = dslCont.fetch(sql);
        ArrayList<String> allModules = new ArrayList<>();
        for(Record r : result){
                
                allModules.add((String)r.getValue(field("module_id")));
                
        }
        return allModules;
    }
}
