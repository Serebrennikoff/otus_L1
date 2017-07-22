package ru.otus.parallel_sort;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FourThreadParallelSort {

    private final static int UNSORTED_CODE = 0;
    private final static int QUARTER_SORTED_CODE = 4;
    private final static int HALF_SORTED_CODE = 6;
    private final static int FULL_SORTED_CODE = 7;

    private final static int THREAD_NUM = 4;

    private final Sorter[] threadPool;
    private final Queue<Runnable> jobQueue;

    private final Object monitor = new Object();
    private int curStatus = 0;

    public FourThreadParallelSort() {
        threadPool = new Sorter[THREAD_NUM];
        jobQueue = new ConcurrentLinkedQueue<>();
        for(int i = 0; i < THREAD_NUM; i++) {
            threadPool[i] = new Sorter();
            threadPool[i].start();
        }
    }

    public void sortArray(int[] arr) {
        int sharesNum = THREAD_NUM;
        int threadJobShare;
        int jobRemainder;

        while (curStatus != FULL_SORTED_CODE) {

            threadJobShare = arr.length/sharesNum;
            jobRemainder = arr.length%sharesNum;

            divideAndSort(arr, threadJobShare, jobRemainder, sharesNum);

            sharesNum /= 2;
        }

        curStatus = 0;
    }

    public void stopWork() {
        for (Sorter thread : threadPool) {
            thread.terminate();
            try {
                thread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(thread.getName() + " was stopped.");
        }
    }

    private void divideAndSort(int[] arr, int threadJobShare, int jobRemainder, int sharesNum) {
        int nextStatus;
        switch (curStatus) {
            case UNSORTED_CODE: nextStatus = QUARTER_SORTED_CODE; break;
            case QUARTER_SORTED_CODE: nextStatus = HALF_SORTED_CODE; break;
            case HALF_SORTED_CODE: nextStatus = FULL_SORTED_CODE; break;
            default: throw new RuntimeException("Should not reach this.");
        }

        int coveredRange = 0;

        for(int i = 0; i < sharesNum; i++) {
            int curCoveredRange = coveredRange; // effectively final
            int range = (jobRemainder-- > 0) ? threadJobShare+1 : threadJobShare;

            jobQueue.add(() -> {
                Arrays.sort(arr, curCoveredRange, curCoveredRange+range);
                synchronized (monitor) {
                    curStatus++;
                    monitor.notify();
                }
            });

            coveredRange += range;
        }
        synchronized (monitor) {
            while (curStatus != nextStatus) {
                try {
                    monitor.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class Sorter extends Thread {
        private volatile boolean running = true;

        void terminate() {running = false;}

        @Override
        public void run() {
            Runnable r;

            while (running) {
                while ((r = jobQueue.poll()) == null && running) {
                    //do nothing
                }
                if (running) {
                    r.run();
                }
            }
        }
    }
}
