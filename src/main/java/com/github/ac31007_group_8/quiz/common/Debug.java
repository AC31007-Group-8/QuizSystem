package com.github.ac31007_group_8.quiz.common;

import com.github.ac31007_group_8.quiz.util.Init;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.route.RouteOverview;

import static spark.Spark.get;

/**
 * Debug routes.
 *
 * @author Robert T.
 */
public class Debug {

    private Debug() {} // Static

    private static final Logger LOGGER = LoggerFactory.getLogger(Debug.class);

    @Init
    public static void init() {
        LOGGER.debug("Registering debug routes");
        RouteOverview.enableRouteOverview("/debug/routes");
        get("/debug/ping", (req, res) -> "Pong!");
    }

}
