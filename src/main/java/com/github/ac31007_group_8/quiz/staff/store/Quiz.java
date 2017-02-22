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
public class Quiz {
    
    int quiz_id;
    int staff_id;
    int time_limit;
    String module_id;
    String title;
    boolean publish_status; 
    
    
    public Quiz(){
        quiz_id = 0;
        staff_id = 0;
        time_limit = 0;
        module_id = "";
        title = "";
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
    
    
    
}
