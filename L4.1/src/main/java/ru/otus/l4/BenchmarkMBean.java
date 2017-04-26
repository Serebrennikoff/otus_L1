package ru.otus.l4;

/**
 * Created by tully.
 */
public interface BenchmarkMBean {
    int getSize();

    void setSize(int size);

    boolean getMemLeakStatus();

    void setMemLeakStatus(boolean memLeakOn);
}
