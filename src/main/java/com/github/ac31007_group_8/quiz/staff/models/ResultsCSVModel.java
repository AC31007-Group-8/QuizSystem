package com.github.ac31007_group_8.quiz.staff.models;

import com.github.ac31007_group_8.quiz.Database;
import com.github.ac31007_group_8.quiz.generated.tables.pojos.Quiz;
import com.github.ac31007_group_8.quiz.generated.tables.pojos.Student;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.ac31007_group_8.quiz.generated.Tables.*;

/**
 * Model for results -> CSV conversion.
 *
 * Note: This model is NOT unit-tested - the jOOQ mocking framework just isn't very good at handling multiple-join
 * queries in a usable manner.
 *
 * @author Robert T.
 */
public class ResultsCSVModel {

    private final int quizId;
    private final Result<Record> fetchResult;

    public ResultsCSVModel(int quizId) {
        this.quizId = quizId;
        DSLContext db = Database.getJooq();
        fetchResult = db.select()
                .from(QUIZ)
                .join(RESULT).on(QUIZ.QUIZ_ID.equal(RESULT.QUIZ_ID))
                .join(STUDENT).on(STUDENT.STUDENT_ID.equal(RESULT.STUDENT_ID))
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
     * Gets the results from this model.
     *
     * @return A set of QuizResult objects with every completed quiz attempt.
     */
    public Set<QuizResult> getResults() {
        return fetchResult.stream().map(it -> {
            // wtb type aliases
            com.github.ac31007_group_8.quiz.generated.tables.pojos.Result res = it.into(
                    com.github.ac31007_group_8.quiz.generated.tables.pojos.Result.class);
            Student student = it.into(Student.class);
            return new QuizResult(student.getMatricNumber(), res.getScore(), res.getDate(), res.getDuration());
        }).collect(Collectors.toSet());
    }

    // Again with the lack of data classes, Java. Seriously.
    public static class QuizResult {
        public final String matric;
        public final int score;
        public final Date date;
        public final int duration;

        public QuizResult(String matric, int score, Date date, int duration) {
            this.matric = matric;
            this.score = score;
            this.date = date;
            this.duration = duration;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof QuizResult) {
                QuizResult res = (QuizResult)obj;
                if (this.matric.equals(res.matric) && this.score == res.score && this.date.equals(res.date)
                        && this.duration == res.duration) {
                    return true;
                }
            }
            return false;
        }
    }

}
