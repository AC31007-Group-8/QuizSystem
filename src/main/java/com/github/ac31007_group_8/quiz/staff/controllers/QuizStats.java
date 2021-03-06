package com.github.ac31007_group_8.quiz.staff.controllers;

import com.github.ac31007_group_8.quiz.Database;
import com.github.ac31007_group_8.quiz.generated.tables.pojos.Answer;
import com.github.ac31007_group_8.quiz.generated.tables.pojos.Question;
import com.github.ac31007_group_8.quiz.generated.tables.pojos.Quiz;
import com.github.ac31007_group_8.quiz.staff.models.QuizModel;
import com.github.ac31007_group_8.quiz.staff.models.ResultsCSVModel;
import com.github.ac31007_group_8.quiz.staff.models.StatsModel;
import com.github.ac31007_group_8.quiz.util.CSVConverter;
import com.github.ac31007_group_8.quiz.util.Init;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static spark.Spark.*;

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
        get("/staff/getResults/csv/:id", QuizStats::getResultsCSV);
        get("/staff/getResults/:id", QuizStats::getAllResults);
        get("/staff/getResults/:id/:student", QuizStats::getResultsForStudent);
    }

    private static Object getResultsCSV(Request req, Response res) {
        try {
            int id = Integer.parseInt(req.params("id"));
            ResultsCSVModel model = new ResultsCSVModel(id);

            Quiz quiz = model.getQuiz();
            if (quiz == null) throw halt(404, "{\"error\": \"no such quiz\"}");
            Set<ResultsCSVModel.QuizResult> results = model.getResults();

            CSVConverter<ResultsCSVModel.QuizResult> converter =
                    new CSVConverter<>(ResultsCSVModel.QuizResult.class, results);

            // Based on http://stackoverflow.com/q/27244780
            res.raw().setContentType("application/octet-stream");
            res.raw().setHeader("Content-Disposition","attachment; filename=" + id + ".csv");
            return converter.toCSV();
        } catch (HaltException ex) {
            throw ex; // Propagate halt() calls
        } catch (NumberFormatException ex) {
            throw halt(400, "{\"error\": \"invalid id\"}");
        } catch (Exception ex) {
            LOGGER.error("Error handling request.", ex);
            throw halt(500);
        }
    }

    private static Object getAllResults(Request req, Response res) {
        try {
            int id = Integer.parseInt(req.params("id"));
            
            StatsModel model = new StatsModel(id);

            Quiz quiz = model.getQuiz();
            if (quiz == null) throw halt(404, "{\"error\": \"no such quiz\"}");

            // Today's issue of "wtf Java": lack of type aliases
            Set<com.github.ac31007_group_8.quiz.generated.tables.pojos.Result> results = model.getResults();

            int attemptsTotal = results.size();
            int uniqueAttempts = results.stream()
                    .collect(Collectors.toMap(
                            com.github.ac31007_group_8.quiz.generated.tables.pojos.Result::getStudentId,
                            Function.identity(), (a, b) -> a)).size();

            List<QuestionStatsData> qStats = getQuestionStats(model.getRawRecords());

            Map<Integer, Integer> scores = new HashMap<>();
            results.forEach(it -> scores.put(it.getScore(), scores.getOrDefault(it.getScore(), 0) + 1));

            QuizStatsData stats = new QuizStatsData(quiz, qStats, attemptsTotal, uniqueAttempts, scores);
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
        public final Map<Integer, Integer> scores;

        public QuizStatsData(Quiz quiz, List<QuestionStatsData> questions, int timesAttempted,
                             int uniqueStudentsAttempted, Map<Integer, Integer> scores) {
            this.quiz = quiz;
            this.questions = questions;
            this.timesAttempted = timesAttempted;
            this.uniqueStudentsAttempted = uniqueStudentsAttempted;
            this.scores = scores;
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
