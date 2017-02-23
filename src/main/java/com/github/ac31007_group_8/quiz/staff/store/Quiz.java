/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ac31007_group_8.quiz.staff.store;

import java.util.ArrayList;
import com.github.ac31007_group_8.quiz.staff.store.Question;

/**
 *
 * @author Erik Jeny
 */
public class Quiz {
    
    int quiz_id;
    int staff_id;
    int time_limit;
    String module_id;
    String title;
    ArrayList<Question> questions;
    boolean publish_status; 
    
    
    public Quiz(){
        quiz_id = 0;
        staff_id = 0;
        time_limit = 0;
        module_id = "";
        title = "";
        questions = null;
        publish_status = false;
    }
    
    public Quiz(int quiz_id, int staff_id, int time_limit, String module_id, String title, boolean publish_status){
        this.quiz_id = quiz_id;
        this.staff_id = staff_id;
        this.time_limit = time_limit;
        this.module_id = module_id;
        this.title = title;
        this.publish_status = publish_status;
    }

    public int getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(int quiz_id) {
        this.quiz_id = quiz_id;
    }

    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    public int getTime_limit() {
        return time_limit;
    }

    public void setTime_limit(int time_limit) {
        this.time_limit = time_limit;
    }

    public String getModule_id() {
        return module_id;
    }

    public void setModule_id(String module_id) {
        this.module_id = module_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }
    
    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    public boolean isPublish_status() {
        return publish_status;
    }

    public void setPublish_status(boolean publish_status) {
        this.publish_status = publish_status;
    }
    
    
    
    
}
