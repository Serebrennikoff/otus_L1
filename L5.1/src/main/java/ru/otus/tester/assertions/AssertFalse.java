package ru.otus.tester.assertions;

/**
 * Set of methods for asserting false.
 */
public class AssertFalse {
    public static void assertFalse(boolean condition) {
        if(condition) throw new RuntimeException("False assertion failed");
    }

    public static void assertFalse(boolean condition, String message) {
        if(condition) throw new RuntimeException("False assertion failed: " + message);
    }
}
