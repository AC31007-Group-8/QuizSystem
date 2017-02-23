/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ac31007_group_8.quiz.staff.store;

import java.util.ArrayList;
import com.github.ac31007_group_8.quiz.staff.store.Answer;

/**
 *
 * @author Erik Jeny
 */
public class Question {
    
    int question_id;
    int quiz_id;
    ArrayList<Answer> answers;
    String question;
    String explanation;
    
    public Question(){
        question_id = 0;
        quiz_id = 0;
        question = "";
        explanation = "";
    }
    
    public Question(int question_id, int quiz_id, String question, String explanation){
        this.question_id = question_id;
        this.quiz_id = quiz_id;
        this.question = question;
        this.explanation = explanation;
    }

    public int getQuestionID(){
        return question_id;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public int getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(int quiz_id) {
        this.quiz_id = quiz_id;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }
    
    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
    
    
}
