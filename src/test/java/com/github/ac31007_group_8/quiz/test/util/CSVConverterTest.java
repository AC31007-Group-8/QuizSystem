package com.github.ac31007_group_8.quiz.test.util;

import com.github.ac31007_group_8.quiz.util.CSVConverter;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for CSV converter.
 *
 * @author Robert T.
 */
public class CSVConverterTest {

    private final static String EXPECTED_SINGLE_OBJ = "a,b\r\n\"1\",\"test\"\r\n";
    private final static String EXPECTED_MULTIPLE_OBJS = "a,b\r\n\"1\",\"first\"\r\n\"2\",\"second\"\r\n";
    private final static String EXPECTED_NULL_FIELD = "a,b\r\n\"1\",\r\n";

    @Test
    public void testConvertSingleObject() {
        List<CSVTestCase> objs = new LinkedList<>();
        objs.add(new CSVTestCase(1, "test", 2));
        CSVConverter<CSVTestCase> converter = new CSVConverter<>(CSVTestCase.class, objs);
        String out = converter.toCSV();
        assertEquals(EXPECTED_SINGLE_OBJ, out);
    }

    @Test
    public void testConvertMultipleObject() {
        List<CSVTestCase> objs = new LinkedList<>();
        objs.add(new CSVTestCase(1, "first", -1));
        objs.add(new CSVTestCase(2, "second", -1));
        CSVConverter<CSVTestCase> converter = new CSVConverter<>(CSVTestCase.class, objs);
        String out = converter.toCSV();
        assertEquals(EXPECTED_MULTIPLE_OBJS, out);
    }

    @Test
    public void testNullFields() {
        List<CSVTestCase> objs = new LinkedList<>();
        objs.add(new CSVTestCase(1, null, -1));
        CSVConverter<CSVTestCase> converter = new CSVConverter<>(CSVTestCase.class, objs);
        String out = converter.toCSV();
        assertEquals(EXPECTED_NULL_FIELD, out);
    }

    @Test
    public void testNullObjects() {
        List<CSVTestCase> objs = new LinkedList<>();
        objs.add(null);
        objs.add(new CSVTestCase(1, "test", 2));
        CSVConverter<CSVTestCase> converter = new CSVConverter<>(CSVTestCase.class, objs);
        String out = converter.toCSV();
        assertEquals(EXPECTED_SINGLE_OBJ, out);
    }

    @SuppressWarnings("WeakerAccess")
    public static class CSVTestCase {
        public final int a;
        public final String b;

        private final int priv;

        public CSVTestCase(int a, String b, int priv) {
            this.a = a;
            this.b = b;
            this.priv = priv;
        }
    }

}
