/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ac31007_group_8.quiz.test.staff;

import com.github.ac31007_group_8.quiz.Database;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;

import spark.Request;
import spark.Response;
import spark.Spark;



import com.github.ac31007_group_8.quiz.staff.controllers.QuizList;
import com.github.ac31007_group_8.quiz.staff.models.QuizModel;
import java.util.ArrayList;
import org.jooq.DSLContext;

import static org.junit.Assert.*;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;
import org.powermock.api.mockito.PowerMockito;



import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


/**
 *
 * @author Vlad
 * 
 * some test ideas. Not finished. Not right. Not good. DON'T!
 */



@RunWith(PowerMockRunner.class)
@PrepareForTest({QuizList.class, Database.class}) 

public class QuizListTest {
    

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
//    
//    
//    
//    
//    @Test
//    public void testRootRoute() throws IOException {
//        TestResponse res = makeGetRequest("/staff/quizListFilter?module=AC&year=2017");
//        assertEquals(200, res.status);
//        assertNotNull(res.body); 
//        assertEquals("Hello World!", res.body);
//    }
//    
//        
    
    
    
  
    
    //------------------
    
    
    @Test
    public void moduleCodeIsRequired() throws Exception {
        
        Request req = mock(Request.class);       
        Response res = mock(Response.class);
        
        when(req.queryParams("moduleCode")).thenReturn(null);
        String jsonResp = (String) QuizList.getFilteredQuizList(req, res);
        assertEquals("{\"message\":\"Bad input!\"}",jsonResp);
    }
    
    
    @Test
    public void moduleCodeDoesNotExist() throws Exception {

        
        Request req = mock(Request.class);       
        Response res = mock(Response.class);
        
        
        when(req.queryParams("published")).thenReturn("1");
        when(req.queryParams("moduleCode")).thenReturn("-1000");
        when(req.queryParams("creator")).thenReturn("Iain");
        when(req.queryParams("sortBy")).thenReturn("byTitle");
        
        
        //to do nothing when Database.getJooq is called
        PowerMockito.mockStatic(Database.class);
       
        
       
        QuizModel quizModelMock = PowerMockito.mock(QuizModel.class);
        PowerMockito.when(quizModelMock.getQuizzesFiltered(null,null,null, null,  null)).thenReturn(new ArrayList<>());
        PowerMockito.whenNew(QuizModel.class).withNoArguments().thenReturn(quizModelMock);
        
        
        
        
        String jsonResp = (String) QuizList.getFilteredQuizList(req, res);
        
       
        assertEquals("[]",jsonResp);

    }
    
    
    
    


    
    
    
}




