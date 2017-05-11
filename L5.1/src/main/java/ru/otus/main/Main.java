package ru.otus.main;

import ru.otus.tester.Tester;
import ru.otus.tester.annotations.*;

import static ru.otus.tester.assertions.AssertFalse.*;
import static ru.otus.tester.assertions.AssertTrue.*;
import static ru.otus.tester.assertions.AssertNotNull.*;

/**
 * ...
 */
public class Main {
    public static void main(String[] args) {
        Tester.test(Main.class);
    }

    private int counter = 0;

    @BeforeAll
    public void init() {
        System.out.println("--------------------");
        System.out.println("Testing started.");
        System.out.println("--------------------");
        System.out.println();
    }

    @AfterAll
    public void finish() {
        System.out.println();
        System.out.println("--------------------");
        System.out.println("Testing finished");
        System.out.println("--------------------");
    }

    @BeforeEach
    public void showTestNum() {
        System.out.println("TEST N." + counter);
    }

    @AfterEach
    public void incCounter() {counter++;}

    @Test
    public void firstAssertionFalseTest() {
        assertFalse("False".equals("True"));
    }

    @Test
    public void secondAssertionFalseTest() {
        assertFalse("False".equals("False"), "False is to be equal to false");
    }

    @Test
    @DisplayName(name="Test passing assert true")
    public void thirdTest() {
        assertTrue("True".equals("True"));
    }

    @Test
    @DisplayName(name="Test failing not null test")
    public void fourthTest() {
        int[] arr = null;
        assertNotNull(arr, "Array should not be equal to null reference");
    }
}
