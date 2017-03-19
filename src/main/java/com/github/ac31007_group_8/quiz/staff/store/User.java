package com.github.ac31007_group_8.quiz.staff.store;

/**
 * Created by Can on 14/03/2017.
 */
public class User {

    String name; //The display name of the user.
    boolean isStaff = false;
    String userid; //studentID for students, staffID for staff
    String email; //email (for student results)

    public User(String name, String userid){
        this.name = name;
        this.userid = userid;
    }

    public void setStaff(){
        isStaff = true;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public boolean isStaff(){
        return isStaff;
    }
    public String getName(){
        return name;
    }
}