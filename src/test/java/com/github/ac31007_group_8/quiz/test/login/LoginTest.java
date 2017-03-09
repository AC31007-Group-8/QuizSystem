package com.github.ac31007_group_8.quiz.test.login;

import com.github.ac31007_group_8.quiz.QuizSparkApp;
import com.github.ac31007_group_8.quiz.staff.controllers.StaffLoginController;
import org.junit.*;
import spark.Request;
import spark.Response;
import spark.Spark;
import com.github.ac31007_group_8.quiz.util.*;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by Can on 07/03/2017.
 */
public class LoginTest {



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
    public void serveLoginPageTest() {
        boolean exceptionThrown = false;
        TestResponse response = null;
        try {
            response = TestRequest.makeGETRequest("/staff/login");
        } catch (IOException ex)
        {
            exceptionThrown = true;
            System.out.println("Exception: " + ex.getMessage());
        }
        //response = new TestResponse(200, "hi");
        assertFalse(exceptionThrown);
        assertNotNull(response);
        assertEquals(200, response.status);
        assertNotNull(response.body);
    }

    @Test
    public void acceptLoginDetailsTest() {
        boolean exceptionThrown = false;
        TestResponse response = null;
        try {
            response = TestRequest.makePOSTRequest("/staff/login", "username=name&password=pass");
        } catch (IOException ex)
        {
            exceptionThrown = true;
            System.out.println("Exception: " + ex.getMessage());
        }
        //response = new TestResponse(200, "hi");
        assertFalse(exceptionThrown);
        assertNotNull(response);
        assertEquals(200, response.status);
        assertNotNull(response.body);
    }

}
