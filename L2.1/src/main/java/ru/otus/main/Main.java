package ru.otus.main;

import java.util.*;

import static ru.otus.agent.Agent.getObjectSize;

/**
 * Outputs sizes of data types in memory.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("------------------------");
        System.out.println("Sizes of objects in memory");
        System.out.println("------------------------");
        System.out.println();

        System.out.println("Empty String:");
        printSize(new String(""));
        System.out.println();

        System.out.println("Empty array:");
        printSize(new int[0]);
        System.out.println();

        System.out.println("Array with length of 10:");
        printSize(new int[10]);
        System.out.println();

        System.out.println("Raw Object:");
        printSize(new Object());
        System.out.println();

        System.out.println("Reference:");
        System.out.println("reference - " + (getObjectSize(new Object[1])-getObjectSize(new Object[0])) + " bytes");
        System.out.println();

        System.out.println("ArrayList:");
        printSize(new ArrayList<>());
        System.out.println();

        System.out.println("LinkedList:");
        printSize(new LinkedList<>());
        System.out.println();

        System.out.println("------------------------");
    }

    private static void printSize(Object obj) {
        String output = String.format("%s - %d bytes", obj.getClass().getSimpleName(), getObjectSize(obj));
        System.out.println(output);
    }
}
