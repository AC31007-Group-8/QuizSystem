package com.github.ac31007_group_8.quiz.test.util;

/**
 * Created by Can on 08/03/2017.
 */

public class TestResponse {
    public final String body;
    public final int status;

    public TestResponse(int status, String body) {
        this.status = status;
        this.body = body;
    }
}
