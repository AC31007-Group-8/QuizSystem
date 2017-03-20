package com.github.ac31007_group_8.quiz.staff.store;

import java.util.ArrayList;

/**
 * Created by Callum on 19-Mar-17.
 */
public class QuizAttempts {
    ArrayList<QuizAttempt> attempts;
    int quizID;
    String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuizID() {
        return quizID;
    }

    public void setQuizID(int quizID) {
        this.quizID = quizID;
    }

    public ArrayList<QuizAttempt> getAttempts() {
        return attempts;
    }

    public void setAttempts(ArrayList<QuizAttempt> attempts) {
        this.attempts = attempts;
    }






}
