package com.github.ac31007_group_8.quiz.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class to convert sets/lists of objects to CSV. Does NOT support nested types and nested collections! This only
 * applies to public fields within the class - private fields will not be indexed.
 *
 * @author Robert T.
 */
public class CSVConverter<T> {

    private static final String NEWLINE = "\r\n";
    private static final String SEPERATOR = ",";

    private final Class<T> type;
    private final List<T> objects;

    public CSVConverter(Class<T> type, List<T> objects) {
        this.type = type;
        this.objects = objects;
    }

    public CSVConverter(Class<T> type, Set<T> objs) {
        this(type, objs.stream().collect(Collectors.toList()));
    }

    /**
     * Converts the objects in this converter to CSV.
     *
     * @return The objects in CSV form.
     */
    public String toCSV() {
        List<Field> fields = getFields();
        StringBuilder builder = new StringBuilder();
        boolean _first = true;
        for (Field it : fields) {
            if (!_first) {
                builder.append(SEPERATOR);
            } else _first = false;
            builder.append(it.getName());
        }
        builder.append(NEWLINE);

        try {
            for (T obj : objects) {
                if (obj == null) continue;
                _first = true;
                for (Field f : fields) {
                    if (!_first) {
                        builder.append(SEPERATOR);
                    } else _first = false;
                    Object value = f.get(obj);
                    if (value != null) {
                        builder.append("\"");
                        builder.append(value.toString());
                        builder.append("\"");
                    }
                }
                builder.append(NEWLINE);
            }
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }

        return builder.toString();
    }

    private List<Field> getFields() {
        Field[] fieldsArr = type.getFields();
        return Arrays.asList(fieldsArr);
    }

}
