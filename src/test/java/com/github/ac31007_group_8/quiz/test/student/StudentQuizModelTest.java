///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.github.ac31007_group_8.quiz.test.student;
//
//import com.github.ac31007_group_8.quiz.Database;
//import static com.github.ac31007_group_8.quiz.generated.Tables.MODULE;
//import static com.github.ac31007_group_8.quiz.generated.Tables.QUIZ;
//import static com.github.ac31007_group_8.quiz.generated.Tables.RESULT;
//import static com.github.ac31007_group_8.quiz.generated.Tables.STUDENT;
//
//
//import com.github.ac31007_group_8.quiz.staff.store.QuizInfoStudent;
//import com.github.ac31007_group_8.quiz.student.models.StudentQuizModel;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import org.jooq.DSLContext;
//import org.jooq.Record5;
//import org.jooq.Record6;
//import org.jooq.Result;
//import org.jooq.SQLDialect;
//import org.jooq.impl.DSL;
//import org.jooq.tools.jdbc.MockDataProvider;
//import org.jooq.tools.jdbc.MockExecuteContext;
//import org.jooq.tools.jdbc.MockResult;
//import org.junit.Test;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//import org.junit.Before;
//
///**
// *
// * @author Vlad
// * https://www.jooq.org/doc/3.4/manual/tools/jdbc-mocking/
//   https://blog.jooq.org/2013/02/20/easy-mocking-of-your-database/
// */
//
//class MySpecialProvider implements MockDataProvider {
//
//    public int testCase;
//    private final DSLContext creator = DSL.using(SQLDialect.MYSQL);
//    // Your contract is to return execution results, given a context
//    // object, which contains SQL statement(s), bind values, and some
//    // other context values
//
//    @Override
//    public MockResult[] execute(MockExecuteContext context) throws SQLException {
//
//     
//        if (testCase == 1) {//maps StudentQuizInfo correctly
//            
//            Result<Record5< String, Integer, Integer, String, String>> result = creator.newResult( MODULE.MODULE_NAME,
//                    QUIZ.QUIZ_ID, QUIZ.TIME_LIMIT, QUIZ.TITLE, QUIZ.MODULE_ID);
//
//            Record5< String, Integer, Integer, String, String> r = creator.newRecord( MODULE.MODULE_NAME,
//                    QUIZ.QUIZ_ID, QUIZ.TIME_LIMIT, QUIZ.TITLE, QUIZ.MODULE_ID);
//
//          
//            r.set(MODULE.MODULE_NAME, "Computers");
//            r.set(QUIZ.QUIZ_ID, 2);
//            r.set(QUIZ.TIME_LIMIT, 3);
//            r.set(QUIZ.TITLE, "IainsQuiz");
//            r.set(QUIZ.MODULE_ID, "AC123");
//            result.add(r);
//
//            return new MockResult[]{
//                new MockResult(1, result)
//            };
//        }
//        else if (testCase==2){//returns record
//            Result<Record5< String, Integer, Integer, String, String>> result = creator.newResult(MODULE.MODULE_NAME,
//                     QUIZ.QUIZ_ID, QUIZ.TIME_LIMIT, QUIZ.TITLE, QUIZ.MODULE_ID);
//
//            Record5< String, Integer, Integer, String, String> r = creator.newRecord( MODULE.MODULE_NAME,
//                     QUIZ.QUIZ_ID, QUIZ.TIME_LIMIT, QUIZ.TITLE, QUIZ.MODULE_ID);
//            result.add(r);
//            return new MockResult[]{
//                new MockResult(1, result)
//            };
//        }
//        else if (testCase==3){//sorts
//            Result<Record5< String, Integer, Integer, String, String>> result = creator.newResult(MODULE.MODULE_NAME,
//                    QUIZ.QUIZ_ID, QUIZ.TIME_LIMIT, QUIZ.TITLE, QUIZ.MODULE_ID);
//
//            Record5< String, Integer, Integer, String, String> r1 = creator.newRecord( MODULE.MODULE_NAME,
//                   QUIZ.QUIZ_ID, QUIZ.TIME_LIMIT, QUIZ.TITLE, QUIZ.MODULE_ID);
//            r1.set(QUIZ.TITLE, "Aaa");
//           
//            
//            Record5< String, Integer, Integer, String, String> r2 = creator.newRecord(MODULE.MODULE_NAME,
//                    QUIZ.QUIZ_ID, QUIZ.TIME_LIMIT, QUIZ.TITLE, QUIZ.MODULE_ID);
//            r2.set(QUIZ.TITLE, "Bbb");
//          
//            Record5< String, Integer, Integer, String, String> r3 = creator.newRecord( MODULE.MODULE_NAME,
//                    QUIZ.QUIZ_ID, QUIZ.TIME_LIMIT, QUIZ.TITLE, QUIZ.MODULE_ID);
//            r3.set(QUIZ.TITLE, "Ccc");
//           
//            result.add(r1);
//            result.add(r2);
//            result.add(r3);
//            
//            return new MockResult[]{ new MockResult(1, result) };
//        }
//        else if (testCase==4){//filters 
//            Result<Record5< String, Integer, Integer, String, String>> result = creator.newResult( MODULE.MODULE_NAME,
//                     QUIZ.QUIZ_ID, QUIZ.TIME_LIMIT, QUIZ.TITLE, QUIZ.MODULE_ID);
//
//            Record5< String, Integer, Integer, String, String> r1 = creator.newRecord( MODULE.MODULE_NAME,
//                    QUIZ.QUIZ_ID, QUIZ.TIME_LIMIT, QUIZ.TITLE, QUIZ.MODULE_ID);
//            r1.set(QUIZ.MODULE_ID, "A123");
//            Record5< String, Integer, Integer, String, String> r2 = creator.newRecord( MODULE.MODULE_NAME,
//                   QUIZ.QUIZ_ID, QUIZ.TIME_LIMIT, QUIZ.TITLE, QUIZ.MODULE_ID);
//            r2.set(QUIZ.MODULE_ID, "A1234");
//            Record5< String, Integer, Integer, String, String> r3 = creator.newRecord( MODULE.MODULE_NAME,
//                    QUIZ.QUIZ_ID, QUIZ.TIME_LIMIT, QUIZ.TITLE, QUIZ.MODULE_ID);
//            r3.set(QUIZ.MODULE_ID, "A1234");
//            
//           
//          
//            Object[] bindings = context.bindings();
//
//            if (bindings.length==0){//-1 passed
//                result.add(r1);
//                result.add(r2);
//                result.add(r3);
//            }
//            else if (((String)bindings[0]).equals("A123")){
//                result.add(r1);
//            }
//            else if(((String)bindings[0]).equals("A1234")){
//                result.add(r2);
//                result.add(r3);
//            }
//            else {/*no matches*/    }
//            
//            return new MockResult[]{ new MockResult(1, result) };
//        }
//        else {
//            return null;
//        }
//    }
//};
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//public class StudentQuizModelTest {
//    
//    MySpecialProvider provider = new MySpecialProvider();
//    DSLContext dslCont;
//    StudentQuizModel qm; 
//    
//    @Before
//    public void init(){
//        Database.setMockProvider(provider);
//        dslCont = Database.getJooq();
//        qm = new StudentQuizModel();
//    }
//    
//
//    
//    @Test
//    public void getFilteredQuizInfoTest_QuizInfoMappedCorrectly() throws Exception {
//        
//        provider.testCase = 1;//test quizInfo assembled with correct values
//        ArrayList<QuizInfoStudent> allQi = qm.getFilteredQuizInfo(dslCont, "AC12345", "byTitle", "isTaken","isRelevant", 1);
//        assertEquals(allQi.get(0).getModule_name(),"Computers");
//        assertTrue(allQi.get(0).isRelevant());//this is not returned by query,but set by method depending on passed parameters!
//        assertTrue(allQi.get(0).isTaken());// since passed isTaken and isRelevant expect true
//        assertEquals(allQi.get(0).getQuizId(),2);
//        assertEquals(allQi.get(0).getTime_limit(),(Integer) 3);
//        assertEquals(allQi.get(0).getTitle(),"IainsQuiz");
//        assertEquals(allQi.get(0).getModule_id(),"AC123");
//        
//    }
//    
//    
//    @Test
//    public void getFilteredQuizInfoTest_recordAddedToList() throws Exception {
//        provider.testCase = 2;//test record returned
//        ArrayList<QuizInfoStudent> allQi = qm.getFilteredQuizInfo(dslCont, "", "", "", "byStatus",1);
//        assertEquals(allQi.size(),1);
//    }
//    
//    
//    
//    
//    
//    
//    @Test
//    public void getFilteredQuizInfoTest_sortsResults() throws Exception {
//        provider.testCase = 3;//sorts
//        ArrayList<QuizInfoStudent> allQi = qm.getFilteredQuizInfo(dslCont, "", "", "", "byTitle",1);
//        for (int i=0;i<allQi.size()-1;i++){
//            assertTrue(allQi.get(i).getTitle().compareTo(allQi.get(i+1).getTitle())<=0);
//        }
//        
//       
//    }
//    
//    
//    @Test
//    public void getFilteredQuizInfoTest_filtersByModuleCode() throws Exception {
//        provider.testCase = 4;//filters by module_id
//        
//        ArrayList<QuizInfoStudent> allQi = qm.getFilteredQuizInfo(dslCont, "doesNotExist", "", "", "",1);
//        assertTrue(allQi.isEmpty());
//        
//        allQi = qm.getFilteredQuizInfo(dslCont, "A123", "", "", "",1);
//        assertTrue(allQi.size()==1);
//        
//        allQi = qm.getFilteredQuizInfo(dslCont, "A1234","", "", "",1);
//        assertTrue(allQi.size()==2);
//        
//        allQi = qm.getFilteredQuizInfo(dslCont, "-1","", "", "",1);
//        assertTrue(allQi.size()==3);
//
//    }
//    
//}
