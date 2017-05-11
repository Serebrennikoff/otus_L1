package ru.otus.tester.assertions;

/**
 * Set of methods for asserting true.
 */
public class AssertTrue {
    public static void assertTrue(boolean condition) {
        if(!condition) throw new RuntimeException("True assertion failed");
    }

    public static void assertTrue(boolean condition, String message) {
        if(!condition) throw new RuntimeException("True assertion failed: " + message);
    }
}
