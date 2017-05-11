package ru.otus.tester.methodFilter;

import java.lang.reflect.Method;

/**
 * Functional interface to be used for filtering method lists.
 */
@FunctionalInterface
public interface Filter {
    /**
     * Evaluates suitability of a given method.
     * @param meth method to evaluate
     * @return true if method meets given requirements
     */
    boolean filter(Method meth);
}
