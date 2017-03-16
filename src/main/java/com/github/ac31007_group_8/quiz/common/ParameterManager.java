package com.github.ac31007_group_8.quiz.common;

import spark.Request;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by Can on 12/03/2017.
 */
public class ParameterManager {

    public static HashMap<String, Object> getAllParameters(Request req)
    {
        HashMap<String, Object> map = new HashMap<>();
        addAllParameters(map, req);

        return map;
    }

    public static void addAllParameters(HashMap<String, Object> map, Request req)
    {
        addURLParameters(map);
        addSessionParameters(map, req);
    }

    public static void addURLParameters(HashMap<String, Object> map)
    {
        map.put("baseURL", "http://localhost:4567");
    }


    public static void addSessionParameters(HashMap<String,Object> map, Request req){
        for (String attribute : req.session().attributes()) {
            map.put(attribute, req.session().attribute(attribute));
        }
    }

}
