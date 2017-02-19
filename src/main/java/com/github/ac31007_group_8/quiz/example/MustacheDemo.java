package com.github.ac31007_group_8.quiz.example;

import static spark.Spark.*;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

/**
 * Sample class to demonstrate Mustache.
 *
 * @author Robert T.
 */
public class MustacheDemo {

    private MustacheDemo() {} // Static

    public static void init() {
        get("/example", MustacheDemo::handle);
    }

    private static Object handle(Request req, Response res) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("val", "example");
        map.put("list", new Object[]{"test", "more test"});
        TemplateEngine eng = new MustacheTemplateEngine();
        return eng.render(eng.modelAndView(map, "example.mustache"));
    }
}