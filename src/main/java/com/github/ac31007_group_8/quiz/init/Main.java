package com.github.ac31007_group_8.quiz.init;

import com.github.ac31007_group_8.quiz.QuizSparkApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entrypoint for running using the standard Spark embedded server. Don't edit this!
 *
 * @author Robert T
 */
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOGGER.info("Starting Spark embedded server at http://localhost:4567/");
        LOGGER.info("For current routes, see http://localhost:4567/debug/routes");
        QuizSparkApp.init();
    }

}
