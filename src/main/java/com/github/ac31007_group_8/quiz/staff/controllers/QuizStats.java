package com.github.ac31007_group_8.quiz.staff.controllers;

import com.github.ac31007_group_8.quiz.Database;
import com.github.ac31007_group_8.quiz.generated.tables.pojos.Answer;
import com.github.ac31007_group_8.quiz.generated.tables.pojos.Question;
import com.github.ac31007_group_8.quiz.util.Init;
import com.github.ac31007_group_8.quiz.util.Pair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.HaltException;
import spark.Request;
import spark.Response;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static spark.Spark.*;
import static com.github.ac31007_group_8.quiz.generated.Tables.*;

/**
 * Routes for getting quiz results.
 *
 * @author Robert T.
 */
public class QuizStats {

    private QuizStats() {}

    private static final Logger LOGGER = LoggerFactory.getLogger(QuizStats.class);
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    @Init
    public static void init() {
        get("/staff/getResults/:id", QuizStats::getAllResults);
        get("/staff/getResults/:id/:student", QuizStats::getResultsForStudent);
    }

    private static Object getAllResults(Request req, Response res) {
        try {
            int id = Integer.parseInt(req.params("id"));

            DSLContext db = Database.getJooq();

            // This'd be nicer if Java had proper tuples like Scala. Why are we using Java again? Oh wait...
            Pair<Map<Integer, Question>, Map<Integer, Set<Answer>>> qAndA = getQuestionsAnswersForQuiz(db, id);
            Map<Integer, Question> questions = qAndA.first;
            Map<Integer, Set<Answer>> answers = qAndA.second;

            Result<Record> results = db.select()//RESULT.fields())
                    .from(QUIZ)
                    .join(RESULT).on(QUIZ.QUIZ_ID.equal(RESULT.QUIZ_ID))
                    .join(RESULT_TO_ANSWER).on(RESULT.RESULT_ID.equal(RESULT_TO_ANSWER.RESULT_ID))
                    .join(QUESTION).on(QUIZ.QUIZ_ID.equal(QUESTION.QUIZ_ID))
                    .join(ANSWER).on(RESULT_TO_ANSWER.ANSWER_ID.equal(ANSWER.ANSWER_ID))
                    .where(QUIZ.QUIZ_ID.equal(id))
                    .fetch();

            throw halt(500, "Not Implemented");
        } catch (HaltException ex) {
            // Pass!
            throw ex;
        } catch (NumberFormatException ex) {
            throw halt(400, "{\"error\": \"invalid id\"}");
        } catch (Exception ex) {
            LOGGER.error("Error handling request.", ex);
            throw halt(500);
        }
    }

    private static Object getResultsForStudent(Request req, Response res) {
        // TODO
        throw halt(500, "Not Implemented");
    }

    private static Pair<Map<Integer, Question>, Map<Integer, Set<Answer>>> getQuestionsAnswersForQuiz(DSLContext db,
                                                                                                      int quizId) {
        Result<Record> questionsAndAnswersResult = db.select()
                .from(QUIZ)
                .join(QUESTION).on(QUIZ.QUIZ_ID.equal(QUESTION.QUIZ_ID))
                .join(ANSWER).on(QUESTION.QUESTION_ID.equal(ANSWER.QUESTION_ID))
                .where(QUIZ.QUIZ_ID.equal(quizId))
                .fetch();

        // Based on http://stackoverflow.com/a/20363874
        // Note: toMap has a lambda at the end to resolve clashes (which we ignore and merge)
        Map<Integer, Question> questions = questionsAndAnswersResult.into(Question.class).stream()
                .collect(Collectors.toMap(Question::getQuestionId, Function.identity(), (a, b) -> a));

        Map<Integer, Set<Answer>> answers = questionsAndAnswersResult.into(Answer.class).stream()
                .collect(Collectors.groupingBy(Answer::getQuestionId, Collectors.toSet()));

        return new Pair<>(questions, answers);
    }

}
