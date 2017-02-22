package com.github.ac31007_group_8.quiz.student.models;
//
//import com.github.ac31007_group_8.quiz.Database;
//import com.github.ac31007_group_8.quiz.staff.store.Answer;
//import com.github.ac31007_group_8.quiz.staff.store.Question;
//import com.github.ac31007_group_8.quiz.staff.store.Quiz;
//import org.jooq.*;
//
//import org.apache.commons.lang3.tuple.*;
//
//import java.util.LinkedList;
//import java.util.List;
//import java.util.logging.Logger;
//
//import static com.github.ac31007_group_8.quiz.generated.Tables.*;
//
//
///**
// * Created by Can on 21/02/2017.
// */
public class StudentQuizModel {
//
//    DSLContext dbConnection = null;
//
//    public StudentQuizModel(){
//        dbConnection = Database.getJooq(); //Connects to the database
//    }
//
//    public Quiz getQuiz(int quizID){
//
//        String sql = dbConnection.select() //Selects all fields if empty
//                .from(QUIZ)
//                .where(QUIZ.QUIZ_ID.equal(quizID))
//                .getSQL();
//
//        try{
//
//            Result<Record> result = dbConnection.fetch(sql);
//
//            for(Record r : result){ //Iterates through the returned results
//
//                Quiz quiz = new Quiz(r.get(QUIZ.QUIZ_ID),r.get(QUIZ.STAFF_ID), r.get(QUIZ.TIME_LIMIT),
//                        r.get(QUIZ.MODULE_ID), r.get(QUIZ.TITLE), r.get(QUIZ.PUBLISH_STATUS)!=0);   //!=0 Converts a single bit to boolean
//                return quiz;
//
//            }
//        }
//        catch(Exception e)
//        {
//            Logger.getGlobal().info("Exception: " + e.getMessage());
//        }
//        return null;
//
//    }
//
//    public List<Question> getQuestions(int quizID){
//
//        List<Question> questions = new LinkedList<Question>();
//
//        String sql = dbConnection.select() //Selects all fields if empty
//                .from(QUESTION)
//                .where(QUESTION.QUIZ_ID.equal(quizID))
//                .getSQL();
//
//        try{
//
//            Result<Record> result = dbConnection.fetch(sql);
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
//        List<Answer> answers = new LinkedList<Answer>();
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
//
//    public List<Pair<Question,List<Answer>>> getQuestionSets(int quizID){
//
//        //definition: questionSet, in this case, is a pair made up of a question and its relevant answers.
//
//        List<Answer> answers = new LinkedList<Answer>();
//
//        //TODO: Replace with a prepared statement later.
//        String sql = "SELECT * FROM `question`, `answer` WHERE `question`.question_id = `answer`.question_id AND `question`.quiz_id = " + quizID + " ORDER BY `question`.question_id asc;";
//
//                /*dbConnection.select() //Selects all fields if empty
//                .from(QUESTION)
//                .leftOuterJoin(ANSWER)
//                .on(QUESTION.QUESTION_ID.equal(ANSWER.QUESTION_ID))
//                //.where("quiz_id = ?", Integer.toString(quizID))
//                //.where(QUESTION.QUIZ_ID.equal(quizID))
//                .orderBy(QUESTION.QUESTION_ID.asc())
//                .getSQL();*/
//
//        /*
//        .from(QUESTION).join(ANSWER).using(QUESTION.QUESTION_ID)
//                .where(QUESTION.QUIZ_ID.eq(1))
//                .orderBy(QUESTION.QUESTION_ID.asc())
//                .getSQL();
//         */
//
//
//
//        Logger.getGlobal().info("Tried to get question set for Quiz ID: " + quizID);
//
//        try{
//
//            Result<Record> result = dbConnection.fetch(sql);
//
//            List<Pair<Question,List<Answer>>> questionSets = new LinkedList<Pair<Question,List<Answer>>>();
//
//            MutablePair<Question,List<Answer>> pair = new MutablePair<Question,List<Answer>>();
//            for(Record r : result) { //Each row contains a question and an answer for that question
//
//                //check if we are still adding answers for the same question, or if this is the first question.
//                int questionID = r.get(QUESTION.QUESTION_ID);
//
//                if (pair.getLeft() == null || questionID != pair.getLeft().getQuestionID()) //no NPE should occur since left condition gets checked first.
//                {
//                    //add old pair to list and move on to new pair
//                    if (pair.getLeft() != null)
//                    {
//                        questionSets.add(pair);
//                    }
//                    //reset pair
//                    pair = new MutablePair<Question,List<Answer>>();
//
//                    //set question
//                    Question question = new Question(r.get(QUESTION.QUESTION_ID),r.get(QUESTION.QUIZ_ID),
//                            r.get(QUESTION.QUESTION_), r.get(QUESTION.EXPLANATION));
//                    pair.setLeft(question);
//
//                }
//
//                //add answer to question set
//                if (pair.getRight() == null) pair.setRight(new LinkedList<Answer>());
//
//                Answer answer = new Answer(r.get(ANSWER.ANSWER_ID),r.get(ANSWER.QUESTION_ID),
//                        r.get(ANSWER.ANSWER_), r.get(ANSWER.IS_CORRECT)!=0);
//                pair.getRight().add(answer);
//            }
//            //add last pair
//            if (pair.getLeft() != null) questionSets.add(pair);
//
//            //now we have a full list of question sets
//            return questionSets;
//
//
//        }
//        catch(Exception e)
//        {
//            Logger.getGlobal().info("Exception: " + e.getMessage());
//        }
//        return null;
//
//    }
//
//
}
