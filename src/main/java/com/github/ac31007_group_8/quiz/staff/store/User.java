package com.github.ac31007_group_8.quiz.staff.store;

/**
 * Created by Can on 14/03/2017.
 */
public class User {

    private String name; //The display name of the user.
    private boolean isStaff = false;
    private int userid; //studentID for students, staffID for staff
    private String email; //email (for student results)

    public User(String name, int userid) {
        this.name = name;
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStaff() {
        return isStaff;
    }

    public void setStaff() {
        isStaff = true;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

}
