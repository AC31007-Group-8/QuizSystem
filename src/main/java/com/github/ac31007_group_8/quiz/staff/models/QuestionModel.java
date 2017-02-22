/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ac31007_group_8.quiz.staff.models;

import com.github.ac31007_group_8.quiz.Database;
import static com.github.ac31007_group_8.quiz.generated.Tables.*;
import com.github.ac31007_group_8.quiz.staff.store.Question;
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
public class QuestionModel {

    public QuestionModel(){   
    }
    
    public Question getQuestion(int quiz_id){
        
        DSLContext create = Database.getJooq(); //Connects to the database
        
        String sql = create.select(QUESTION.QUESTION_ID, QUESTION.QUIZ_ID, QUESTION.QUESTION_, QUESTION.EXPLANATION)
                            .from(QUESTION)
                            .where(QUESTION.QUIZ_ID.equal(quiz_id))
                            .getSQL();
        
        try{
            Result<Record> result = create.fetch(sql);

            for(Record r : result){ //Iterates through the returned results
                Question question = new Question(r.getValue(QUESTION.QUESTION_ID), r.getValue(QUESTION.QUIZ_ID), r.getValue(QUESTION.QUESTION_), r.getValue(QUESTION.EXPLANATION));

                return question; //Returns current version of the model
            }
        }
        catch(Exception e)
        {
        }
        return null;
    }
    
    public Vector<Question> getAnswerAll()
    {      
        Vector<Question> questions = new Vector();
        DSLContext create = Database.getJooq();
        
        String sql = create.select(QUESTION.QUESTION_ID, QUESTION.QUIZ_ID, QUESTION.QUESTION_, QUESTION.EXPLANATION)
                            .from(QUESTION)
                            .getSQL();
        
        try{
        
            Result<Record> result = create.fetch(sql);

            for(Record r : result){ //Iterates through the returned results 
                Question question = new Question(r.getValue(QUESTION.QUESTION_ID), r.getValue(QUESTION.QUIZ_ID), r.getValue(QUESTION.QUESTION_), r.getValue(QUESTION.EXPLANATION));

                questions.add(question); //Adds current state of the model to the vector array
            }
        }
        catch(Exception e)
        {
            return null;
        }
        return questions;
    }
    
    public boolean addQuestion(int question_id, int quiz_id, String question, String explanation) 
    {   
        DSLContext create = Database.getJooq();
        
        try{
            create.insertInto(QUESTION, QUESTION.QUESTION_ID, QUESTION.QUIZ_ID, QUESTION.QUESTION_, QUESTION.EXPLANATION)
                                .values(question_id, quiz_id, question, explanation)
                                .execute();
        }
        catch(Exception e)
        {
            return false;
        }
        
        return true;  
    }
    
    
    
}
