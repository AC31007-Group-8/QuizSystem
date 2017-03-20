package com.github.ac31007_group_8.quiz.student.models;

import com.github.ac31007_group_8.quiz.Database;
import static com.github.ac31007_group_8.quiz.generated.Tables.*;
import java.util.ArrayList;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

import java.util.logging.Logger;

import com.github.ac31007_group_8.quiz.staff.store.*;

/**
 * Created by Callum
 */
public class ResultListModel {

    DSLContext dbConnection = null;

    public ResultListModel(){
        dbConnection = Database.getJooq(); //Connects to the database
    }

    public StudentResults getCompleteQuizzes(int studentID){
        StudentResults studResults = new StudentResults(); // Holds all attempted quizzes
        QuizAttempts quizAttempts = new QuizAttempts(); //Holds all attempts on one quiz
        ArrayList<QuizAttempt> quizAttemptsArray = new ArrayList<>(); //Holds all Attempts on one quiz
        ArrayList<QuizAttempts> studentResultsArray = new ArrayList<>();// Holds all attempted quizzes by the student

        try{

            Result<Record> result = dbConnection.select()
                    .from(RESULT)
                    .where(RESULT.STUDENT_ID.equal(studentID))
                    .orderBy(RESULT.QUIZ_ID.asc(), RESULT.DATE.asc())
                    .fetch();

            boolean foundQuizFlag = false;
            int quizIDr = -1;//Will be compared with quizIDs to determine when we have moved on to look at the results of a different quiz

            for(Record r : result){//Iterates through the returned results
                Logger.getGlobal().info("Record id: " + r.get(RESULT.QUIZ_ID));
                foundQuizFlag = true;
                if (quizIDr == -1) {
                    quizIDr = r.get(RESULT.QUIZ_ID);

                    QuizAttempt attempt = new QuizAttempt(); //Holds one attempt at one quiz
                    attempt.setResultID(r.get(RESULT.RESULT_ID));
                    attempt.setScore(r.get(RESULT.SCORE));
                    attempt.setDate(r.get(RESULT.DATE).toString());
                    quizAttemptsArray.add(attempt);// Add the attempt to the list of attempts at one specific quiz

                }else {
                    if(quizIDr!=r.get(RESULT.QUIZ_ID)){
                        int previousQuizID = quizIDr;
                        quizIDr = r.get(RESULT.QUIZ_ID);
                        quizAttempts.setAttempts(quizAttemptsArray);//Put array into quizAttempts store
                        quizAttempts.setQuizID(previousQuizID);//Set the quiz id on the quizAttempts store
                        Record titleRec = dbConnection.select(QUIZ.TITLE)
                                .from(QUIZ)
                                .where(QUIZ.QUIZ_ID.equal(previousQuizID))
                                .fetchOne();
                        Logger.getGlobal().info("title from db is: " + titleRec.get(QUIZ.TITLE));
                        quizAttempts.setTitle(titleRec.get(QUIZ.TITLE));
                        studentResultsArray.add(quizAttempts); //Now add the quizAttempts store to the array of all student results
                        quizAttemptsArray = new ArrayList<>(); //Clear the current list of attempts on the previous quiz for the new quiz attempts
                        quizAttempts = new QuizAttempts();

                        QuizAttempt attempt = new QuizAttempt(); //Holds one attempt at one quiz

                        attempt.setResultID(r.get(RESULT.RESULT_ID));
                        attempt.setScore(r.get(RESULT.SCORE));
                        attempt.setDate(r.get(RESULT.DATE).toString());
                        quizAttemptsArray.add(attempt);// Add the attempt to the list of attempts at one specific quiz
                    }
                    else{
                        QuizAttempt attempt = new QuizAttempt(); //Holds one attempt at one quiz

                        attempt.setResultID(r.get(RESULT.RESULT_ID));
                        attempt.setScore(r.get(RESULT.SCORE));
                        attempt.setDate(r.get(RESULT.DATE).toString());
                        quizAttemptsArray.add(attempt);// Add the attempt to the list of attempts at one specific quiz
                    }
                }
            }
            if(foundQuizFlag)
            {
                quizAttempts.setAttempts(quizAttemptsArray);//Put array into quizAttempts store
                quizAttempts.setQuizID(quizIDr);//Set the quiz id on the quizAttempts store
                Record titleRec = dbConnection.select(QUIZ.TITLE)
                        .from(QUIZ)
                        .where(QUIZ.QUIZ_ID.equal(quizIDr))
                        .fetchOne();
                Logger.getGlobal().info("title from db is: " + titleRec.get(QUIZ.TITLE));
                quizAttempts.setTitle(titleRec.get(QUIZ.TITLE));
                studentResultsArray.add(quizAttempts); //Now add the quizAttempts store to the array of all student results


            }
            studResults.setStudResults(studentResultsArray);
            return studResults;
        }
        catch(Exception e)
        {
            Logger.getGlobal().info("Exception: " + e.getMessage());
        }
        return null;
    }

}

/*  for(Record r : result) { //Iterates through the returned results
                int quizIDr = r.get(RESULT.QUIZ_ID);//Will be compared with quizIDs to determine when we have moved on to look at the results of a different quiz
                for (Record s : result) {
                    int quizIDs = s.get(RESULT.QUIZ_ID);
                    if(quizIDr!=quizIDs){
                        quizAttempts.setAttempts(quizAttemptsArray);//Put array into quizAttempts store
                        quizAttempts.setQuizID(quizIDr);//Set the quiz id on the quizAttempts store
                        Record titleRec = dbConnection.select(QUIZ.TITLE)
                                .from(QUIZ)
                                .where(QUIZ.QUIZ_ID.equal(quizIDr))
                                .fetchOne();
                        Logger.getGlobal().info("title from db is: " + titleRec.get(QUIZ.TITLE));
                        quizAttempts.setTitle(titleRec.get(QUIZ.TITLE));
                        studentResultsArray.add(quizAttempts); //Now add the quizAttempts store to the array of all student results
                        quizAttemptsArray = new ArrayList<>(); //Clear the current list of attempts on the previous quiz for the new quiz attempts
                    }
                    else{
                        QuizAttempt attempt = new QuizAttempt(); //Holds one attempt at one quiz

                        attempt.setResultID(s.get(RESULT.RESULT_ID));
                        attempt.setScore(s.get(RESULT.SCORE));
                        attempt.setDate(s.get(RESULT.DATE).toString());
                        quizAttemptsArray.add(attempt);// Add the attempt to the list of attempts at one specific quiz
                    }

                }
            }
            studResults.setStudResults(studentResultsArray);
            return studResults;*/
