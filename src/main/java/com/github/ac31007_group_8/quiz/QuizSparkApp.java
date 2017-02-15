package com.github.ac31007_group_8.quiz;

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
        // Debugging paths:
        LOGGER.debug("Registering debug routes");
        RouteOverview.enableRouteOverview("/debug/routes");
        get("/debug/ping", (req, res) -> "Pong!");

        // Add more routes below here.
        // ---------------------------
    }

}
