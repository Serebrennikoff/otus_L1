package ru.otus.l4;

import java.lang.management.GarbageCollectorMXBean;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * ...
 */
class Benchmark implements BenchmarkMBean {
    private int size = 0;
    private boolean memLeakOn = true;

    private GarbageCollectorMXBean youngGC;
    private GarbageCollectorMXBean oldGC;
    private long lastTime;

    private long lastYoungGCTime;
    private long lastOldGCTime;
    private long lastYoungGCCollectionCount;
    private long lastOldGCCollectionCount;

    Benchmark(GarbageCollectorMXBean youngGC, GarbageCollectorMXBean oldGC, long startTime) {
        this.youngGC = youngGC;
        this.oldGC = oldGC;
        this.lastTime = startTime;

        lastYoungGCTime = youngGC.getCollectionTime();
        lastOldGCTime = oldGC.getCollectionTime();
        lastYoungGCCollectionCount = youngGC.getCollectionCount();
        lastOldGCCollectionCount = oldGC.getCollectionCount();
    }

    void run() {
        Object[] array = new Object[size];
        List<String> gutter = new LinkedList<>();

        int n = 0;
        double timePassed;
        int currentSize = size;

        while (n < Integer.MAX_VALUE) {

            timePassed = (System.nanoTime() - lastTime)/1e9;
            if(timePassed >= 60) { // log gc performance every 30 seconds
                logGCPerformance(timePassed);
                lastTime = System.nanoTime();
            }

            int i = n % currentSize;
            array[i] = new String(new char[0]);

            if(n % 199 == 0 && memLeakOn)
                gutter.add(new String(new char[0]));

            n++;
            if (n % currentSize == 0) {
                currentSize = size;
                array = new Object[currentSize];
            }
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public boolean getMemLeakStatus() {return memLeakOn;}

    @Override
    public void setMemLeakStatus(boolean memLeakOn) {
        this.memLeakOn = memLeakOn;
        System.out.println();
        System.out.println("---------------------------------");
        if(memLeakOn) System.out.println("Memory leak turned on");
        else System.out.println("Memory leak turned off");
        System.out.println("---------------------------------");
        System.out.println();
    }

    private void logGCPerformance(double timePassed) {
        System.out.println("Time passed since last log: " + String.format(Locale.US,"%.2f seconds", timePassed));
        System.out.println(youngGC.getName() + " performed " + (youngGC.getCollectionCount() - lastYoungGCCollectionCount)
                + " collections in " + (youngGC.getCollectionTime() - lastYoungGCTime)/1000 + " seconds");
        System.out.println(oldGC.getName() + " performed " + (oldGC.getCollectionCount() - lastOldGCCollectionCount)
                + " collections in " + (oldGC.getCollectionTime() - lastOldGCTime)/1000 + " seconds");
        System.out.println("---------------------------------");

        lastOldGCTime = oldGC.getCollectionTime();
        lastYoungGCTime = youngGC.getCollectionTime();
        lastOldGCCollectionCount = oldGC.getCollectionCount();
        lastYoungGCCollectionCount = youngGC.getCollectionCount();
    }

}
