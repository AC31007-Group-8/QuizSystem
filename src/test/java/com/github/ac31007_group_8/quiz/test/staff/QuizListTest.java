/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ac31007_group_8.quiz.test.staff;

import com.github.ac31007_group_8.quiz.Database;
import com.github.ac31007_group_8.quiz.staff.controllers.QuizList;
import com.github.ac31007_group_8.quiz.staff.models.QuizModel;
import com.github.ac31007_group_8.quiz.staff.store.QuizInfo;
import java.sql.SQLException;
import java.util.ArrayList;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import static org.junit.Assert.assertEquals;
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
import static org.mockito.Matchers.eq;
import org.mockito.Mock;

/**
 *
 * @author Vlad
 */



@RunWith(PowerMockRunner.class)
@PrepareForTest({QuizList.class, Database.class}) 
public class QuizListTest {
    
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
    
    
    
}
