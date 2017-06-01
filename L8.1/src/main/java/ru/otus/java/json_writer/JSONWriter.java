package ru.otus.java.json_writer;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

/**
 * Converts a given object to JSON string.
 */
public final class JSONWriter {
    private JSONWriter(){}

    public static String toJSON(Object obj) throws IllegalAccessException {
        StringBuilder jsonStr = new StringBuilder();
        Class klass = obj.getClass();
        String klassName = klass.getSimpleName();
        if (klassName.matches("(Integer|Byte|Long|Short|Float|Double|Boolean|Character|String)")) {
            switch (klassName) {
                case "Character":
                case "String":
                    return String.format("\"%s\"", obj.toString());
                default:
                    return obj.toString();

            }
        } else if (klass.isArray()) {
            int length = Array.getLength(obj);
            jsonStr.append("[");
            for (int i = 0; i < length; i++) {
                jsonStr.append(toJSON(Array.get(obj, i)) + ",");
            }
            jsonStr.deleteCharAt(jsonStr.length()-1);
            jsonStr.append("]");
        } else {
            Field[] fields = klass.getDeclaredFields();
            jsonStr.append("{\n");
            for (Field field : fields) {
                field.setAccessible(true);
                jsonStr.append(String.format("\t\"%s\": %s,\n", field.getName(), toJSON(field.get(obj))));
            }
            jsonStr.deleteCharAt(jsonStr.length()-2); // delete trailing comma
            jsonStr.append("}");
        }
        return jsonStr.toString();
    }
}
