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

    public Quiz getQuiz(int quizID, DSLContext context){

        if (context!=null)
        {
            dbConnection=context;
        }

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

}
