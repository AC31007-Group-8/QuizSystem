package com.github.ac31007_group_8.quiz.test.util.cp_test;

import com.github.ac31007_group_8.quiz.util.Init;

/**
 * Testing stub for ClasspathInitialiser tests.
 *
 * @author Robert T.
 */
public class ClasspathInitTest {

    public static int calls = 0;

    @Init
    public static void handle() {
        calls += 1;
    }

}
