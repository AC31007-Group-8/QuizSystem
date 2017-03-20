package com.github.ac31007_group_8.quiz.test.resultList;

import com.github.ac31007_group_8.quiz.test.util.TestResponse;
import com.github.ac31007_group_8.quiz.test.util.TestRequest;
import com.github.ac31007_group_8.quiz.QuizSparkApp;
import com.github.ac31007_group_8.quiz.generated.tables.records.StudentRecord;
import com.github.ac31007_group_8.quiz.student.controllers.ResultList;
import com.github.ac31007_group_8.quiz.student.models.ResultListModel;
import org.junit.*;
import spark.Request;
import spark.Response;
import spark.Spark;
import com.github.ac31007_group_8.quiz.util.*;
import org.jooq.tools.jdbc.MockConnection;
import org.jooq.tools.jdbc.MockDataProvider;
import org.jooq.impl.DSL;
import org.jooq.*;
import org.jooq.tools.jdbc.*;
import static com.github.ac31007_group_8.quiz.generated.Tables.STUDENT;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.*;
/**
 * Created by Callum on 20-Mar-17.
 */
public class ResultListTest {

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
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void serveResultPageTest(){
    boolean exceptionThrown = false;
    TestResponse response = null;
        try {
        response = TestRequest.makeGETRequest("/student/resultList");
    } catch (IOException ex)
    {
        exceptionThrown = true;
        System.out.println("Exception: " + ex.getMessage());
    }
    //response = new TestResponse(200, "hi");
    assertTrue(exceptionThrown);
    assertNull(response);
    }
}