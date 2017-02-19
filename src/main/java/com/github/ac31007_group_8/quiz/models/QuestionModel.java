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
public class QuestionModel {
    
    int question_id;
    int quiz_id;
    String question;
    String explanation;
    
    public QuestionModel(){
        question_id = 0;
        quiz_id = 0;
        question = "";
        explanation = "";
    }
    
    public QuestionModel(int question_id, int quiz_id, String question, String explanation){
        this.question_id = question_id;
        this.quiz_id = quiz_id;
        this.question = question;
        this.explanation = explanation;
    }
    
    public QuestionModel getQuestion(int quiz_id){
        
        String sql = create.select(field("question.question_id"), field("question.quiz_id"), field("question.question"), field("question.explanation"))
                            .from(table("question"))
                            .where(field("question.quiz_id").equal(quiz_id))
                            .getSQL();
        
        try{
        
            DSLContext create = Database.getJooq();
            Result<Record> result = create.fetch(sql);

            for(Record r : result){ //Iterates through the returned results
                question_id = r.getValue(question.question_id);
                this.quiz_id = r.getValue(question.quiz_id);
                question = r.getValue(question.question);
                explanation = r.getValue(question.explanation);

                return this; //Returns current version of the model
            }
        }
        catch(Exception e)
        {
        }
        return null;
    }
    
    public Vector<QuestionModel> getAnswerAll()
    {      
        Vector<QuestionModel> questions = new Vector();
        
        String sql = create.select(field("question.question_id"), field("question.quiz_id"), field("question.question"), field("question.explanation"))
                            .from(table("question"))
                            .getSQL();
        
        try{
        
            DSLContext create = Database.getJooq();
            Result<Record> result = create.fetch(sql);

            for(Record r : result){ //Iterates through the returned results 
                question_id = r.getValue(question.question_id);
                this.quiz_id = r.getValue(question.quiz_id);
                question = r.getValue(question.question);
                explanation = r.getValue(question.explanation);

                questions.add(this); //Adds current state of the model to the vector array
            }
        }
        catch(Exception e)
        {
            return null;
        }
        return questions;
    }
    
    
    
}
