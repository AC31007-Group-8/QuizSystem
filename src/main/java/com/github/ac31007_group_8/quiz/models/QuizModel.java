/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ac31007_group_8.quiz.models;

import com.github.ac31007_group_8.quiz.Database;
import java.util.Vector;
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
    
    int quiz_id;
    int staff_id;
    int time_limit;
    String module_id;
    String module_code;
    String title;
    boolean publish_status; 
    
    public QuizModel(){
        quiz_id = 0;
        staff_id = 0;
        time_limit = 0;
        module_id = "";
        module_code = "";
        title = "";
        publish_status = false;
    }
    
    public QuizModel(int quiz_id, int staff_id, int time_limit, String module_id, String module_code, String title, boolean publish_status){
        this.quiz_id = quiz_id;
        this.staff_id = staff_id;
        this.time_limit = time_limit;
        this.module_id = module_id;
        this.module_code = module_code;
        this.title = title;
        this.publish_status = publish_status;
    }
    
    public QuizModel getQuiz(int quiz_id){
        
        String sql = create.select()
                            .from(table("quiz"))
                            .where(field("quiz.quiz_id").equal(quiz_id))
                            .getSQL();
        
        try{
        
            DSLContext create = Database.getJooq();
            Result<Record> result = create.fetch(sql);

            for(Record r : result){ //Iterates through the returned results
                this.quiz_id = r.getValue(quiz.quiz_id);
                staff_id = r.getValue(quiz.staff_id);
                time_limit = r.getValue(quiz.time_limit);
                module_id = r.getValue(quiz.module_id);
                module_code = r.getValue(quiz.module_code);
                title = r.getValue(quiz.title);
                publish_status = r.getValue(quiz.publish_status);

                return this; //Returns current version of the model
            }
        }
        catch(Exception e)
        {
        }
        return null;
    }
    
    public Vector<QuizModel> getQuizAll()
    {      
        Vector<QuizModel> quizzes = new Vector();
        
        /**Creates SQL Statement**/
        String sql = create.select()
                        .from(table("quiz"))
                        .getSQL();
        
        try{
        
            DSLContext create = Database.getJooq(); //Connects to the database
            Result<Record> result = create.fetch(sql);

            for(Record r : result){ //Iterates through the returned results 
                quiz_id = r.getValue(quiz.quiz_id);
                staff_id = r.getValue(quiz.staff_id);
                time_limit = r.getValue(quiz.time_limit);
                module_id = r.getValue(quiz.module_id);
                module_code = r.getValue(quiz.module_code);
                title = r.getValue(quiz.title);
                publish_status = r.getValue(quiz.publish_status);

                quizzes.add(this); //Adds current state of the model to the vector array
            }
        }
        catch(Exception e)
        {
            return null;
        }
        return quizzes;
    }
    
    public Vector<QuizModel> getQuizAllPerStaff(int staff_id)
    {      
        Vector<QuizModel> quizzes = new Vector();
        
        /**Creates SQL Statement**/
        String sql = create.select()
                        .from(table("quiz"))
                        .where(field("quiz.staff_id").equal(staff_id))
                        .getSQL();
        
        try{
        
            DSLContext create = Database.getJooq(); //Connects to the database
            Result<Record> result = create.fetch(sql);

            for(Record r : result){ //Iterates through the returned results 
                quiz_id = r.getValue(quiz.quiz_id);
                this.staff_id = r.getValue(quiz.staff_id);
                time_limit = r.getValue(quiz.time_limit);
                module_id = r.getValue(quiz.module_id);
                module_code = r.getValue(quiz.module_code);
                title = r.getValue(quiz.title);
                publish_status = r.getValue(quiz.publish_status);

                quizzes.add(this); //Adds current state of the model to the vector array
            }
        }
        catch(Exception e)
        {
            return null;
        }
        return quizzes;
    }
    
    public Vector<QuizModel> getQuizAllPerModule(String module_id)
    {      
        Vector<QuizModel> quizzes = new Vector();
        
        /**Creates SQL Statement**/
        String sql = create.select()
                        .from(table("quiz"))
                        .where(field("quiz.module_id").equal(module_id))
                        .getSQL();
        
        try{
        
            DSLContext create = Database.getJooq(); //Connects to the database
            Result<Record> result = create.fetch(sql);

            for(Record r : result){ //Iterates through the returned results 
                quiz_id = r.getValue(quiz.quiz_id);
                staff_id = r.getValue(quiz.staff_id);
                time_limit = r.getValue(quiz.time_limit);
                this.module_id = r.getValue(quiz.module_id);
                module_code = r.getValue(quiz.module_code);
                title = r.getValue(quiz.title);
                publish_status = r.getValue(quiz.publish_status);

                quizzes.add(this); //Adds current state of the model to the vector array
            }
        }
        catch(Exception e)
        {
            return null;
        }
        return quizzes;
    }
    
}
