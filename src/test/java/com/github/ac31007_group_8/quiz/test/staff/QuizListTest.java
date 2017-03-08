/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ac31007_group_8.quiz.test.staff;

import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;

import static org.junit.Assert.*;
import spark.Request;
import spark.Spark;



import com.github.ac31007_group_8.quiz.staff.controllers.QuizList;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import spark.utils.IOUtils;

/**
 *
 * @author Vlad
 */
public class QuizListTest extends Request{
    

    
    @BeforeClass
    public static void beforeClass() {
      
        QuizList.init();
        Spark.awaitInitialization();
    }
    
    @AfterClass
    public static void afterClass() {
        Spark.stop();
    }
    
    
    
    private TestResponse makePostRequest(String path, String body) throws IOException {
        
        URL url = new URL("http://localhost:4567" + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.addRequestProperty("Content-Type", "application/" + "POST");
        connection.setRequestProperty("Content-Length", Integer.toString(body.length()));
        connection.getOutputStream().write(body.getBytes("UTF8"));//add request body
        connection.connect();
        
        
        
        return new TestResponse(connection.getResponseCode());
    }
    
    
    private TestResponse makeGetRequest(String path) throws IOException {
        URL url = new URL("http://localhost:4567" + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        //connection.setDoInput(true);
        
        
        connection.connect();
        String responseBody = IOUtils.toString(connection.getInputStream());
        return new TestResponse(connection.getResponseCode(), responseBody);
    }
    
    
    private static class TestResponse {
        public final String body;
        public final int status;

        public TestResponse(int status, String body) {
            this.status = status;
            this.body = body;
        }
        
        
        public TestResponse(int status) {
            this.status = status;
            body=null;
        }
    }
    
    
    
    
    
    //tests :)
    
    
    //1) get no parameters --> returns body with all quizzes
    //2) get module=AC --> return body where module is AC only
    //3) 
    
    
    
    //------------------
    
     @Test
    public void testBadModuleParameter() throws IOException {
        TestResponse res = makeGetRequest("/staff/quizListFilter?module=----&year=2017");
        assertEquals(200, res.status);
        assertNotNull(res.body); 
        assertEquals("Hello World!", res.body);
    }
    
    
    
    
    @Test
    public void testRootRoute() throws IOException {
        TestResponse res = makeGetRequest("/staff/quizListFilter?module=AC&year=2017");
        assertEquals(200, res.status);
        assertNotNull(res.body); 
        assertEquals("Hello World!", res.body);
    }
    
    
    @Test
    public void testVladCanDoTests() {
       
    }
    
    
    
}




