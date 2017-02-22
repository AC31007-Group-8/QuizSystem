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
public class Answer {
    
    public int answer_id;
    public int question_id;
    public String answer;
    public boolean correct;
    
    public Answer(){
        answer_id = 0;
        question_id = 0;
        answer = "";
        correct = true;
    }  
    
    public Answer(int answer_id, int question_id, String answer, boolean correct){
        this.answer_id = answer_id;
        this.question_id = question_id;
        this.answer = answer;
        this.correct = correct;
    } 
    
}
