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
public class QuizInfo {
    
    int quizId =0;
    Integer time_limit = null;
    String title = null;
    String module_name = null;
    String module_id = null;
    boolean publish_status = false;
    String first_name = null;
    String second_name = null;

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getTimeLimit() {
        return time_limit;
    }

    public void setTimeLimit(int timeLimit) {
        this.time_limit = timeLimit;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getModuleName() {
        return module_name;
    }

    public void setModuleName(String moduleName) {
        this.module_name = moduleName;
    }

    public String getModuleCode() {
        return module_id;
    }

    public void setModuleCode(String moduleCode) {
        this.module_id = moduleCode;
    }

    public boolean isIsPublished() {
        return publish_status;
    }

    public void setIsPublished(boolean isPublished) {
        this.publish_status = isPublished;
    }

    public Integer getTime_limit() {
        return time_limit;
    }

    public void setTime_limit(Integer time_limit) {
        this.time_limit = time_limit;
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

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getSecond_name() {
        return second_name;
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
    }

    
    
    
    @Override
    public String toString() {
      return "id: "+quizId+" time: "+time_limit+" title: "+title+" moduleName: "+module_name+" moduleCode: "+module_id+" isPublished: "+publish_status+" creatorName: "+first_name+" "+second_name;
    }
    
    
    
}
