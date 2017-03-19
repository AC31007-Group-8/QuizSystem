package com.github.ac31007_group_8.quiz.common;

import spark.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletContext;

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
        addURLParameters(map, req);
        addSessionParameters(map, req);
    }

    public static void addURLParameters(HashMap<String, Object> map, Request req)
    {
      
        ServletContext sc = req.raw().getSession().getServletContext();
        if (sc==null){//jetty
             map.put("baseURL","");
        }
        else{
            map.put("baseURL",sc.getContextPath());//tomcat
        }
    }

    public static String getBaseURL(Request req){
        ServletContext sc = req.raw().getSession().getServletContext();
        if (sc==null){//jetty
            return "";
        }
        else{
            return sc.getContextPath();//tomcat
        }
    }


    public static void addSessionParameters(HashMap<String,Object> map, Request req){
        for (String attribute : req.session().attributes()) {
            map.put(attribute, req.session().attribute(attribute));
        }
    }

    public static void writeMessage(HashMap<String, Object> map, String message){
        List<String> msgs = (List<String>)map.get("messages");
        if (msgs == null) {
            msgs = new ArrayList<String>();
            map.put("messages", msgs); //replace old value
        }
        msgs.add(message);

    }

}
