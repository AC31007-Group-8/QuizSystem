/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ac31007_group_8.quiz.test;

import com.github.ac31007_group_8.quiz.Database;
import org.junit.Test;

import spark.Request;
import spark.Response;



import com.github.ac31007_group_8.quiz.staff.controllers.QuizList;
import com.github.ac31007_group_8.quiz.staff.models.QuizModel;
import com.github.ac31007_group_8.quiz.staff.store.QuizInfo;
import java.sql.SQLException;
import java.util.ArrayList;
import org.jooq.DSLContext;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import org.powermock.api.mockito.PowerMockito;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


/**
 *
 * @author Vlad
 * 
 */



@RunWith(PowerMockRunner.class)
@PrepareForTest({QuizList.class, Database.class}) 

public class QuizListTest_Example {
    
//TO MAKE A REAL CONNECTION, but I don't
//    
//    @BeforeClass
//    public static void beforeClass() {
//      
//        QuizList.init();
//        Spark.awaitInitialization();
//    }
//    
//    @AfterClass
//    public static void afterClass() {
//        Spark.stop();
//    }
    
    
    
//    private TestResponse makePostRequest(String path, String body) throws IOException {
//        
//        URL url = new URL("http://localhost:4567" + path);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("POST");
//        connection.setDoOutput(true);
//        connection.addRequestProperty("Content-Type", "application/" + "POST");
//        connection.setRequestProperty("Content-Length", Integer.toString(body.length()));
//        connection.getOutputStream().write(body.getBytes("UTF8"));//add request body
//        connection.connect();
//        
//        
//        
//        return new TestResponse(connection.getResponseCode());
//    }
//    
//    
//    private TestResponse makeGetRequest(String path) throws IOException {
//        URL url = new URL("http://localhost:4567" + path);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("GET");
//        //connection.setDoInput(true);
//        
//        
//        connection.connect();
//        String responseBody = IOUtils.toString(connection.getInputStream());
//        return new TestResponse(connection.getResponseCode(), responseBody);
//    }
//    
//    
//    private static class TestResponse {
//        public final String body;
//        public final int status;
//
//        public TestResponse(int status, String body) {
//            this.status = status;
//            this.body = body;
//        }
//        
//        
//        public TestResponse(int status) {
//            this.status = status;
//            body=null;
//        }
//    }
//     @Test
//    public void testBadModuleParameter() throws IOException {
//        TestResponse res = makeGetRequest("/staff/quizListFilter?module=----&year=2017");
//        assertEquals(200, res.status);
//        assertNotNull(res.body); 
//        assertEquals("Hello World!", res.body);
//    }
//    @Test
//    public void testRootRoute() throws IOException {
//        TestResponse res = makeGetRequest("/staff/quizListFilter?module=AC&year=2017");
//        assertEquals(200, res.status);
//        assertNotNull(res.body); 
//        assertEquals("Hello World!", res.body);
//    }
//    
//        
    
    
    
  
    private Request req ;      
    private Response res ;
    
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
    public void moduleCodeDoesNotExist() throws SQLException{        
       
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
    public void modelThrowsExceptionTest() throws SQLException {        
       
        PowerMockito.when(quizModelMock.getQuizzesFiltered(any(DSLContext.class),eq("AC1234"),eq("1"), eq("Iain"), eq("byTitle"))).thenThrow(new SQLException());
                
        String jsonResp = (String) QuizList.getFilteredQuizList(req, res);
        assertEquals("{\"message\":\"Exception occured\"}",jsonResp);

    }
    
}










