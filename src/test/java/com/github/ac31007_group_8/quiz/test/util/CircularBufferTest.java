package com.github.ac31007_group_8.quiz.test.util;

import com.github.ac31007_group_8.quiz.util.CircularBuffer;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for the CircularBuffer used by Database class.
 *
 * @author Robert T.
 */
public class CircularBufferTest {

    public static Object evictionTestObj = null;

    @Test
    public void testNormalAddition() {
        CircularBuffer<Object> buffer = new CircularBuffer<>(2, (obj) -> {});
        buffer.add(new Object());
        assertEquals(1, buffer.size());
        buffer.add(new Object());
        assertEquals(2, buffer.size());
    }

    @Test
    public void testEvictionWithoutFunction() {
        CircularBuffer<Object> buffer = new CircularBuffer<>(1, (obj) -> {});
        buffer.add(new Object());
        buffer.add(new Object());
        assertEquals(1, buffer.size());
    }

    @Test
    public void testEviction() {
        evictionTestObj = null;
        CircularBuffer<Object> buffer = new CircularBuffer<>(1, (obj) -> CircularBufferTest.evictionTestObj = obj);

        Object expected = new Object();
        buffer.add(expected);
        buffer.add(new Object());

        assertEquals(expected, evictionTestObj);
    }

}
