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
    private boolean isStudentAnswer;
    public String answer;
    public boolean correct;
    
    public Answer(){
        answer_id = 0;
        question_id = 0;
        answer = "";
        correct = true;
        isStudentAnswer = false;    //Assume false by default
    }  
    
    public Answer(int answer_id, int question_id, String answer, boolean correct){
        this.answer_id = answer_id;
        this.question_id = question_id;
        this.answer = answer;
        this.correct = correct;
    } 

    public int getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(int answer_id) {
        this.answer_id = answer_id;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public boolean isStudentAnswer()
    {
        return isStudentAnswer;
    }

    public void setIsStudentAnswer(boolean isStudentAnswer)
    {
        this.isStudentAnswer = isStudentAnswer;
    }




}
