package com.github.ac31007_group_8.quiz.staff.store;

/**
 * Created by Can on 14/03/2017.
 */
public class User {

    String username;
    boolean isStaff = false;

    public User(String username){
        this.username = username;
    }
    public void setStaff(){
        isStaff = true;
    }
}
