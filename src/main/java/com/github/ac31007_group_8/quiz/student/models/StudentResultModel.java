package com.github.ac31007_group_8.quiz.student.models;

import com.github.ac31007_group_8.quiz.Database;

import org.jooq.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.github.ac31007_group_8.quiz.generated.Tables.*;
/**
 *
 * @author Kerr & Allan
 */
public class StudentResultModel {

    DSLContext dbConnection = null;

    public StudentResultModel(){
        dbConnection = Database.getJooq(); //Connects to the database
    }


    public List<Integer> getResultAnswers(int resultID)
    {
        List<Integer> resultAnswerIds = new ArrayList<Integer>();
        try{
            Result<Record> result = dbConnection.select()
                .from(RESULT_TO_ANSWER)
                .where(RESULT_TO_ANSWER.RESULT_ID.equal(resultID))
                .orderBy(RESULT_TO_ANSWER.ANSWER_ID.asc())
                .fetch();

            for(Record r : result){ //Iterates through the returned results
                resultAnswerIds.add(r.get(RESULT_TO_ANSWER.ANSWER_ID));
            }
            return resultAnswerIds;
        }
        catch(Exception e)
        {
            Logger.getGlobal().info("Exception: " + e.getMessage());
        }

        return resultAnswerIds;
    }
}
