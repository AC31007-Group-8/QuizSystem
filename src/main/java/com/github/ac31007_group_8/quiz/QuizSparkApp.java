package com.github.ac31007_group_8.quiz;


import com.github.ac31007_group_8.quiz.staff.controllers.QuizManager;
import com.github.ac31007_group_8.quiz.student.controllers.QuizManagerStudent;
import com.github.ac31007_group_8.quiz.util.ClasspathInitialiser;
import com.github.ac31007_group_8.quiz.util.Init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.route.RouteOverview;

import static spark.Spark.*;

/**
 * Main entry for all types of launch. Use @link{com.github.ac31007_group_8.quiz.util.Init} for defining routes.
 *
 * @see com.github.ac31007_group_8.quiz.util.Init
 * @author Robert T.
 */
public class QuizSparkApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuizSparkApp.class);

    private QuizSparkApp() {} // Static only

    public static void init() {
        // Setup (do not remove this!)
        Configuration.load();

        staticFiles.location("/public"); // resources/public, localhost:4567/file.html 
        //staticFiles.externalLocation(System.getProperty("user.dir") + "/src/main/resources/public");//if you want a new version of a static file after F5 when developing (better way?)




        // Register all @Init methods.
        ClasspathInitialiser cp = new ClasspathInitialiser(Init.class, "com.github.ac31007_group_8.quiz");
        cp.callAllMethods();

    }

}
