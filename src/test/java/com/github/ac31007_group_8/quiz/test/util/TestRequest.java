package com.github.ac31007_group_8.quiz.test.util;

import java.net.*;
import java.io.IOException;
import java.util.logging.Logger;

import org.apache.commons.io.*;
import java.io.*;

/**
 * Created by Can on 08/03/2017.
 */
public class TestRequest {

    public static TestResponse makeGETRequest(String path) throws IOException {
        String method = "GET";
        URL url = new URL("http://localhost:4567" + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setDoOutput(true);
        connection.connect();
        String body = IOUtils.toString(connection.getInputStream());
        return new TestResponse(connection.getResponseCode(), body);
    }

    public static TestResponse makePOSTRequest(String path, String params) throws IOException {
        String method = "POST";
        URL url = new URL("http://localhost:4567" + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(params);
        wr.flush();
        wr.close();
        ///
        connection.connect();
        String body = IOUtils.toString(connection.getInputStream());
        return new TestResponse(connection.getResponseCode(), body);
    }

}
