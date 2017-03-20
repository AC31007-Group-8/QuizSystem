/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ac31007_group_8.quiz.test.staff;

import com.github.ac31007_group_8.quiz.Database;
import com.github.ac31007_group_8.quiz.QuizSparkApp;
import com.github.ac31007_group_8.quiz.generated.tables.records.QuizRecord;
import com.github.ac31007_group_8.quiz.generated.tables.records.StaffRecord;
import com.github.ac31007_group_8.quiz.staff.controllers.QuizList;
import com.github.ac31007_group_8.quiz.staff.models.QuizModel;
import com.github.ac31007_group_8.quiz.staff.models.StaffLoginModel;
import com.github.ac31007_group_8.quiz.staff.store.Quiz;
import com.github.ac31007_group_8.quiz.staff.store.QuizInfo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.github.ac31007_group_8.quiz.staff.store.User;
import com.github.ac31007_group_8.quiz.student.models.StudentQuizModel;
import com.github.ac31007_group_8.quiz.test.util.TestRequest;
import com.github.ac31007_group_8.quiz.test.util.TestResponse;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;

import org.jooq.impl.DSL;
import org.jooq.tools.jdbc.MockConnection;
import org.jooq.tools.jdbc.MockDataProvider;
import org.jooq.tools.jdbc.MockExecuteContext;
import org.jooq.tools.jdbc.MockResult;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.github.ac31007_group_8.quiz.generated.Tables.QUIZ;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import spark.Request;
import spark.Response;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import org.mockito.Mock;
import spark.Spark;

/**
 *
 * @author Vlad
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({QuizList.class, Database.class})
public class QuizListTest {
    private Request req ;      
    private Response res ;

    @BeforeClass
    public static void setUpClass() {

        QuizSparkApp.init();
        Spark.awaitInitialization();

    }

    @AfterClass
    public static void tearDownClass() {

        Spark.stop();

    }

    @Before
    public void init() throws Exception{
        PowerMockito.mockStatic(Database.class);//to do nothing when Database.getJooq is called
        
        req = mock(Request.class);       
        res = mock(Response.class);
        when(req.queryParams("published")).thenReturn("1");
        when(req.queryParams("moduleCode")).thenReturn("AC1234");
        when(req.queryParams("creator")).thenReturn("Iain");
        when(req.queryParams("sortBy")).thenReturn("byTitle");
        
        PowerMockito.whenNew(QuizModel.class).withNoArguments().thenReturn(quizModelMock);
    }
    
    
    @Mock
    private  QuizModel quizModelMock ;
    
    
    @Test
    public void moduleCodeIsRequired() throws Exception {
        when(req.queryParams("moduleCode")).thenReturn(null);
        String jsonResp = (String) QuizList.getFilteredQuizList(req, res);
        assertEquals("{\"message\":\"Bad input!\"}",jsonResp);
        when(req.queryParams("moduleCode")).thenReturn("AC1234");//set back
    }
    
    @Test
    public void publishStatusIsRequired() throws Exception {
        when(req.queryParams("published")).thenReturn(null);
        String jsonResp = (String) QuizList.getFilteredQuizList(req, res);
        assertEquals("{\"message\":\"Bad input!\"}",jsonResp);
        when(req.queryParams("published")).thenReturn("1");
    }
    
    @Test
    public void creatorIsRequired() throws Exception {
        when(req.queryParams("creator")).thenReturn(null);
        String jsonResp = (String) QuizList.getFilteredQuizList(req, res);
        assertEquals("{\"message\":\"Bad input!\"}",jsonResp);
        when(req.queryParams("creator")).thenReturn("Iain");
    }
    
    @Test
    public void sortByIsRequired() throws Exception {
       
        when(req.queryParams("sortBy")).thenReturn(null);
        String jsonResp = (String) QuizList.getFilteredQuizList(req, res);
        assertEquals("{\"message\":\"Bad input!\"}",jsonResp);
        when(req.queryParams("sortBy")).thenReturn("byTitle");
    }
    
    
    
    
    
    @Test
    public void moduleCodeDoesNotExist() throws SQLException {        
       
        PowerMockito.when(quizModelMock.getQuizzesFiltered(any(DSLContext.class),eq("AC1234"),eq("1"), eq("Iain"), eq("byTitle"))).thenReturn(new ArrayList<>());
                
        String jsonResp = (String) QuizList.getFilteredQuizList(req, res);
        assertEquals("[]",jsonResp);

    }
    
 
    @Test
    public void returnsQuizInfoJson() throws SQLException {        
       
        ArrayList<QuizInfo> allQi = new ArrayList<>();
        allQi.add(new QuizInfo(1,2,"Computers","Archi","AC",true,"Iain","Murray"));
      
        PowerMockito.when(quizModelMock.getQuizzesFiltered(any(DSLContext.class),eq("AC1234"),eq("1"), eq("Iain"), eq("byTitle"))).thenReturn(allQi);
       
        String jsonResp = (String) QuizList.getFilteredQuizList(req, res);
        
        assertEquals("[{\"quizId\":1,\"time_limit\":2,\"title\":\"Computers\",\"module_name\":\"Archi\",\"module_id\":\"AC\",\"publish_status\":true,\"first_name\":\"Iain\",\"second_name\":\"Murray\"}]", jsonResp);
    }
    
    @Test
    public void modelThrowsExceptionTest() throws DataAccessException {        
       
        PowerMockito.when(quizModelMock.getQuizzesFiltered(any(DSLContext.class),eq("AC1234"),eq("1"), eq("Iain"), eq("byTitle"))).thenThrow(new DataAccessException("Exception!"));
                
        String jsonResp = (String) QuizList.getFilteredQuizList(req, res);
        assertEquals("{\"message\":\"Exception occured\"}",jsonResp);

    }
    
   @Test
    public void serveQuizPreviewPage(){
       boolean exceptionThrown = false;
       TestResponse response = null;
       try {
           response = TestRequest.makeGETRequest("/staff/previewQuiz");
       } catch (IOException ex)
       {
           exceptionThrown = true;
           System.out.println("Exception: " + ex.getMessage());
       }
       assertTrue(exceptionThrown); //Expected status code 401, which should throw an exception
       assertNull(response);
   }

    @Test
    public void PublishStatusChangeResponseTest() {
        boolean exceptionThrown = false;
        TestResponse response = null;
        try {
            response = TestRequest.makePOSTRequest("/staff/changePublishStatus", "quizID=-1&publishTarget=false");
        } catch (IOException ex)
        {
            exceptionThrown = true;
            System.out.println("Exception: " + ex.getMessage());
        }
        assertTrue(exceptionThrown);
        assertNull(response);
    }

    @Test
    public void PublishStatusModelUpdateTest() {

        // Initialise your data provider (implementation further down):
        MockDataProvider provider = new PublishStatusMockDataProvider();
        MockConnection connection = new MockConnection(provider);

        // Pass the mock connection to a jOOQ DSLContext:
        DSLContext testContext = DSL.using(connection, SQLDialect.MYSQL);

        StudentQuizModel quizModel = new StudentQuizModel();

        //Make sure it's false initially
        Quiz quiz = quizModel.getQuiz(1, testContext);
        assertNotNull(quiz);
        assertFalse(quiz.isPublish_status());

        QuizModel quizWriteModel = new QuizModel();

        boolean exceptionThrown = false;
        try {
            quizWriteModel.setPublishStatus(testContext, 1, true);
            ((PublishStatusMockDataProvider)provider).setUpdated();
        } catch (Exception ex) {
            exceptionThrown = true;
            System.out.println("Exception: " + ex.getMessage());
        }
        assertFalse(exceptionThrown);

        //Then ensure that its publish status was changed to true.
        quiz = quizModel.getQuiz(1, testContext);
        assertTrue(quiz.isPublish_status());


    }

    
}


class PublishStatusMockDataProvider implements MockDataProvider {

    private static boolean wasUpdated = false;

    private MockResult[] getResult(DSLContext create)
    {
        MockResult[] mock = new MockResult[1];
        Result<QuizRecord> result = create.newResult(QUIZ);
        if (!wasUpdated)
        {
            result.add(create.newRecord(QUIZ));
            result.get(0).setValue(QUIZ.QUIZ_ID, 1);
            result.get(0).setValue(QUIZ.STAFF_ID, 1);
            result.get(0).setValue(QUIZ.MODULE_ID, "AC11111");
            result.get(0).setValue(QUIZ.TITLE, "Test Quiz");
            result.get(0).setValue(QUIZ.TIME_LIMIT, 100);
            result.get(0).setValue(QUIZ.PUBLISH_STATUS, (byte)0);
        } else {
            result.add(create.newRecord(QUIZ));
            result.get(0).setValue(QUIZ.QUIZ_ID, 1);
            result.get(0).setValue(QUIZ.STAFF_ID, 1);
            result.get(0).setValue(QUIZ.MODULE_ID, "AC11111");
            result.get(0).setValue(QUIZ.TITLE, "Test Quiz");
            result.get(0).setValue(QUIZ.TIME_LIMIT, 100);
            result.get(0).setValue(QUIZ.PUBLISH_STATUS, (byte)1);
        }

        mock[0] = new MockResult(1, result);
        return mock;
    }

    @Override
    public MockResult[] execute(MockExecuteContext ctx) throws SQLException {
        System.out.println("WasUpdated: " + wasUpdated);
        // You might need a DSLContext to create org.jooq.Result and org.jooq.Record objects
        DSLContext create = DSL.using(SQLDialect.MYSQL);
        MockResult[] mock = new MockResult[1];

        // The execute context contains SQL string(s), bind values, and other meta-data
        String sql = ctx.sql();

        System.out.println("Query: " + ctx.sql());

        if (sql.toUpperCase().startsWith("SELECT")) {
            mock = getResult(create);
        }
        else if (sql.toUpperCase().startsWith("UPDATE")) {
            setUpdated();
        }

        return mock;
    }

    public void setUpdated(){
        wasUpdated = true;
    }
}
