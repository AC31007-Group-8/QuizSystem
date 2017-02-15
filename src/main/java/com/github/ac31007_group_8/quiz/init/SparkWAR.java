package com.github.ac31007_group_8.quiz.init;

import com.github.ac31007_group_8.quiz.QuizSparkApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.servlet.SparkApplication;

/**
 * Main entrypoint for when launching on a servlet server such as Tomcat. Don't edit this!
 *
 * @author Robert T.
 */
@SuppressWarnings("unused")
public class SparkWAR implements SparkApplication {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void init() {
        LOGGER.debug("init()");
        LOGGER.info("Starting Spark in Servlet mode.");
        QuizSparkApp.init();
    }

    @Override
    public void destroy() {
        LOGGER.debug("destroy()");
    }

}
