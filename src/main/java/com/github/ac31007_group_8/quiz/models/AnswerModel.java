/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ac31007_group_8.quiz.models;

import static org.jooq.impl.DSL.*;

import com.github.ac31007_group_8.quiz.Database;
import java.util.Vector;

import org.jooq.DSLContext;
import org.jooq.*;
/**
 *
 * @author Erik Jeny
 */
public class AnswerModel {
    
    int answer_id;
    int question_id;
    String answer;
    boolean correct;
    
    public AnswerModel(){
        answer_id = 0;
        question_id = 0;
        answer = "";
        correct = true;
    }  
    
    public AnswerModel(int answer_id, int question_id, String answer, boolean correct){
        this.answer_id = answer_id;
        this.question_id = question_id;
        this.answer = answer;
        this.correct = correct;
    } 
    
    public AnswerModel getAnswer(int question_id)
    {       
        DSLContext create = Database.getJooq();
        
        String sql = create.select(field("answer.answer_id"), field("answer.question_id"), field("answer.answer"), field("answer.is_correct"))
                            .from(table("answer"))
                            .where(field("answer.question_id").equal(question_id))
                            .getSQL();
        
        try{
        
            Result<Record> result = create.fetch(sql);

            for(Record r : result){ //Iterates through the returned results
                answer_id = r.getValue(answer.answer_id);
                this.question_id = r.getValue(answer.question_id);
                answer = r.getValue(answer.answer);
                correct = r.getValue(answer.is_correct);

                return this; //Returns current version of the model
            }
        }
        catch(Exception e)
        {
        }
        return null;
    }
    
    public Vector<AnswerModel> getAnswerAll()
    {      
        Vector<AnswerModel> answers = new Vector();
        DSLContext create = Database.getJooq();
        
        String sql = create.select(field("answer.answer_id"), field("answer.question_id"), field("answer.answer"), field("answer.is_correct"))
                            .from(table("answer"))
                            .getSQL();
        
        try{
        
            Result<Record> result = create.fetch(sql);

            for(Record r : result){ //Iterates through the returned results 
                answer_id = r.getValue(answer.answer_id);
                question_id = r.getValue(answer.question_id);
                answer = r.getValue(answer.answer);
                correct = r.getValue(answer.is_correct);

                answers.add(this); //Adds current state of the model to the vector array
            }
        }
        catch(Exception e)
        {
            return null;
        }
        return answers;
    }
    
    public boolean addAnswer(int answer_id, int question_id, String answer, boolean correct) 
    {   
        DSLContext create = Database.getJooq();
        
        try{
            create.insertInto(answer, answer.answer_id, answer.question_id, answer.answer, answer.is_correct)
                                .values(answer_id, question_id, answer, correct)
                                .execute();
        }
        catch(Exception e)
        {
            return false;
        }
        
        return true;  
    }
}
