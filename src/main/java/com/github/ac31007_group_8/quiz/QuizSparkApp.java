package com.github.ac31007_group_8.quiz;

import com.github.ac31007_group_8.quiz.example.MustacheDemo;
import com.github.ac31007_group_8.quiz.staff.controllers.QuizManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.route.RouteOverview;

import static spark.Spark.*;

/**
 * Main entry for all types of launch. Define routes here!
 *
 * @author Robert T.
 */
public class QuizSparkApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuizSparkApp.class);

    private QuizSparkApp() {} // Static only

    public static void init() {
        // Setup (do not remove this!)
        Configuration.load();

        //staticFiles.location("/public"); // resources/public, localhost:4567/file.html
        staticFiles.externalLocation(System.getProperty("user.dir") + "/src/main/resources/public");
        
        // Debugging paths:
        LOGGER.debug("Registering debug routes");
        RouteOverview.enableRouteOverview("/debug/routes");
        
       
        
        get("/debug/ping", (req, res) -> "Pong!");

        get("/staff/createQuiz", QuizManager::sendQuizForm);
        post("/staff/createQuiz","application/json", QuizManager::saveQuiz);
        
        
        // Add more routes below here.
        // ---------------------------

        // Example module
        MustacheDemo.init();
    }

}
