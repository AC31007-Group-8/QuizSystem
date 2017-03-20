package com.github.ac31007_group_8.quiz.test.login;

import com.github.ac31007_group_8.quiz.test.util.TestResponse;
import com.github.ac31007_group_8.quiz.test.util.TestRequest;
import com.github.ac31007_group_8.quiz.QuizSparkApp;
import com.github.ac31007_group_8.quiz.generated.tables.records.StaffRecord;
import com.github.ac31007_group_8.quiz.generated.tables.records.StudentRecord;
import com.github.ac31007_group_8.quiz.staff.controllers.StaffLoginController;
import com.github.ac31007_group_8.quiz.staff.models.StaffLoginModel;
import com.github.ac31007_group_8.quiz.student.models.StudentLoginModel;
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
import static com.github.ac31007_group_8.quiz.generated.Tables.STAFF;

import java.io.IOException;
import java.sql.SQLException;

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
    public void serveLogoutPageTest() {
        boolean exceptionThrown = false;
        TestResponse response = null;
        try {
            response = TestRequest.makeGETRequest("/logout");
        } catch (IOException ex)
        {
            exceptionThrown = true;
            System.out.println("Exception: " + ex.getMessage());
        }
        assertFalse(exceptionThrown);
        assertNotNull(response);
        assertEquals(200, response.status);
        assertNotNull(response.body);
    }

    @Test
    public void serveStudentLoginPageTest() {
        boolean exceptionThrown = false;
        TestResponse response = null;
        try {
            response = TestRequest.makeGETRequest("/student/login");
        } catch (IOException ex)
        {
            exceptionThrown = true;
            System.out.println("Exception: " + ex.getMessage());
        }
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
        assertFalse(exceptionThrown);
        assertNotNull(response);
        assertEquals(200, response.status);
        assertNotNull(response.body);
    }

    @Test
    public void rejectDetailsTest() {
        boolean exceptionThrown = false;
        TestResponse response = null;
        try {
            response = TestRequest.makePOSTRequest("/staff/login", ""); //no parameters, should be denied!
        } catch (IOException ex)
        {
            exceptionThrown = true;
            System.out.println("Exception: " + ex.getMessage());
        }
        assertTrue(exceptionThrown); //should reject
        assertNull(response);
    }

    @Test
    public void acceptStudentLoginDetailsTest() {
        boolean exceptionThrown = false;
        TestResponse response = null;
        try {
            response = TestRequest.makePOSTRequest("/student/login", "username=name&password=pass");
        } catch (IOException ex)
        {
            exceptionThrown = true;
            System.out.println("Exception: " + ex.getMessage());
        }
        assertFalse(exceptionThrown);
        assertNotNull(response);
        assertEquals(200, response.status);
        assertNotNull(response.body);
    }

    @Test
    public void rejectStudentDetailsTest() {
        boolean exceptionThrown = false;
        TestResponse response = null;
        try {
            response = TestRequest.makePOSTRequest("/student/login", ""); //no parameters, should be denied!
        } catch (IOException ex)
        {
            exceptionThrown = true;
            System.out.println("Exception: " + ex.getMessage());
        }
        assertFalse(exceptionThrown); //should reject (changed temporarily by Erik)
        assertNotNull(response);
    }

    @Test
    public void studentRetrievalTest() {

        boolean exceptionThrown = false;
        try {

            // Initialise your data provider (implementation further down):
            MockDataProvider provider = new StudentLoginMockDataProvider();
            MockConnection connection = new MockConnection(provider);

            // Pass the mock connection to a jOOQ DSLContext:
            DSLContext testContext = DSL.using(connection, SQLDialect.MYSQL);

            StudentLoginModel studentLoginModel = new StudentLoginModel(testContext);
            assertTrue(studentLoginModel.isValidLogin("username", "password"));
            assertNotNull(studentLoginModel.getUser("username", "password"));

        } catch (Exception ex)
        {
            exceptionThrown = true;
            System.out.println("Exception: " + ex.getMessage());
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void staffRetrievalTest() {

        boolean exceptionThrown = false;
        try {

            // Initialise your data provider (implementation further down):
            MockDataProvider provider = new StaffLoginMockDataProvider();
            MockConnection connection = new MockConnection(provider);

            // Pass the mock connection to a jOOQ DSLContext:
            DSLContext testContext = DSL.using(connection, SQLDialect.MYSQL);

            StaffLoginModel staffLoginModel = new StaffLoginModel(testContext);
            assertTrue(staffLoginModel.isValidLogin("username", "password"));
            assertNotNull(staffLoginModel.getUser("username", "password"));

        } catch (Exception ex)
        {
            exceptionThrown = true;
            System.out.println("Exception: " + ex.getMessage());
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void StaffOnlyAccessTest() {
        boolean exceptionThrown = false;
        TestResponse response = null;
        try {
            response = TestRequest.makeGETRequest("/staff/testAddress");
        } catch (IOException ex)
        {
            exceptionThrown = true;
            System.out.println("Exception: " + ex.getMessage());
        }
        assertTrue(exceptionThrown); //should reject
        assertNull(response);
    }

    @Test
    public void PublicAccessToLoginPageTest() {
        boolean exceptionThrown = false;
        TestResponse response = null;
        try {
            response = TestRequest.makeGETRequest("/staff/login");
        } catch (IOException ex)
        {
            exceptionThrown = true;
            System.out.println("Exception: " + ex.getMessage());
        }
        assertFalse(exceptionThrown); //should reject
        assertNotNull(response);
    }

}


class StaffLoginMockDataProvider implements MockDataProvider {

    @Override
    public MockResult[] execute(MockExecuteContext ctx) throws SQLException {

        // You might need a DSLContext to create org.jooq.Result and org.jooq.Record objects
        DSLContext create = DSL.using(SQLDialect.MYSQL);
        MockResult[] mock = new MockResult[1];

        // The execute context contains SQL string(s), bind values, and other meta-data
        String sql = ctx.sql();

        // Exceptions are propagated through the JDBC and jOOQ APIs
        if (sql.toUpperCase().startsWith("DROP")) {
            throw new SQLException("Statement not supported: " + sql);
        }

        // You decide, whether any given statement returns results, and how many
        else if (sql.toUpperCase().startsWith("SELECT")) {

            // Always return one author record
            Result<StaffRecord> result = create.newResult(STAFF);
            result.add(create.newRecord(STAFF));
            result.get(0).setValue(STAFF.STAFF_ID, 1);
            result.get(0).setValue(STAFF.FIRST_NAME, "Chris");
            result.get(0).setValue(STAFF.SECOND_NAME, "Samuel");
            mock[0] = new MockResult(1, result);
        }

        return mock;
    }
}



class StudentLoginMockDataProvider implements MockDataProvider {

    @Override
    public MockResult[] execute(MockExecuteContext ctx) throws SQLException {

        // You might need a DSLContext to create org.jooq.Result and org.jooq.Record objects
        DSLContext create = DSL.using(SQLDialect.MYSQL);
        MockResult[] mock = new MockResult[1];

        // The execute context contains SQL string(s), bind values, and other meta-data
        String sql = ctx.sql();

        // Exceptions are propagated through the JDBC and jOOQ APIs
        if (sql.toUpperCase().startsWith("DROP")) {
            throw new SQLException("Statement not supported: " + sql);
        }

        // You decide, whether any given statement returns results, and how many
        else if (sql.toUpperCase().startsWith("SELECT")) {

            // Always return one author record
            Result<StudentRecord> result = create.newResult(STUDENT);
            result.add(create.newRecord(STUDENT));
            result.get(0).setValue(STUDENT.STUDENT_ID, 1);
            result.get(0).setValue(STUDENT.MATRIC_NUMBER, "001112222");
            result.get(0).setValue(STUDENT.EMAIL, "test@test.com");
            mock[0] = new MockResult(1, result);
        }

        return mock;
    }
}