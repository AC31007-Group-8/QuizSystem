package com.github.ac31007_group_8.quiz.student.controllers;

import com.github.ac31007_group_8.quiz.common.ParameterManager;
import com.github.ac31007_group_8.quiz.student.models.ResultListModel;
import com.github.ac31007_group_8.quiz.util.Init;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;
import spark.template.mustache.MustacheTemplateEngine;

import java.lang.reflect.Array;
import java.util.*;
import java.util.logging.Logger;

import com.github.ac31007_group_8.quiz.student.*;
import com.github.ac31007_group_8.quiz.staff.store.*;

import static spark.Spark.*;

/**
 * Created by Callum on 18-Mar-17.
 */
public class ResultList {

    public ResultList(){
    }
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ResultList.class);

    @Init
    public static void init() {
        get("/student/resultList", ResultList::parseResultList);
    }

    public static Object parseResultList(Request req, Response res){

        User user = req.session().attribute("user");
        int studentID = user.getUserid();

        TemplateEngine eng = new MustacheTemplateEngine();

        ResultListModel resultModel = new ResultListModel();

        StudentResults studResults = resultModel.getCompleteQuizzes(studentID);
        HashMap<String, Object> map = ParameterManager.getAllParameters(req);
        map.put("studentResults", studResults);

        return eng.render(eng.modelAndView(map, "resultList.mustache"));
    }


}
