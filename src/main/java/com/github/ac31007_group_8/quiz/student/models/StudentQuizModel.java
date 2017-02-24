package com.github.ac31007_group_8.quiz.student.models;

import com.github.ac31007_group_8.quiz.Database;
import com.github.ac31007_group_8.quiz.staff.models.QuizModel;
import com.github.ac31007_group_8.quiz.staff.store.Answer;
import com.github.ac31007_group_8.quiz.staff.store.Question;
import com.github.ac31007_group_8.quiz.staff.store.Quiz;
import com.github.ac31007_group_8.quiz.staff.store.QuizSection;
import org.jooq.*;

import org.apache.commons.lang3.tuple.*;

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

    public List<Question> getQuestions(int quizID){

        //TODO: Consider using ArrayList if typical usage of the returned result ends up doing a substantial number of element lookups
        List<Question> questions = new LinkedList<Question>();


        try{

            //String sql = "SELECT * FROM `question` WHERE `question`.quiz_id = " + quizID + " ORDER BY `question`.question_id asc;";
            //Replaced with prepared statements through JOOQ:
            Result<Record> result = dbConnection.select()
                    .from(QUESTION)
                    .where(QUESTION.QUIZ_ID.equal(quizID))
                    .orderBy(QUESTION.QUESTION_ID.asc())
                    .fetch();

            for(Record r : result){ //Iterates through the returned results
                Question question = new Question(r.get(QUESTION.QUESTION_ID),r.get(QUESTION.QUIZ_ID),
                        r.get(QUESTION.QUESTION_), r.get(QUESTION.EXPLANATION));
                questions.add(question);
            }

            return questions;
        }
        catch(Exception e)
        {
            Logger.getGlobal().info("Exception: " + e.getMessage());
        }
        return null;
    }

    public List<Answer> getAnswers(int questionID){

        List<Answer> answers = new LinkedList<Answer>();

        String sql = dbConnection.select() //Selects all fields if empty
                .from(ANSWER)
                .where(ANSWER.QUESTION_ID.equal(questionID))
                .getSQL();

        try{

            Result<Record> result = dbConnection.fetch(sql);

            for(Record r : result){ //Iterates through the returned results
                Answer answer = new Answer(r.get(ANSWER.ANSWER_ID),r.get(ANSWER.QUESTION_ID),
                        r.get(ANSWER.ANSWER_), r.get(ANSWER.IS_CORRECT)!=0);
                answers.add(answer);
            }

            return answers;
        }
        catch(Exception e)
        {
            Logger.getGlobal().info("Exception: " + e.getMessage());
        }
        return null;

    }

    public List<QuizSection> getQuizSections(int quizID){

        Logger.getGlobal().info("Trying to get quizSection for Quiz ID: " + quizID);

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
            List<QuizSection> quizSections = new LinkedList<QuizSection>();

            QuizSection pair = new QuizSection();
            for(Record r : result) { //Each row contains a question and an answer for that question

                //check if we are still adding answers for the same question, or if this is the first question.
                int questionID = r.get(QUESTION.QUESTION_ID);

                if (pair.getQuestion() == null || questionID != pair.getQuestion().getQuestionID()) //no NPE should occur since left condition gets checked first.
                {
                    //add old pair to list and move on to new pair
                    if (pair.getQuestion() != null)
                    {
                        quizSections.add(pair);
                    }
                    //reset pair
                    pair = new QuizSection();

                    //set question
                    Question question = new Question(r.get(QUESTION.QUESTION_ID),r.get(QUESTION.QUIZ_ID),
                            r.get(QUESTION.QUESTION_), r.get(QUESTION.EXPLANATION));
                    pair.setQuestion(question);

                }

                //add answer to quiz section (pair)
                if (pair.getAnswers() == null) pair.setAnswers(new LinkedList<Answer>());

                Answer answer = new Answer(r.get(ANSWER.ANSWER_ID),r.get(ANSWER.QUESTION_ID),
                        r.get(ANSWER.ANSWER_), r.get(ANSWER.IS_CORRECT)!=0);
                pair.getAnswers().add(answer);
            }
            //add last pair
            if (pair.getQuestion() != null) quizSections.add(pair);

            //now we have a full list of question sets
            return quizSections;


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
        List<QuizSection> quizSections = getQuizSections(quizID);
        quiz.setQuizSections(quizSections);

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

            Logger.getGlobal().info("Recording answers for result ID: " + resultID);

            //write each submitted answer into DB
            for (Integer answerID : answerIDs) {
                Logger.getGlobal().info("Record answer, ID: " + answerID);
                dbConnection.insertInto(RESULT_TO_ANSWER, RESULT_TO_ANSWER.RESULT_ID, RESULT_TO_ANSWER.ANSWER_ID)
                        .values(resultID, answerID).execute();
            }

        }
        else {
            Logger.getGlobal().info("Error: did not insert result into DB!");
        }
    }

}
