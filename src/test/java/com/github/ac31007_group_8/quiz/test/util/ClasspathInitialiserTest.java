package com.github.ac31007_group_8.quiz.test.util;

import com.github.ac31007_group_8.quiz.test.util.cp_test.ClasspathInitTest;
import com.github.ac31007_group_8.quiz.util.ClasspathInitialiser;
import com.github.ac31007_group_8.quiz.util.Init;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for Classpath Initialiser.
 *
 * @author Robert T.
 */
public class ClasspathInitialiserTest {

    @Test
    public void testInit() {
        ClasspathInitialiser init = new ClasspathInitialiser(Init.class,
                "com.github.ac31007_group_8.quiz.test.util.cp_test");
        ClasspathInitTest.calls = 0;
        init.callAllMethods();
        assertEquals(1, ClasspathInitTest.calls);
        init.callAllMethods();
        assertEquals(2, ClasspathInitTest.calls);
    }

}
