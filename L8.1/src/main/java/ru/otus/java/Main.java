package ru.otus.java;

import ru.otus.java.json_writer.JSONWriter;
import ru.otus.java.sample_objects.Group;
import ru.otus.java.sample_objects.PrimitiveContainer;
import ru.otus.java.sample_objects.Student;

import javax.json.*;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * ...
 */
public class Main {
    public static void main(String[] args) throws Exception {
        PrimitiveContainer obj = new PrimitiveContainer();
        System.out.println(JSONWriter.toJSON(obj));
        System.out.println("==========================================");

        Student alex = new Student("Alex", "Ley", 19, new String[]{"math40", "cs50", "cs51"}, 'B');
        Student kate = new Student("Kate", "Blanch", 21, new String[]{"cs50", "nw10"}, 'A');
        Student billy = new Student("Billy", "Thornton", 21, new String[]{"math40", "cs51", "nw10"}, 'C');

        Group group01 = new Group(109, new Student[]{alex, billy, kate});

        String json = JSONWriter.toJSON(group01);
        System.out.println(json);
        System.out.println("==========================================");

        JsonReader r = Json.createReader(new StringReader(json));
        JsonStructure jsonst = r.read();

        navigateTree(jsonst, "base");
        System.out.println("==========================================");

        String jsonData = writeToString((JsonObject) jsonst);
        System.out.println(jsonData);
    }

    private static String writeToString(JsonObject jsonst) {
        StringWriter stWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stWriter)) {
            jsonWriter.writeObject(jsonst);
        }

        return stWriter.toString();
    }

    private static void navigateTree(JsonValue tree, String key) {
        if (key != null)
            System.out.print("Key " + key + ": ");
        switch (tree.getValueType()) {
            case OBJECT:
                System.out.println("OBJECT");
                JsonObject object = (JsonObject) tree;
                for (String name : object.keySet())
                    navigateTree(object.get(name), name);
                break;
            case ARRAY:
                System.out.println("ARRAY");
                JsonArray array = (JsonArray) tree;
                for (JsonValue val : array)
                    navigateTree(val, null);
                break;
            case STRING:
                JsonString st = (JsonString) tree;
                System.out.println("STRING " + st.getString());
                break;
            case NUMBER:
                JsonNumber num = (JsonNumber) tree;
                System.out.println("NUMBER " + num.toString());
                break;
            case TRUE:
            case FALSE:
            case NULL:
                System.out.println(tree.getValueType().toString());
                break;
        }
    }
}
