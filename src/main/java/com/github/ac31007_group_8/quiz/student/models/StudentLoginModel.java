package com.github.ac31007_group_8.quiz.student.models;

import com.github.ac31007_group_8.quiz.Database;
import com.github.ac31007_group_8.quiz.staff.store.User;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

import java.util.logging.Logger;

import static com.github.ac31007_group_8.quiz.generated.Tables.STUDENT;

/**
 * Created by Can on 16/03/2017.
 */
public class StudentLoginModel {

    DSLContext dbConnection = null;

    public StudentLoginModel(){
        dbConnection = Database.getJooq(); //Connects to the database
    }

    public boolean isValidLogin(String username, String password)
    {
        try{
            //Refactored to use prepared statements using JOOQ:
            Result<Record> result = dbConnection.select()
                    .from(STUDENT)
                    .where(STUDENT.PASSWORD.equal(password).and(STUDENT.MATRIC_NUMBER.equal(username)))
                    .fetch();

            for(Record r : result){ //Iterates through the returned results
                return true;
            }
        }
        catch(Exception e)
        {
            Logger.getGlobal().info("Exception: " + e.getMessage());
        }
        return false;
    }

    public User getUser(String username, String password)
    {
        //Return a user object with the correct details, but return null if details are not valid.
        if (!isValidLogin(username, password))
        {
            return null;
        }

        try{
            //Refactored to use prepared statements using JOOQ:
            Result<Record> result = dbConnection.select().from(STUDENT).where(STUDENT.PASSWORD.equal(password).and(STUDENT.MATRIC_NUMBER.equal(username))).fetch();

            for(Record r : result){ //Iterates through the returned results - there should only be 1.
                User user = new User("Student " + r.get(STUDENT.MATRIC_NUMBER));
                return user;
            }
        }
        catch(Exception e)
        {
            Logger.getGlobal().info("Exception: " + e.getMessage());
        }
        return null;
    }

}
