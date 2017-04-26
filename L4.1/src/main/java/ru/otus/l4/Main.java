package ru.otus.l4;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

/**
 * Starts benchmark class and logs the work of gc currently in use.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());
        System.out.println("---------------------------------");
        List<GarbageCollectorMXBean> gcList = ManagementFactory.getGarbageCollectorMXBeans();

        int size = 5 * 1000 * 1000;

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ru.otus:type=Benchmark");
        Benchmark mbean = new Benchmark(gcList.get(0), gcList.get(1), System.nanoTime());
        mbs.registerMBean(mbean, name);

        mbean.setSize(size);
        mbean.run();
    }
}

