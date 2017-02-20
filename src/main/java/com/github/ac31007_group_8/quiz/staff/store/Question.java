/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ac31007_group_8.quiz.staff.store;

/**
 *
 * @author Erik Jeny
 */
public class Question {
    
    int question_id;
    int quiz_id;
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
    
}
