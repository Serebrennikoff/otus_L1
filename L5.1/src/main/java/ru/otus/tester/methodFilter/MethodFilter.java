package ru.otus.tester.methodFilter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * To be used for filtering methods.
 */
public class MethodFilter {
    private MethodFilter() {};

    public static List<Method> filterMethods(final List<Method> methods, Predicate<Method> p) {
        List<Method> res = new ArrayList<>();
        for(Method method : methods) {
            if(p.test(method)) res.add(method);
        }
        return res;
    }
}
