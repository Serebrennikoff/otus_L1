package ru.otus.tester.methodFilter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * To be used for filtering methods.
 */
public class MethodFilter {
    private MethodFilter() {};

    public static List<Method> filterMethods(final List<Method> methods, Filter filter) {
        List<Method> res = new ArrayList<>();
        for(Method method : methods) {
            if(filter.filter(method)) res.add(method);
        }
        return res;
    }
}
