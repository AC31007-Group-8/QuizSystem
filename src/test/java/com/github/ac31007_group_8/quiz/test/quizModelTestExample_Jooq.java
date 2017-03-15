/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ac31007_group_8.quiz.test;

import static com.github.ac31007_group_8.quiz.generated.Tables.MODULE;
import static com.github.ac31007_group_8.quiz.generated.Tables.QUIZ;
import static com.github.ac31007_group_8.quiz.generated.Tables.STAFF;

import com.github.ac31007_group_8.quiz.staff.models.QuizModel;
import com.github.ac31007_group_8.quiz.staff.store.QuizInfo;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import org.jooq.DSLContext;
import org.jooq.Record8;
import org.jooq.Result;
import org.jooq.SQLDialect;
import static org.jooq.SQLDialect.MYSQL;
import org.jooq.impl.DSL;
import org.jooq.tools.jdbc.MockConnection;
import org.jooq.tools.jdbc.MockDataProvider;
import org.jooq.tools.jdbc.MockExecuteContext;
import org.jooq.tools.jdbc.MockResult;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.Before;

/**
 *
 * @author Vlad
 * https://www.jooq.org/doc/3.4/manual/tools/jdbc-mocking/
   https://blog.jooq.org/2013/02/20/easy-mocking-of-your-database/
 */

class MySpecialProvider implements MockDataProvider {

    public int testCase;
    private final DSLContext creator = DSL.using(SQLDialect.MYSQL);
    // Your contract is to return execution results, given a context
    // object, which contains SQL statement(s), bind values, and some
    // other context values

    @Override
    public MockResult[] execute(MockExecuteContext context) throws SQLException {

        System.out.println(context.sql());
        if (testCase == 1) {
            
            Result<Record8<String, String, String, Byte, Integer, Integer, String, String>> result = creator.newResult(STAFF.FIRST_NAME, STAFF.SECOND_NAME, MODULE.MODULE_NAME,
                    QUIZ.PUBLISH_STATUS, QUIZ.QUIZ_ID, QUIZ.TIME_LIMIT, QUIZ.TITLE, QUIZ.MODULE_ID);

            Record8<String, String, String, Byte, Integer, Integer, String, String> r = creator.newRecord(STAFF.FIRST_NAME, STAFF.SECOND_NAME, MODULE.MODULE_NAME,
                    QUIZ.PUBLISH_STATUS, QUIZ.QUIZ_ID, QUIZ.TIME_LIMIT, QUIZ.TITLE, QUIZ.MODULE_ID);

            r.set(STAFF.FIRST_NAME, "Iain");
            result.add(r);

            return new MockResult[]{
                new MockResult(1, result)
            };
        } else {
            return null;
        }
    }
};




public class quizModelTestExample_Jooq {
    
    MySpecialProvider provider = new MySpecialProvider();
    DSLContext dslCont;
    QuizModel qm; 
    
    @Before
    public void init(){
        Connection connection = new MockConnection(provider);
        dslCont = DSL.using(connection, MYSQL);
        qm = new QuizModel();
    }
    

    
    
    
    
    @Test
    public void getQuizzesFilteredTest2() throws Exception {
        
        
        provider.testCase = 1;
        ArrayList<QuizInfo> allQi = qm.getQuizzesFiltered(dslCont, "AC12345", "1", "Iain", "byTitle");

        assertEquals(1, allQi.size());

    }
    
    
}
