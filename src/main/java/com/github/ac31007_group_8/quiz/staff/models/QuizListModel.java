/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ac31007_group_8.quiz.staff.models;

import com.github.ac31007_group_8.quiz.Database;

import com.github.ac31007_group_8.quiz.staff.store.Quiz;
import org.jooq.*;

import org.apache.commons.lang3.tuple.*;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import static com.github.ac31007_group_8.quiz.generated.Tables.*;

/**
 *
 * @author Callum N
 */
public class QuizListModel {

    public QuizListModel(){
    }

    public Quiz getQuizAll()
    {
        DSLContext create = Database.getJooq(); //Connects to the database

        /**Creates SQL Statement**/
        String sql = create.select()
                        .from(QUIZ)
                        .getSQL();

        try{

            Result<Record> result = create.fetch(sql);

            for(Record r : result){ //Iterates through the returned results
                Quiz quiz = new Quiz(r.get(QUIZ.QUIZ_ID),r.get(QUIZ.STAFF_ID), r.get(QUIZ.TIME_LIMIT),
                        r.get(QUIZ.MODULE_ID), r.get(QUIZ.TITLE), r.get(QUIZ.PUBLISH_STATUS)!=0);   //!=0 Converts a single bit to boolean
                return quiz;
            }
        }
        catch(Exception e)
        {
            Logger.getGlobal().info("Exception: " + e.getMessage());
        }
        return null;

    }
}