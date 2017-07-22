package ru.otus;

import ru.otus.parallel_sort.FourThreadParallelSort;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        FourThreadParallelSort parSort = new FourThreadParallelSort();
        int arrSize = 25;

        int[] arr = populateAndShuffle(arrSize);
        for (Integer integer : arr) {
            System.out.print(integer + " ");
        }
        System.out.println();
        System.out.println("-----------------------------------------------------------");

        parSort.sortArray(arr);

        for (Integer integer : arr) {
            System.out.print(integer + " ");
        }
        System.out.println();
        System.out.println("-----------------------------------------------------------");

        arrSize = 10999;
        arr = populateAndShuffle(arrSize);

        for (Integer integer : arr) {
            System.out.print(integer + " ");
        }
        System.out.println();
        System.out.println("-----------------------------------------------------------");

        parSort.sortArray(arr);

        for (Integer integer : arr) {
            System.out.print(integer + " ");
        }
        System.out.println();
        System.out.println("-----------------------------------------------------------");

        parSort.stopWork();
    }

    private static int[] populateAndShuffle(int arrSize) {
        int[] arr = new int[arrSize];
        List<Integer> list = new LinkedList<>();

        for(int i = 0; i < arrSize; i++) {
            list.add(i);
        }

        Collections.shuffle(list);

        for (int i = 0; i < arrSize; i++) {
            arr[i] = list.get(i);
        }

        return arr;
    }
}
