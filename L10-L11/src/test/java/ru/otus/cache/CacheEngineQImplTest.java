package ru.otus.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CacheEngineQImplTest {
    // Testing strategy:
    //      Use of cache capacity: over-use, under-use;
    //      Order of eviction: eviction in order of creation time, eviction in order of
    //          last access time.

    private static CacheEngine<Integer, String> cache;

    private static final int CACHE_SIZE = 3;

    @BeforeEach
    void initCache() {
        cache = new CacheEngineQImpl<>(CACHE_SIZE, 100000, 5000);
    }

    @Test
    void underUseTest() {
        cache.put(1, "First cached elem");
        cache.put(2, "Second cached elem");

        cache.get(1);
        cache.get(2);
        cache.get(1);
        cache.get(3);

        assertTrue(cache.getHitCount() == 3);
        assertTrue(cache.getMissCount() == 1);

    }

    @Test
    void overUseTest() {
        for(int i = 0; i < CACHE_SIZE*2; i++) {
            cache.put(i, "Sting No. " + i);
        }

        for(int i = 0; i < CACHE_SIZE*2; i++) {
            cache.get(i);
        }

        assertTrue(cache.getHitCount() == 3);
        assertTrue(cache.getMissCount() == 3);
    }

    @Test
    void creationTimeEviction() {
        cache.put(1, "First cached elem");
        cache.put(2, "Second cached elem");
        cache.put(3, "Third cached elem");
        cache.put(4,"Fourth cached elem");

        assertTrue(cache.get(1) == null);

        for(int i = 2; i < 5; i++) {
            assertTrue(cache.get(i) != null);
        }

    }

    @Test
    void accessTimeEvictionTest() {
        cache.put(1, "First cached elem");
        cache.put(2, "Second cached elem");
        cache.put(3, "Third cached elem");

        cache.get(1);
        cache.get(2);

        cache.put(4,"Fourth cached elem");

        cache.get(1);
        cache.get(2);
        cache.get(3);
        cache.get(4);

        assertTrue(cache.getHitCount() == 5);
        assertTrue(cache.getMissCount() == 1);
        assertTrue(cache.get(3) == null);
    }
}