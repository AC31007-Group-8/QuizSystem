/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ac31007_group_8.quiz.staff.store;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author Erik Jeny
 */
public class Quiz {
    
    int quiz_id =0 ;
    int staff_id =0;
    Integer time_limit = null;
    String module_id = "";
    String title= "";
    List<Question> questions = null;
    boolean publish_status = false;
  

  
    
    public Quiz(int staff_id, Integer time_limit, String module_id, String title, boolean publish_status){
        this.staff_id = staff_id;
        this.time_limit = time_limit;
        this.module_id = module_id;
        this.title = title;
        this.publish_status = publish_status;
    }
    
    public Quiz(int quiz_id, int staff_id, Integer time_limit, String module_id, String title, boolean publish_status){
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

    public Integer getTime_limit() {
        return time_limit;
    }

    public void setTime_limit(Integer time_limit) {
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

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
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

    
    
    @Override
    public String toString(){
        
        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonInString = gson.toJson(this);
        return jsonInString;
        
    }
}
