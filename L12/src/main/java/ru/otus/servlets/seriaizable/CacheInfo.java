package ru.otus.servlets.seriaizable;


import org.hibernate.stat.Statistics;

public class CacheInfo {
    private final String[] regNames;
    private final long putNum;
    private final long hitNum;
    private final long missNum;

    public CacheInfo(Statistics stats) {
        regNames = stats.getSecondLevelCacheRegionNames();
        putNum = stats.getSecondLevelCachePutCount();
        hitNum = stats.getSecondLevelCacheHitCount();
        missNum = stats.getSecondLevelCacheMissCount();
    }
}
