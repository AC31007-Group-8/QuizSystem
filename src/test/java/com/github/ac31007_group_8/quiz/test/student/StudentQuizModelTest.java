/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ac31007_group_8.quiz.test.student;

import com.github.ac31007_group_8.quiz.Database;
import static com.github.ac31007_group_8.quiz.generated.Tables.MODULE;
import static com.github.ac31007_group_8.quiz.generated.Tables.QUIZ;
import static com.github.ac31007_group_8.quiz.generated.Tables.STAFF;


import com.github.ac31007_group_8.quiz.staff.store.QuizInfoStudent;
import com.github.ac31007_group_8.quiz.student.models.StudentQuizModel;
import java.sql.SQLException;
import java.util.ArrayList;
import org.jooq.DSLContext;
import org.jooq.Record7;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.tools.jdbc.MockDataProvider;
import org.jooq.tools.jdbc.MockExecuteContext;
import org.jooq.tools.jdbc.MockResult;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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

     
        if (testCase == 1) {//maps StudentQuizInfo correctly
            
            Result<Record7<String, String, String, Integer, Integer, String, String>> result = creator.newResult(STAFF.FIRST_NAME, STAFF.SECOND_NAME, MODULE.MODULE_NAME,
                    QUIZ.QUIZ_ID, QUIZ.TIME_LIMIT, QUIZ.TITLE, QUIZ.MODULE_ID);

            Record7<String, String, String, Integer, Integer, String, String> r = creator.newRecord(STAFF.FIRST_NAME, STAFF.SECOND_NAME, MODULE.MODULE_NAME,
                    QUIZ.QUIZ_ID, QUIZ.TIME_LIMIT, QUIZ.TITLE, QUIZ.MODULE_ID);

            r.set(STAFF.FIRST_NAME, "Iain");
            r.set(STAFF.SECOND_NAME, "Murray");
            r.set(MODULE.MODULE_NAME, "Computers");
            r.set(QUIZ.QUIZ_ID, 2);
            r.set(QUIZ.TIME_LIMIT, 3);
            r.set(QUIZ.TITLE, "IainsQuiz");
            r.set(QUIZ.MODULE_ID, "AC123");
            result.add(r);

            return new MockResult[]{
                new MockResult(1, result)
            };
        }
        else if (testCase==2){//returns record
            Result<Record7<String, String, String, Integer, Integer, String, String>> result = creator.newResult(STAFF.FIRST_NAME, STAFF.SECOND_NAME, MODULE.MODULE_NAME,
                     QUIZ.QUIZ_ID, QUIZ.TIME_LIMIT, QUIZ.TITLE, QUIZ.MODULE_ID);

            Record7<String, String, String, Integer, Integer, String, String> r = creator.newRecord(STAFF.FIRST_NAME, STAFF.SECOND_NAME, MODULE.MODULE_NAME,
                     QUIZ.QUIZ_ID, QUIZ.TIME_LIMIT, QUIZ.TITLE, QUIZ.MODULE_ID);
            result.add(r);
            return new MockResult[]{
                new MockResult(1, result)
            };
        }
        else if (testCase==3){//sorts
            Result<Record7<String, String, String, Integer, Integer, String, String>> result = creator.newResult(STAFF.FIRST_NAME, STAFF.SECOND_NAME, MODULE.MODULE_NAME,
                    QUIZ.QUIZ_ID, QUIZ.TIME_LIMIT, QUIZ.TITLE, QUIZ.MODULE_ID);

            Record7<String, String, String, Integer, Integer, String, String> r1 = creator.newRecord(STAFF.FIRST_NAME, STAFF.SECOND_NAME, MODULE.MODULE_NAME,
                   QUIZ.QUIZ_ID, QUIZ.TIME_LIMIT, QUIZ.TITLE, QUIZ.MODULE_ID);
            r1.set(QUIZ.TITLE, "Aaa");
           
            
            Record7<String, String, String, Integer, Integer, String, String> r2 = creator.newRecord(STAFF.FIRST_NAME, STAFF.SECOND_NAME, MODULE.MODULE_NAME,
                    QUIZ.QUIZ_ID, QUIZ.TIME_LIMIT, QUIZ.TITLE, QUIZ.MODULE_ID);
            r2.set(QUIZ.TITLE, "Bbb");
          
            Record7<String, String, String, Integer, Integer, String, String> r3 = creator.newRecord(STAFF.FIRST_NAME, STAFF.SECOND_NAME, MODULE.MODULE_NAME,
                    QUIZ.QUIZ_ID, QUIZ.TIME_LIMIT, QUIZ.TITLE, QUIZ.MODULE_ID);
            r3.set(QUIZ.TITLE, "Ccc");
           
            result.add(r1);
            result.add(r2);
            result.add(r3);
            
            return new MockResult[]{ new MockResult(1, result) };
        }
        else if (testCase==4){//filters 
            Result<Record7<String, String, String, Integer, Integer, String, String>> result = creator.newResult(STAFF.FIRST_NAME, STAFF.SECOND_NAME, MODULE.MODULE_NAME,
                     QUIZ.QUIZ_ID, QUIZ.TIME_LIMIT, QUIZ.TITLE, QUIZ.MODULE_ID);

            Record7<String, String, String, Integer, Integer, String, String> r1 = creator.newRecord(STAFF.FIRST_NAME, STAFF.SECOND_NAME, MODULE.MODULE_NAME,
                    QUIZ.QUIZ_ID, QUIZ.TIME_LIMIT, QUIZ.TITLE, QUIZ.MODULE_ID);
            r1.set(QUIZ.MODULE_ID, "A123");
            Record7<String, String, String, Integer, Integer, String, String> r2 = creator.newRecord(STAFF.FIRST_NAME, STAFF.SECOND_NAME, MODULE.MODULE_NAME,
                   QUIZ.QUIZ_ID, QUIZ.TIME_LIMIT, QUIZ.TITLE, QUIZ.MODULE_ID);
            r2.set(QUIZ.MODULE_ID, "A1234");
            Record7<String, String, String, Integer, Integer, String, String> r3 = creator.newRecord(STAFF.FIRST_NAME, STAFF.SECOND_NAME, MODULE.MODULE_NAME,
                    QUIZ.QUIZ_ID, QUIZ.TIME_LIMIT, QUIZ.TITLE, QUIZ.MODULE_ID);
            r3.set(QUIZ.MODULE_ID, "A1234");
            
           
          
            Object[] bindings = context.bindings();

            if (bindings.length==0){//-1 passed
                result.add(r1);
                result.add(r2);
                result.add(r3);
            }
            else if (((String)bindings[0]).equals("A123")){
                result.add(r1);
            }
            else if(((String)bindings[0]).equals("A1234")){
                result.add(r2);
                result.add(r3);
            }
            else {/*no matches*/    }
            
            return new MockResult[]{ new MockResult(1, result) };
        }
        else {
            return null;
        }
    }
};















public class StudentQuizModelTest {
    
    MySpecialProvider provider = new MySpecialProvider();
    DSLContext dslCont;
    StudentQuizModel qm; 
    
    @Before
    public void init(){
        Database.setMockProvider(provider);
        dslCont = Database.getJooq();
        qm = new StudentQuizModel();
    }
    

    
    @Test
    public void getFilteredQuizInfoTest_QuizInfoMappedCorrectly() throws Exception {
        
        provider.testCase = 1;//test quizInfo assembled with correct values
        ArrayList<QuizInfoStudent> allQi = qm.getFilteredQuizInfo(dslCont, "AC12345", "Iain", "byTitle", "isTaken","isRelevant");
        assertEquals(allQi.get(0).getFirst_name(),"Iain");
        assertEquals(allQi.get(0).getSecond_name(),"Murray");
        assertEquals(allQi.get(0).getModule_name(),"Computers");
        assertTrue(allQi.get(0).isRelevant());//this is not returned by query,but set by method depending on passed parameters!
        assertTrue(allQi.get(0).isTaken());// since passed isTaken and isRelevant expect true
        assertEquals(allQi.get(0).getQuizId(),2);
        assertEquals(allQi.get(0).getTime_limit(),(Integer) 3);
        assertEquals(allQi.get(0).getTitle(),"IainsQuiz");
        assertEquals(allQi.get(0).getModule_id(),"AC123");
        
    }
    
    
    @Test
    public void getFilteredQuizInfoTest_recordAddedToList() throws Exception {
        provider.testCase = 2;//test record returned
        ArrayList<QuizInfoStudent> allQi = qm.getFilteredQuizInfo(dslCont, "", "", "", "", "byStatus");
        assertEquals(allQi.size(),1);
    }
    
    
    
    
    
    
    @Test
    public void getFilteredQuizInfoTest_sortsResults() throws Exception {
        provider.testCase = 3;//sorts
        ArrayList<QuizInfoStudent> allQi = qm.getFilteredQuizInfo(dslCont, "", "", "", "", "byTitle");
        for (int i=0;i<allQi.size()-1;i++){
            assertTrue(allQi.get(i).getTitle().compareTo(allQi.get(i+1).getTitle())<=0);
        }
        
       
    }
    
    
    @Test
    public void getFilteredQuizInfoTest_filtersByModuleCode() throws Exception {
        provider.testCase = 4;//filters by module_id
        
        ArrayList<QuizInfoStudent> allQi = qm.getFilteredQuizInfo(dslCont, "doesNotExist","", "", "", "");
        assertTrue(allQi.isEmpty());
        
        allQi = qm.getFilteredQuizInfo(dslCont, "A123","", "", "", "");
        assertTrue(allQi.size()==1);
        
        allQi = qm.getFilteredQuizInfo(dslCont, "A1234", "","", "", "");
        assertTrue(allQi.size()==2);
        
        allQi = qm.getFilteredQuizInfo(dslCont, "-1", "","", "", "");
        assertTrue(allQi.size()==3);

    }
    
}
