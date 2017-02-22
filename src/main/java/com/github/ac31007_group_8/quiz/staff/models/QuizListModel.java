/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ac31007_group_8.quiz.staff.models;

import com.github.ac31007_group_8.quiz.Database;
import com.github.ac31007_group_8.quiz.staff.store.Answer;
import com.github.ac31007_group_8.quiz.staff.store.Question;
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
//    }

    public Vector<Quiz> getQuizAll()
    {
        Vector<Quiz> quizzes = new Vector();
        DSLContext create = Database.getJooq(); //Connects to the database

        /**Creates SQL Statement**/
        String sql = create.select()
                        .from(table("quiz"))
                        .getSQL();

        try{

            Result<Record> result = create.fetch(sql);

            for(Record r : result){ //Iterates through the returned results
                Quiz quiz = new Quiz(r.getValue(r.getValue(quiz.module_id), r.getValue(quiz.title));

                quizzes.add(quiz); //Adds current state of the model to the vector array
            }
        }
        catch(Exception e)
        {
            return null;
        }
        return quizzes;
    }
}