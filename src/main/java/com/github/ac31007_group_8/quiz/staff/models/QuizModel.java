/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ac31007_group_8.quiz.staff.models;

import com.github.ac31007_group_8.quiz.Database;
import static com.github.ac31007_group_8.quiz.generated.Tables.*;
import  com.github.ac31007_group_8.quiz.generated.tables.records.*;
import com.github.ac31007_group_8.quiz.staff.store.Question;
import com.github.ac31007_group_8.quiz.staff.store.Quiz;
import com.github.ac31007_group_8.quiz.staff.store.Answer;
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
            e.printStackTrace();
           
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
    
    
    
    public void saveQuiz(Quiz quizToSave, DSLContext create) throws SQLException{
        
            //dafuck ??? 865ms to save this!!?
        
            //store quiz
            QuizRecord quizR = create.newRecord(QUIZ, quizToSave);
            quizR.store();
            Integer quizId = quizR.getQuizId();
           
          
            long start = System.currentTimeMillis();
            quizToSave.getQuestions().stream().map((nextQuest) -> {
                //store question
                QuestionRecord questionR = create.newRecord(QUESTION, nextQuest);
                questionR.setQuizId(quizId);
                questionR.store();
                Integer questionId = questionR.getQuestionId();
                //store answers
                ArrayList<AnswerRecord> answers = new ArrayList<>();
                for (Answer a:nextQuest.getAnswers()){
                    a.setQuestion_id(questionId);
                    AnswerRecord answrR = create.newRecord(ANSWER, a);
                    answers.add(answrR);
                }
                return answers;
            }).forEach((answers) -> {
                System.out.println( System.currentTimeMillis()-start);
                create.batchInsert(answers).execute();
                 System.out.println( System.currentTimeMillis()-start);
            });
            
            
            System.out.println( System.currentTimeMillis()-start);
        
        
    }
    
    public boolean removeQuiz(int quiz_id)
    {
        
        DSLContext create = Database.getJooq();            
        
        try{
            create.delete(QUIZ)
                    .where(QUIZ.QUIZ_ID.equals(quiz_id))
                    .execute();
        }
        catch(Exception e)
        {
            return false;
        }
        
        return true;  
    }
    
    public boolean updateQuiz(int quiz_id, int time_limit, String module_id, String title, boolean publish_status)
    {
        
        DSLContext create = Database.getJooq();            
        
        try{
            create.update(QUIZ)
                    .set(QUIZ.TIME_LIMIT, time_limit)
                    .set(QUIZ.MODULE_ID, module_id)
                    .set(QUIZ.TITLE, title)
                    .set(QUIZ.PUBLISH_STATUS, (byte)(publish_status?1:0))
                    .where(QUIZ.QUIZ_ID.equals(quiz_id))
                    .execute();
        }
        catch(Exception e)
        {
            return false;
        }
        
        return true;  
    }
    
    public ArrayList<String> getModuleList(DSLContext dslCont) throws SQLException{
        
        DSLContext create = Database.getJooq();
    
        String sql = create.select(MODULE.MODULE_ID)
                            .from(MODULE)
                            .getSQL();
        
        Result<Record> result = create.fetch(sql);
        ArrayList<String> allModules = new ArrayList<>();
        for(Record r : result){
                
                allModules.add((String)r.getValue(MODULE.MODULE_ID));
                
        }
        return allModules;
    }
}
