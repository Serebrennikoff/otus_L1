package ru.otus.tester.assertions;

/**
 * Set of methods for asserting not null value.
 */
public class AssertNotNull {
    public static void assertNotNull(Object obj) {
        if(obj == null) throw new RuntimeException("Not null assertion failed");
    }

    public static void assertNotNull(Object obj, String message) {
        if(obj == null) throw new RuntimeException("Not null assertion failed: " + message);
    }
}
