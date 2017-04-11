package ru.otus.l2;

import java.util.*;

/**
 * Outputs sizes of data types in memory.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Runtime rt = Runtime.getRuntime();
        int size = 1024 * 1024;
        long memAfter, memBefore;

        Object[] arr = new Object[size];
        rt.gc();
        memBefore = rt.totalMemory() - rt.freeMemory();

        for (int i = 0; i < size; i++) {
            arr[i] = new Object();
            // arr[i] = new Integer(1);
            // arr[i] = new String("");
            // arr[i] = new int[0];
            // arr[i] = new int[5];
            // arr[i] = new ArrayList<>();
            // arr[i] = new LinkedList<>();
        }

        rt.gc();

        memAfter = rt.totalMemory() - rt.freeMemory();

        System.out.println("Memory used before: " + memBefore);
        System.out.println("Memory used after: " + memAfter);
        System.out.println("Delta: " + (memAfter - memBefore));
        System.out.println();

        System.out.println(arr[0].getClass().getSimpleName() + " size: " + (memAfter-memBefore)/size + " bytes");
    }
}
