package com.github.ac31007_group_8.quiz.staff.models;

import com.github.ac31007_group_8.quiz.Database;
import com.github.ac31007_group_8.quiz.generated.tables.pojos.Quiz;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

import java.util.Set;
import java.util.stream.Collectors;

import static com.github.ac31007_group_8.quiz.generated.Tables.*;

/**
 * Model for Quiz Stats
 *
 * @author Robert T.
 */
public class StatsModel {

    private final Result<Record> fetchResult;
    private final int quizId;

    public StatsModel(int quizId) {
        this.quizId = quizId;
        DSLContext db = Database.getJooq();
        fetchResult = db.select()//RESULT.fields())
                .from(QUIZ)
                .join(RESULT).on(QUIZ.QUIZ_ID.equal(RESULT.QUIZ_ID))
                .join(RESULT_TO_ANSWER).on(RESULT.RESULT_ID.equal(RESULT_TO_ANSWER.RESULT_ID))
                .join(QUESTION).on(QUIZ.QUIZ_ID.equal(QUESTION.QUIZ_ID))
                .join(ANSWER).on(RESULT_TO_ANSWER.ANSWER_ID.equal(ANSWER.ANSWER_ID))
                .where(QUIZ.QUIZ_ID.equal(quizId))
                .fetch();
    }

    /**
     * Gets the Quiz this model refers to.
     *
     * @return The Quiz object, or null if it doesn't exist.
     */
    public Quiz getQuiz() {
        Set<Quiz> quizzes = fetchResult.into(Quiz.class).stream().collect(Collectors.toSet());
        if (quizzes.size() > 1) throw new IllegalArgumentException("Multiple quizzes with same ID!? " + quizId);
        return quizzes.stream().findFirst().orElse(null);
    }

    /**
     * Gets the set of results for this quiz.
     *
     * @return Set of results.
     */
    public Set<com.github.ac31007_group_8.quiz.generated.tables.pojos.Result> getResults() {
        return fetchResult
                .into(com.github.ac31007_group_8.quiz.generated.tables.pojos.Result.class)
                .stream().collect(Collectors.toSet());
    }

    /**
     * @deprecated Fix any usage of this later.
     * @return The raw jOOQ records.
     */
    @Deprecated
    public Result<Record> getRawRecords() {
        return fetchResult;
    }

}
