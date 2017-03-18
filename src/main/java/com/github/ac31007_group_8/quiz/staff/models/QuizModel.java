/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ac31007_group_8.quiz.staff.models;

import com.github.ac31007_group_8.quiz.Database;
import static com.github.ac31007_group_8.quiz.generated.Tables.*;
import  com.github.ac31007_group_8.quiz.generated.tables.records.*;
import com.github.ac31007_group_8.quiz.staff.store.QuizInfo;
import com.github.ac31007_group_8.quiz.staff.store.Quiz;
import com.github.ac31007_group_8.quiz.staff.store.Answer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record8;
import org.jooq.Result;
import org.jooq.SortField;
import org.jooq.exception.DataAccessException;
import static org.jooq.impl.DSL.val;

/**
 *
 * @author Erik Jeny
 */
public class QuizModel {
    
    public QuizModel(){
    }
    
    
    public ArrayList<QuizInfo> getAllQuizInfo(DSLContext create) throws DataAccessException    {      
        
        ArrayList<QuizInfo> allQuizInfo = new ArrayList();
        
        

        String sql = create.select(STAFF.FIRST_NAME,STAFF.SECOND_NAME,MODULE.MODULE_NAME,
                                    QUIZ.PUBLISH_STATUS,QUIZ.QUIZ_ID,QUIZ.TIME_LIMIT,QUIZ.TITLE,QUIZ.MODULE_ID )
                        .from(QUIZ)
                        .join(MODULE).on(QUIZ.MODULE_ID.equal(MODULE.MODULE_ID))
                        .join(STAFF).on(QUIZ.STAFF_ID.equal(STAFF.STAFF_ID))
                        .getSQL();

        Result<Record> result = create.fetch(sql);

        for(Record r : result){
            allQuizInfo.add(r.into(QuizInfo.class)); 
        }
        
        return allQuizInfo;
    }
    

    
    public ArrayList<QuizInfo> getQuizzesFiltered(DSLContext dslCont,String moduleCode,String isPublished,String creator, String sortBy) throws DataAccessException{


        List<Condition> conditions = new ArrayList<>();
        
        if (!moduleCode.equals("-1")){
            conditions.add(QUIZ.MODULE_ID.equal(val(moduleCode)));
        }
        
        if ( isPublished.equals("1") || isPublished.equals("0")){
            conditions.add(QUIZ.PUBLISH_STATUS.equal((byte)Integer.parseInt(isPublished)));
        }
        
        //better search? http://stackoverflow.com/questions/3338889/how-to-find-similar-results-and-sort-by-similarity
        if (!creator.equals("")){
            conditions.add((STAFF.FIRST_NAME.contains(val(creator))).or(STAFF.SECOND_NAME.contains(val(creator))));
        }
        
        SortField sortTarget =null;
        
        if (sortBy.equals("byName")){
            sortTarget= MODULE.MODULE_NAME.asc();
        }
        else if (sortBy.equals("byStatus")){
            sortTarget= QUIZ.PUBLISH_STATUS.asc();
        }
        else if (sortBy.equals("byTitle")){
            sortTarget= QUIZ.TITLE.asc();
        }
        else if (sortBy.equals("byCode")){
            sortTarget= QUIZ.MODULE_ID.asc();
        }
          

        Result<Record8<String, String, String, Byte, Integer, Integer, String, String>> result = dslCont.select(STAFF.FIRST_NAME,STAFF.SECOND_NAME,MODULE.MODULE_NAME,
                                    QUIZ.PUBLISH_STATUS,QUIZ.QUIZ_ID,QUIZ.TIME_LIMIT,QUIZ.TITLE,QUIZ.MODULE_ID )
                        .from(QUIZ)
                        .join(MODULE).on(QUIZ.MODULE_ID.equal(MODULE.MODULE_ID))
                        .join(STAFF).on(QUIZ.STAFF_ID.equal(STAFF.STAFF_ID))
                        .where(conditions)
                        .orderBy(sortTarget)
                        .fetch();
        

        ArrayList<QuizInfo> allQuizInfo = new ArrayList();
        
        
        for(Record r : result){
            allQuizInfo.add(r.into(QuizInfo.class)); 
        }
        
       
        return allQuizInfo;
        

    }
    
    
    
    public void saveQuiz(Quiz quizToSave, DSLContext create) throws DataAccessException{
        
        
            //store quiz
            QuizRecord quizR = create.newRecord(QUIZ, quizToSave);
            quizR.store();
            Integer quizId = quizR.getQuizId();
           
          
          
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
              
                create.batchInsert(answers).execute();
               
            });
            
            
           
        
        
    }
        
      
    public ArrayList<String> getModuleList(DSLContext dslCont) throws DataAccessException{
        
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
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    // INCORRECT BELOW :)
    
    
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
    
    
}
