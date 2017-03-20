package com.github.ac31007_group_8.quiz.staff.store;

/**
 * Created by Callum on 19-Mar-17.
 */
public class QuizAttempt {
    int resultID;
    int score;
    String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getResultID() {
        return resultID;
    }

    public void setResultID(int resultID) {
        this.resultID = resultID;
    }
}
