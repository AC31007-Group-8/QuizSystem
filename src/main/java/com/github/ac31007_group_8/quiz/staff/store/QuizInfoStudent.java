/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ac31007_group_8.quiz.staff.store;

/**
 *
 * @author Vlad
 */
public class QuizInfoStudent {
    
    int quizId =0;
    Integer time_limit = null;
    String title = null;
    String module_name = null;
    String module_id = null;
    boolean isRelevant = false;
    boolean isTaken = false;

    
    public QuizInfoStudent(){
        
    }
    
    public QuizInfoStudent(int quizId, Integer time, String title, String moduleName, String module_id, boolean isRelevant,boolean isTaken){
        this.quizId = quizId;
        time_limit = time;
        this.title = title;
        module_name= moduleName;
        this.module_id = module_id;
        this.isRelevant = isRelevant;
        this.isTaken = isTaken;
 
    }
    
    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public Integer getTime_limit() {
        return time_limit;
    }

    public void setTime_limit(Integer time_limit) {
        this.time_limit = time_limit;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getModule_name() {
        return module_name;
    }

    public void setModule_name(String module_name) {
        this.module_name = module_name;
    }

    public String getModule_id() {
        return module_id;
    }

    public void setModule_id(String module_id) {
        this.module_id = module_id;
    }

    public boolean isRelevant() {
        return isRelevant;
    }

    public void setIsRelevant(boolean isRelevant) {
        this.isRelevant = isRelevant;
    }

    public boolean isTaken() {
        return isTaken;
    }

    public void setIsTaken(boolean isTaken) {
        this.isTaken = isTaken;
    }


    
    
    
    
}
