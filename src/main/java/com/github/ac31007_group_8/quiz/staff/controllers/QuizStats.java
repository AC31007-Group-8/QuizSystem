package com.github.ac31007_group_8.quiz.staff.controllers;

import com.github.ac31007_group_8.quiz.Database;
import com.github.ac31007_group_8.quiz.generated.tables.pojos.Answer;
import com.github.ac31007_group_8.quiz.generated.tables.pojos.Question;
import com.github.ac31007_group_8.quiz.generated.tables.pojos.Quiz;
import com.github.ac31007_group_8.quiz.util.Init;
import com.github.ac31007_group_8.quiz.util.Pair;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
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

import java.util.List;
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

            // TODO: Refactor into model
            DSLContext db = Database.getJooq();
            Result<Record> dbResults = db.select()//RESULT.fields())
                    .from(QUIZ)
                    .join(RESULT).on(QUIZ.QUIZ_ID.equal(RESULT.QUIZ_ID))
                    .join(RESULT_TO_ANSWER).on(RESULT.RESULT_ID.equal(RESULT_TO_ANSWER.RESULT_ID))
                    .join(QUESTION).on(QUIZ.QUIZ_ID.equal(QUESTION.QUIZ_ID))
                    .join(ANSWER).on(RESULT_TO_ANSWER.ANSWER_ID.equal(ANSWER.ANSWER_ID))
                    .where(QUIZ.QUIZ_ID.equal(id))
                    .fetch();

            Set<Quiz> quizzes = dbResults.into(Quiz.class).stream().collect(Collectors.toSet());
            if (quizzes.size() == 0) {
                throw halt(404, "{\"error\": \"no such quiz\"}");
            }
            Quiz quiz = quizzes.toArray(new Quiz[]{})[0];

            // This'd be nicer if Java had proper tuples like Scala. Why are we using Java again? Oh wait...
            //Pair<Map<Integer, Question>, Map<Integer, Set<Answer>>> qAndA = getQuestionsAnswersForQuiz(db, id);
            //Map<Integer, Question> questions = qAndA.first;
            //Map<Integer, Set<Answer>> answers = qAndA.second;

            // wtb type aliases
            Set<com.github.ac31007_group_8.quiz.generated.tables.pojos.Result> results = dbResults
                    .into(com.github.ac31007_group_8.quiz.generated.tables.pojos.Result.class)
                    .stream().collect(Collectors.toSet());

            int attemptsTotal = results.size();
            int uniqueAttempts = results.stream()
                    .collect(Collectors.toMap(
                            com.github.ac31007_group_8.quiz.generated.tables.pojos.Result::getStudentId,
                            Function.identity(), (a, b) -> a)).size();

            List<QuestionStatsData> qStats = getQuestionStats(dbResults);

            QuizStatsData stats = new QuizStatsData(quiz, qStats, attemptsTotal, uniqueAttempts);
            return GSON.toJson(stats);
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

    // TODO: Refactor out into a model class.
    // TODO: Also probably make an intermediate type to hold all the rows for mocking.
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

    private static List<QuestionStatsData> getQuestionStats(Result<Record> dbResults) {
        Multiset<Question> correct = HashMultiset.create();
        Multiset<Question> incorrect = HashMultiset.create();

        dbResults.forEach((it) -> {
            Question q = it.into(Question.class);
            Answer a = it.into(Answer.class);
            if (a.getIsCorrect() == 1) correct.add(q); else incorrect.add(q);
        });

        // We need to copy the set.                   vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
        Set<Question> questions = correct.elementSet().stream().collect(Collectors.toSet());
        questions.addAll(incorrect.elementSet());

        return questions.stream()
                .map(it -> new QuestionStatsData(it, correct.count(it), incorrect.count(it)))
                .collect(Collectors.toList());
    }

    // DATA CLASSES
    // Seriously, Java, why don't you have a thing for this bullshit? Kotlin and Scala do. </rant>
    @SuppressWarnings("WeakerAccess")
    private static class QuizStatsData {
        public final Quiz quiz;
        public final List<QuestionStatsData> questions;
        public final int timesAttempted;
        public final int uniqueStudentsAttempted;

        public QuizStatsData(Quiz quiz, List<QuestionStatsData> questions, int timesAttempted,
                             int uniqueStudentsAttempted) {
            this.quiz = quiz;
            this.questions = questions;
            this.timesAttempted = timesAttempted;
            this.uniqueStudentsAttempted = uniqueStudentsAttempted;
        }
    }

    @SuppressWarnings("WeakerAccess")
    private static class QuestionStatsData {
        public final Question question;
        public final int rightAnswers;
        public final int wrongAnswers;

        public QuestionStatsData(Question question, int rightAnswers, int wrongAnswers) {
            this.question = question;
            this.rightAnswers = rightAnswers;
            this.wrongAnswers = wrongAnswers;
        }
    }

}
