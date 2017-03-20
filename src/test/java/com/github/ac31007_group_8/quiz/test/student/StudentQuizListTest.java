/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ac31007_group_8.quiz.test.student;

import com.github.ac31007_group_8.quiz.Database;
import com.github.ac31007_group_8.quiz.common.ParameterManager;
import com.github.ac31007_group_8.quiz.staff.models.QuizModel;
import com.github.ac31007_group_8.quiz.staff.store.QuizInfoStudent;
import com.github.ac31007_group_8.quiz.staff.store.User;


import com.github.ac31007_group_8.quiz.student.controllers.StudentQuizList;
import com.github.ac31007_group_8.quiz.student.models.StudentQuizModel;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import spark.Request;
import spark.Response;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.doAnswer;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 *
 * @author Vlad
 */



@RunWith(PowerMockRunner.class)
@PrepareForTest({StudentQuizList.class, Database.class, ParameterManager.class}) 
public class StudentQuizListTest {
    
    private Request req ;      
    private Response res ;
    private HashMap<String, Object> sessionMap = new HashMap<>();
    @Before
    public void init() throws Exception{
        PowerMockito.mockStatic(Database.class);//to do nothing when Database.getJooq is called
        
        PowerMockito.mockStatic(ParameterManager.class);
        User u =new User("a@a.com",1);
        sessionMap.put("user",u);
        when(ParameterManager.getAllParameters(any(Request.class))).thenReturn(sessionMap);
        
        req = mock(Request.class);       
        res = mock(Response.class);
        
        when(req.queryParams("moduleCode")).thenReturn("AC1234");
        when(req.queryParams("sortBy")).thenReturn("byTitle");//byTitle, byCode, byName, any
        when(req.queryParams("taken")).thenReturn("isTaken");//isTaken, notTaken, any
        when(req.queryParams("relevant")).thenReturn("isRelevant");//isRelevant, notRelevant, any
        
        PowerMockito.whenNew(StudentQuizModel.class).withNoArguments().thenReturn(studentQuizModelMock);
        PowerMockito.whenNew(QuizModel.class).withNoArguments().thenReturn(quizModelMock);
        
        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                          
                when(res.status()).thenReturn((int)invocation.getArguments()[0]);
                return null;
            }
        }).when(res).status(anyInt());
    }
    
    
    @Mock
    private  StudentQuizModel studentQuizModelMock ;
    
    @Mock
    private QuizModel quizModelMock;
    
    
    //---------------------------RELEVANT ---------------------------
    @Test
    public void relevant_statusOk() throws Exception{
        
        PowerMockito.when(studentQuizModelMock.getRelevantQuizzes(anyInt(), any(Connection.class))).thenReturn(new ArrayList<>());
        
        StudentQuizList.sendRelevantQuizzes(req, res);
        assertEquals(res.status(),200);
    }
    
    
    
    @Test
    public void relevant_hasUserInSession() throws Exception{
 
        when(ParameterManager.getAllParameters(any(Request.class))).thenReturn(new HashMap<>());//empty hash map
        PowerMockito.when(studentQuizModelMock.getRelevantQuizzes(anyInt(), any(Connection.class))).thenReturn(new ArrayList<>());
        assertNull( StudentQuizList.sendRelevantQuizzes(req, res));
        assertEquals(res.status(),401);
        
        when(ParameterManager.getAllParameters(any(Request.class))).thenReturn(sessionMap);//set back
    }
    
    @Test
    public void relevant_responseNotNull() throws Exception {
        assertNotNull(StudentQuizList.sendRelevantQuizzes(req, res));
    }
    
    @Test
    public void relevant_catchesSQLException() throws Exception{
 
        
        PowerMockito.when(studentQuizModelMock.getRelevantQuizzes(anyInt(), any(Connection.class))).thenThrow(new SQLException("Exception!"));
        
        assertNull(StudentQuizList.sendRelevantQuizzes(req, res));
        assertEquals(res.status(),500);
        
        
    }
    
    
    
    //---------------------------FILTERED ---------------------------
    @Test
    public void filtered_statusOk() throws Exception{
        
        StudentQuizList.getFilteredQuizList(req, res);
        assertEquals(res.status(),200);
    }
    
    
    
    @Test
    public void filtered_responseNotNull() throws Exception {
        assertNotNull(StudentQuizList.getFilteredQuizList(req, res));
    }
    
    
    @Test
    public void filtered_moduleCodeIsRequired() throws Exception {
        when(req.queryParams("moduleCode")).thenReturn(null);
        String jsonResp = (String) StudentQuizList.getFilteredQuizList(req, res);
        assertEquals("{\"message\":\"Bad input!\"}",jsonResp);
        when(req.queryParams("moduleCode")).thenReturn("AC1234");//set back
    }
    
    @Test
    public void filtered_relevantIsRequired() throws Exception {
        when(req.queryParams("relevant")).thenReturn(null);
        String jsonResp = (String) StudentQuizList.getFilteredQuizList(req, res);
        assertEquals("{\"message\":\"Bad input!\"}",jsonResp);
        when(req.queryParams("relevant")).thenReturn("yes");
    }
    
    
    @Test
    public void filtered_sortByIsRequired() throws Exception {
       
        when(req.queryParams("sortBy")).thenReturn(null);
        String jsonResp = (String) StudentQuizList.getFilteredQuizList(req, res);
        assertEquals("{\"message\":\"Bad input!\"}",jsonResp);
        when(req.queryParams("sortBy")).thenReturn("byTitle");
    }
    
    
    
    
    
    @Test
    public void moduleCodeDoesNotExist() throws SQLException {        
       
        PowerMockito.when(studentQuizModelMock.getFilteredQuizInfo(any(DSLContext.class),eq("AC1234"), eq("byTitle"), eq("isTaken"), eq("isRelevant"), anyInt())).thenReturn(new ArrayList<>());
                
        String jsonResp = (String) StudentQuizList.getFilteredQuizList(req, res);
        assertEquals("[]",jsonResp);

    }
    
 
    @Test
    public void returnsQuizInfoJson() throws SQLException {        
       
        ArrayList<QuizInfoStudent> allQi = new ArrayList<>();
        allQi.add(new QuizInfoStudent(1,2,"Computers","Archi","AC",true,true));
     
        
        
        PowerMockito.when(studentQuizModelMock.getFilteredQuizInfo(any(DSLContext.class),eq("AC1234"), eq("byTitle"), eq("isTaken"), eq("isRelevant"), anyInt())).thenReturn(allQi);
       
        String jsonResp = (String) StudentQuizList.getFilteredQuizList(req, res);
        assertEquals("[{\"quizId\":1,\"time_limit\":2,\"title\":\"Computers\",\"module_name\":\"Archi\",\"module_id\":\"AC\",\"isRelevant\":true,\"isTaken\":true}]", jsonResp);
      
    
    }
    
    @Test
    public void modelThrowsExceptionTest() throws DataAccessException {        
       
        PowerMockito.when(studentQuizModelMock.getFilteredQuizInfo(any(DSLContext.class),eq("AC1234"), eq("byTitle"), eq("isTaken"), eq("isRelevant"), anyInt())).thenThrow(new DataAccessException("Exception!"));
                
        String jsonResp = (String) StudentQuizList.getFilteredQuizList(req, res);
        assertEquals("{\"message\":\"Exception occured\"}",jsonResp);

    }
    
    
    
}
