package ru.otus.tester;

import ru.otus.tester.annotations.*;
import ru.otus.tester.methodFilter.MethodFilter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Performs testing.
 */
public class Tester {
    private Tester(){}

    public static void test(Class<?>... classesToTest) {
        for(Class<?> classToTest : classesToTest) {
            testClass(classToTest);
        }
    }

    private static void testClass(Class<?> classToTest) {
        List<Method> methods = Arrays.asList(classToTest.getMethods());
        List<Method> beforeAllMethods = MethodFilter.filterMethods(methods, meth -> meth.getAnnotation(BeforeAll.class) != null);
        List<Method> beforeEachMethods = MethodFilter.filterMethods(methods, meth -> meth.getAnnotation(BeforeEach.class) != null);
        List<Method> afterEachMethods = MethodFilter.filterMethods(methods, meth -> meth.getAnnotation(AfterEach.class) != null);
        List<Method> afterAllMethods = MethodFilter.filterMethods(methods, meth -> meth.getAnnotation(AfterAll.class) != null);
        List<Method> testMethods = MethodFilter.filterMethods(methods, meth -> meth.getAnnotation(Test.class) != null);

        try {
            Object obj = classToTest.newInstance();
            for(Method beforeAll : beforeAllMethods) {
                beforeAll.invoke(classToTest.cast(obj));
            }
            for(Method testMeth: testMethods) {
                // invoke all beforeEach methods
                for(Method beforeEach : beforeEachMethods) {
                    beforeEach.invoke(classToTest.cast(obj));
                }
                // invoke test method
                try {
                    if(testMeth.getAnnotation(DisplayName.class) != null)
                        System.out.print(testMeth.getAnnotation(DisplayName.class).name());
                    else
                        System.out.print(testMeth.getName());
                    testMeth.invoke(classToTest.cast(obj));
                    System.out.println(" PASSED");
                    System.out.println("-------------------------------");
                } catch (InvocationTargetException e) {
                    System.out.println(" FAILED");
                    System.out.println("\t- " + e.getTargetException().getMessage());
                    System.out.println("-------------------------------");
                }
                // invoke all afterEach methods
                for(Method afterEach : afterEachMethods) {
                    afterEach.invoke(classToTest.cast(obj));
                }
            }
            for(Method afterAll : afterAllMethods) {
                afterAll.invoke(classToTest.cast(obj));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate test class " + e.getMessage());
        }

    }
}
