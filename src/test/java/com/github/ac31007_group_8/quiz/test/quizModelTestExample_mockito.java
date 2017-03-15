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
import java.util.ArrayList;
import java.util.List;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record8;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.SortField;
import org.jooq.impl.DSL;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import org.mockito.Mockito;
import org.mockito.Matchers;
import static org.junit.Assert.assertFalse;



/**
 *
 * @author Vlad
 */


public class quizModelTestExample_mockito {
    
    
    private DSLContext databaseDeepStub;
    private DSLContext databaseCreateResult;

    @Before
    public void mockDslContext() {
        databaseDeepStub = Mockito.mock(DSLContext.class, Mockito.RETURNS_DEEP_STUBS);//deep_stubs == can use several methods in when
        databaseCreateResult = DSL.using(SQLDialect.MYSQL);
    }
    
    

    @Test
    public void getQuizzesFilteredTest() throws Exception {

    
       
        //create returned data
        Result<Record8<String, String, String, Byte, Integer, Integer, String, String>> resultList = databaseCreateResult.newResult(STAFF.FIRST_NAME,STAFF.SECOND_NAME,MODULE.MODULE_NAME,
                                        QUIZ.PUBLISH_STATUS,QUIZ.QUIZ_ID,QUIZ.TIME_LIMIT,QUIZ.TITLE,QUIZ.MODULE_ID );

        Record8<String, String, String, Byte, Integer, Integer, String, String> r = databaseCreateResult.newRecord(STAFF.FIRST_NAME,STAFF.SECOND_NAME,MODULE.MODULE_NAME,
                                        QUIZ.PUBLISH_STATUS,QUIZ.QUIZ_ID,QUIZ.TIME_LIMIT,QUIZ.TITLE,QUIZ.MODULE_ID );


        r.set(STAFF.FIRST_NAME, "Iain");
        r.set(STAFF.SECOND_NAME, "Murray");
        r.set(MODULE.MODULE_NAME, "Computer systems");
        r.set(QUIZ.PUBLISH_STATUS, (byte)1);
        r.set(QUIZ.QUIZ_ID, 23);
        r.set(QUIZ.TIME_LIMIT, 69);
        r.set(QUIZ.TITLE, "wtf is going on");
        r.set(QUIZ.MODULE_ID, "AC123445");
        resultList.add(r); 


        Mockito.when(databaseDeepStub.select(STAFF.FIRST_NAME,STAFF.SECOND_NAME,MODULE.MODULE_NAME,
                                        QUIZ.PUBLISH_STATUS,QUIZ.QUIZ_ID,QUIZ.TIME_LIMIT,QUIZ.TITLE,QUIZ.MODULE_ID )
                            .from(QUIZ)
                            .join(MODULE).on(QUIZ.MODULE_ID.equal(MODULE.MODULE_ID))
                            .join(STAFF).on(QUIZ.STAFF_ID.equal(STAFF.STAFF_ID))
                            .where(Matchers.<List<Condition>>any())
                            .orderBy(Matchers.<SortField<Object>>any())
                            .fetch())
        .thenReturn(resultList);

      
        QuizModel qm = new QuizModel();

        ArrayList<QuizInfo> allQuizInfo = qm.getQuizzesFiltered(databaseDeepStub, "AC123445", "1", "Iain", "byTitle");


 
        assertFalse(allQuizInfo.isEmpty());

    }
    
    
   
    
    
    
}
