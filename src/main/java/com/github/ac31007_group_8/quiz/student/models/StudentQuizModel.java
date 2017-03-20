package com.github.ac31007_group_8.quiz.student.models;

import com.github.ac31007_group_8.quiz.Database;
import com.github.ac31007_group_8.quiz.staff.store.Answer;
import com.github.ac31007_group_8.quiz.staff.store.Question;
import com.github.ac31007_group_8.quiz.staff.store.Quiz;
import org.jooq.*;


import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import static com.github.ac31007_group_8.quiz.generated.Tables.*;
import com.github.ac31007_group_8.quiz.staff.store.QuizInfo;

import com.github.ac31007_group_8.quiz.staff.store.QuizInfoStudent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.jooq.exception.DataAccessException;
import static org.jooq.impl.DSL.val;


///**
// * Created by Can on 21/02/2017.
// */
public class StudentQuizModel {

    DSLContext dbConnection = null;

    public StudentQuizModel(){
        dbConnection = Database.getJooq(); //Connects to the database
    }

//    public List<Question> getQuestions(int quizID){
//
//        //TODO: Consider using ArrayList if typical usage of the returned result ends up doing a substantial number of element lookups
//        List<Question> questions = new LinkedList<>();
//
//        try{
//
//            //String sql = "SELECT * FROM `question` WHERE `question`.quiz_id = " + quizID + " ORDER BY `question`.question_id asc;";
//            //Replaced with prepared statements through JOOQ:
//            Result<Record> result = dbConnection.select()
//                    .from(QUESTION)
//                    .where(QUESTION.QUIZ_ID.equal(quizID))
//                    .orderBy(QUESTION.QUESTION_ID.asc())
//                    .fetch();
//
//            for(Record r : result){ //Iterates through the returned results
//                Question question = new Question(r.get(QUESTION.QUESTION_ID),r.get(QUESTION.QUIZ_ID),
//                        r.get(QUESTION.QUESTION_), r.get(QUESTION.EXPLANATION));
//                questions.add(question);
//            }
//
//            return questions;
//        }
//        catch(Exception e)
//        {
//            Logger.getGlobal().info("Exception: " + e.getMessage());
//        }
//        return null;
//    }
//
//    public List<Answer> getAnswers(int questionID){
//
//        List<Answer> answers = new LinkedList<>();
//
//        String sql = dbConnection.select() //Selects all fields if empty
//                .from(ANSWER)
//                .where(ANSWER.QUESTION_ID.equal(questionID))
//                .getSQL();
//
//        try{
//
//            Result<Record> result = dbConnection.fetch(sql);
//
//            for(Record r : result){ //Iterates through the returned results
//                Answer answer = new Answer(r.get(ANSWER.ANSWER_ID),r.get(ANSWER.QUESTION_ID),
//                        r.get(ANSWER.ANSWER_), r.get(ANSWER.IS_CORRECT)!=0);
//                answers.add(answer);
//            }
//
//            return answers;
//        }
//        catch(Exception e)
//        {
//            Logger.getGlobal().info("Exception: " + e.getMessage());
//        }
//        return null;
//
//    }

    
    
    public List<Question> getQuestions(int quizID){//and answers!

        try{

            //Refactored to use prepared statements, through JOOQ:
            Result<Record> result = dbConnection.select() //Selects all fields if empty
                    .from(QUESTION)
                    .leftOuterJoin(ANSWER)
                    .on(QUESTION.QUESTION_ID.equal(ANSWER.QUESTION_ID))
                    //.where("quiz_id = ?", Integer.toString(quizID))
                    .where(QUESTION.QUIZ_ID.equal(quizID))
                    .orderBy(QUESTION.QUESTION_ID.asc())
                    .fetch();

            //TODO: Consider changing LinkedList to another type of List if many element lookups are being made.
            List<Question> allQuestions = new LinkedList<>();
            
            int  previousQuestionId = -1;
            int  currentQuestionId ;
            Question nextQuestion = null;
            
            for(Record r : result) {
                currentQuestionId = r.get(QUESTION.QUESTION_ID);
                if (previousQuestionId != currentQuestionId){
                    previousQuestionId = currentQuestionId;
                    
                    nextQuestion = new Question(r.get(QUESTION.QUESTION_ID),r.get(QUESTION.QUIZ_ID),
                            r.get(QUESTION.QUESTION_), r.get(QUESTION.EXPLANATION));
                    allQuestions.add(nextQuestion);
                }
                
                
                Answer answer = new Answer(r.get(ANSWER.ANSWER_ID),r.get(ANSWER.QUESTION_ID),
                        r.get(ANSWER.ANSWER_), r.get(ANSWER.IS_CORRECT)!=0);
                nextQuestion.addAnswer(answer);
                
            }
            
            return allQuestions;
            

        }
        catch(Exception e)
        {
            Logger.getGlobal().info("Exception: " + e.getMessage());
        }
        return null;

    }


    public Quiz getCompleteQuiz(int quizID)
    {
        Quiz quiz = getQuiz(quizID);
        if (quiz == null) return quiz;
        List<Question> allQuestions = getQuestions(quizID);
        quiz.setQuestions(allQuestions);

        return quiz;
    }

    public Quiz getQuiz(int quizID){

        try{

            //Refactored to use prepared statements using JOOQ:
            Result<Record> result = dbConnection.select()
            .from(QUIZ)
            .where(QUIZ.QUIZ_ID.equal(quizID))
            .fetch();

            for(Record r : result){ //Iterates through the returned results
                Quiz quiz = new Quiz(r.get(QUIZ.QUIZ_ID),r.get(QUIZ.STAFF_ID), r.get(QUIZ.TIME_LIMIT),
                        r.get(QUIZ.MODULE_ID), r.get(QUIZ.TITLE), r.get(QUIZ.PUBLISH_STATUS)!=0);   //!=0 Converts a single bit to boolean
                return quiz;
            }
        }
        catch(Exception e)
        {
            Logger.getGlobal().info("Exception: " + e.getMessage());
        }
        return null;
    }

    
    
    
    
    
    public void writeResult(int score, int quizID, int studentID, java.sql.Date date, int duration, List<Integer> answerIDs)
    {

        //Store result
        Record record = dbConnection.insertInto(RESULT, RESULT.SCORE, RESULT.QUIZ_ID, RESULT.STUDENT_ID, RESULT.DATE, RESULT.DURATION)
                .values(score, quizID, studentID, date, duration)
                .returning(RESULT.RESULT_ID)
                .fetchOne();

        //Store answers that are a part of the result created above:
        if (record != (null))
        {
            int resultID = record.get(RESULT.RESULT_ID);

            for (Integer answerID : answerIDs) {
                dbConnection.insertInto(RESULT_TO_ANSWER, RESULT_TO_ANSWER.RESULT_ID, RESULT_TO_ANSWER.ANSWER_ID)
                        .values(resultID, answerID).execute();
            }

        }
        else {
            Logger.getGlobal().info("Error: did not insert result into DB!");
        }
    }

    
    
    
    public ArrayList<QuizInfoStudent> getRelevantQuizzes(int studentId, Connection conn) throws SQLException{
        
        
        PreparedStatement stmt = conn.prepareStatement("SELECT quiz.quiz_id, quiz.time_limit, quiz.Title, module.module_name, module.module_id" +
                                        " FROM quiz" +
                                        " INNER JOIN module" +
                                        " ON quiz.module_id = module.module_id" +
                                        " INNER JOIN student_to_module" +
                                        " ON student_to_module.module_id = module.module_id " +
                                        " INNER JOIN student" +
                                        " ON student.student_id = student_to_module.student_id" +
                                        " WHERE publish_status=1 AND student.student_id=?;");
        stmt.setInt(1,studentId);
        
        
        ResultSet rs = stmt.executeQuery();   
        ArrayList<QuizInfoStudent> allRelevantQuizInfo = new ArrayList<>();
        
        while (rs.next()){
            
            
                QuizInfoStudent next = new QuizInfoStudent();
                next.setIsRelevant(true);
                next.setQuizId(rs.getInt(1));
                next.setTime_limit(rs.getInt(2)); 
                next.setTitle(rs.getString(3)); 
                next.setModule_name(rs.getString(4)); 
                next.setModule_id(rs.getString(5));
                
                allRelevantQuizInfo.add(next);       
        }
        
        //a special personal query to just get if quiz was taken by a student!
        //can't join quiz and result as result not necessarily for that student
        //can't join student and result as result not nec. for that quiz
        //circular join (o_O) would miss quizzes that were not taken
        //where would do other random stuff
        
        
        stmt = conn.prepareStatement("SELECT quiz.quiz_id" +
                                        " FROM student" +
                                        " INNER JOIN result" +
                                        " ON student.student_id = result.student_id" +
                                        " INNER JOIN quiz" +
                                        " ON result.quiz_id = quiz.quiz_id " +
                                        " WHERE publish_status=1 AND student.student_id=?"+
                                        " GROUP BY quiz.quiz_id;");
        stmt.setInt(1,studentId);
        
        
        rs = stmt.executeQuery();   
        ArrayList<QuizInfoStudent> allTakenQuizzes = new ArrayList<>();
        while (rs.next()){

            for (QuizInfoStudent qis:allRelevantQuizInfo){
                if (qis.getQuizId()==rs.getInt(1)){
                    qis.setIsTaken(true);
                    break;
                }
            }
        }

        
        
        return allRelevantQuizInfo;
    }
    
    
    
    
    //this is pain :(
     public ArrayList<QuizInfoStudent> getFilteredQuizInfo(DSLContext dslCont, String moduleCode, String sortBy, String taken, String relevant, int studentId) throws DataAccessException    {      
      
        List<Condition> conditions = new ArrayList<>();
        conditions.add(QUIZ.PUBLISH_STATUS.equal((byte)1));
        
        if (!moduleCode.equals("-1")){
            conditions.add(QUIZ.MODULE_ID.equal(val(moduleCode)));
        }
        
        SortField sortTarget =null;
        
        if (sortBy.equals("byName")){
            sortTarget= MODULE.MODULE_NAME.asc();
        }
        else if (sortBy.equals("byTitle")){
            sortTarget= QUIZ.TITLE.asc();
        }
        else if (sortBy.equals("byCode")){
            sortTarget= QUIZ.MODULE_ID.asc();
        }
          
        
        //ALL QUIZ INFO FOR SPECIFIC MODULE   

        Result<Record5<String, Integer, Integer, String, String>> result1 = dslCont.select(MODULE.MODULE_NAME,
                                    QUIZ.QUIZ_ID,QUIZ.TIME_LIMIT,QUIZ.TITLE,QUIZ.MODULE_ID )
                        .from(QUIZ)
                        .join(MODULE).on(QUIZ.MODULE_ID.equal(MODULE.MODULE_ID))
                        .join(STUDENT_TO_MODULE).on(STUDENT_TO_MODULE.MODULE_ID.equal(MODULE.MODULE_ID))
                        .join(STUDENT).on(STUDENT.STUDENT_ID.equal(STUDENT_TO_MODULE.STUDENT_ID))
                        .where(conditions)
                        .orderBy(sortTarget)
                        .fetch();
        
        
        ArrayList<QuizInfoStudent> allQuizInfo = new ArrayList();
        for(Record r : result1){
            allQuizInfo.add(r.into(QuizInfoStudent.class)); 
        }
        
        
         List conditions2 = new ArrayList(conditions);
         conditions2.add(STUDENT.STUDENT_ID.equal(studentId));
        //ALL QUIZ INFO RELEVANT FOR STUDENT and module
        
         Result<Record> result2 = dslCont.select(QUIZ.QUIZ_ID )
                        .from(QUIZ)
                        .join(MODULE).on(QUIZ.MODULE_ID.equal(MODULE.MODULE_ID))
                        .join(STUDENT_TO_MODULE).on(STUDENT_TO_MODULE.MODULE_ID.equal(MODULE.MODULE_ID))
                        .join(STUDENT).on(STUDENT.STUDENT_ID.equal(STUDENT_TO_MODULE.STUDENT_ID))
                        .where(conditions2)
                        .fetch();
     

        for(Record r : result2){
            for (QuizInfoStudent qis:allQuizInfo){
            
                
                if (qis.getQuizId()== r.get(QUIZ.QUIZ_ID)){
                    qis.setIsRelevant(true);
                    break;
                }
            } 
        }
        
        //ALL QUIZZES TAKEN BY STUDENT and module
        
        Result<Record1<Integer>> result3 = dslCont.select(QUIZ.QUIZ_ID )
                        .from(STUDENT)
                        .join(RESULT).on(STUDENT.STUDENT_ID.equal(RESULT.STUDENT_ID))
                        .join(QUIZ).on(RESULT.QUIZ_ID.equal(QUIZ.QUIZ_ID))
                        
                        .where(conditions2)
                        .fetch();
        
        for(Record r : result3){
            for (QuizInfoStudent qis:allQuizInfo){
                if (qis.getQuizId()== r.get(QUIZ.QUIZ_ID)){
                    qis.setIsTaken(true);
                    break;
                }
            } 
        }
        
        
        //FILTER BASED ON FIELDS
        
        //on relevance
        ArrayList<QuizInfoStudent> filteredQuizInfo = new ArrayList<>();
        if ( relevant.equals("isRelevant")){
            for (QuizInfoStudent qis:allQuizInfo){
                if (qis.isRelevant()){
                    filteredQuizInfo.add(qis);
                }
            }
        }
        else if ( relevant.equals("notRelevant")){
             for (QuizInfoStudent qis:allQuizInfo){
                if (!qis.isRelevant()){
                    filteredQuizInfo.add(qis);
                }
            }
        }
        else{
            filteredQuizInfo = allQuizInfo;
        }
        
        allQuizInfo = filteredQuizInfo;
        
        
        
        //on if taken
        
        filteredQuizInfo = new ArrayList<>();
        if ( taken.equals("isTaken")){
            for (QuizInfoStudent qis:allQuizInfo){
                if (qis.isTaken()){
                    filteredQuizInfo.add(qis);
                }
            }
        }
        else if (taken.equals("notTaken")){
             for (QuizInfoStudent qis:allQuizInfo){
                if (!qis.isTaken()){
                    filteredQuizInfo.add(qis);
                }
            }
        }
        else{
            filteredQuizInfo = allQuizInfo;
        }
        
        allQuizInfo = filteredQuizInfo;
         
       
        return allQuizInfo;
         
         
         
         
        


      
       
    }
    
    
    
}




