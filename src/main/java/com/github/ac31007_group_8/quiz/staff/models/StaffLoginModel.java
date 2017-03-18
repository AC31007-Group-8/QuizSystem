package com.github.ac31007_group_8.quiz.staff.models;

import com.github.ac31007_group_8.quiz.Database;
import com.github.ac31007_group_8.quiz.generated.tables.Staff;
import com.github.ac31007_group_8.quiz.staff.store.Quiz;
import com.github.ac31007_group_8.quiz.staff.store.User;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

import java.util.logging.Logger;

import static com.github.ac31007_group_8.quiz.generated.Tables.*;

/**
 * Created by Can on 10/03/2017.
 */
public class StaffLoginModel {

    DSLContext dbConnection = null;

    public StaffLoginModel(){
        dbConnection = Database.getJooq(); //Connects to the database
    }
    public StaffLoginModel(DSLContext dslContext)
    {
        dbConnection = dslContext;
    }

    public boolean isValidLogin(String username, String password)
    {
        try{
            //Refactored to use prepared statements using JOOQ:
            Result<Record> result = dbConnection.select()
                    .from(STAFF)
                    .where(STAFF.PASSWORD.equal(password).and(STAFF.STAFF_NUMBER.equal(username)))
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
            Result<Record> result = dbConnection.select().from(STAFF).where(STAFF.PASSWORD.equal(password).and(STAFF.STAFF_NUMBER.equal(username))).fetch();

            for(Record r : result){ //Iterates through the returned results - there should only be 1.
                User user = new User(r.get(STAFF.FIRST_NAME) + " " + r.get(STAFF.SECOND_NAME), username);
                user.setStaff();
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
