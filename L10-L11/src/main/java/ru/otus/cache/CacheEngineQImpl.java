package ru.otus.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the CacheEngine interface with use of the priority queue.
 */
public class CacheEngineQImpl<K, V> implements CacheEngine<K, V> {
    private static final int QUEUE_HEAD = 0;

    private final int maxElements;
    private final long lifeTimeMs;
    private final long idleTimeMs;

    /** Container for cached elements for fast lookup by key */
    private final Map<K, CachedElem<K, V>> elements = new HashMap<>();
    /** Queue which stores keys and through which eviction is performed  */
    private Object[] q;
    private int qTail = 0;

    private int hitNum = 0;
    private int missNum = 0;

    public CacheEngineQImpl(int maxElements, long lifeTimeMs, long idleTimeMs) {
        this.maxElements = (maxElements > 0) ? maxElements : 0;
        this.lifeTimeMs = (lifeTimeMs > 0) ? lifeTimeMs : 0;
        this.idleTimeMs = (idleTimeMs > 0) ? idleTimeMs : 0;
        this.q = new Object[maxElements];
    }

    @Override
    public void put(K key, V val) {
        if(maxElements == 0) return;
        if(qTail >= maxElements) removeFromCache();
        CachedElem<K, V> element = new CachedElem<>(key, val);
        elements.put(key, element);
        q[qTail] = key;
        element.setQPos(qTail++);
    }

    private void removeFromCache() {
        K elemToRemoveKey = removeFromQ();
        elements.remove(elemToRemoveKey);
    }

    private K removeFromQ() {
        K removedKey = (K)q[QUEUE_HEAD];
        q[QUEUE_HEAD] = q[--qTail];
        heapify(QUEUE_HEAD);
        return removedKey;
    }

    private void heapify(int i) {
        int firstChild;
        int secondChild;
        int shortestTimeElem;

        while(true) {
            firstChild = 2*i+1;
            secondChild = 2*i+2;
            shortestTimeElem = i;

            if(firstChild < qTail) {
                shortestTimeElem = selectElemToPromote(shortestTimeElem, firstChild);
            }

            if(secondChild < qTail) {
                shortestTimeElem = selectElemToPromote(shortestTimeElem, secondChild);
            }

            if(shortestTimeElem == i) {
                elements.get((K)q[i]).setQPos(i);
                break;
            }

            Object temp = q[i];
            q[i] = q[shortestTimeElem];
            elements.get((K)q[i]).setQPos(i);
            q[shortestTimeElem] = temp;
            i = shortestTimeElem;
        }
    }

    private int selectElemToPromote(int parentPos, int childPos) {
        CachedElem<K, V> parentElem = elements.get((K)q[parentPos]);
        CachedElem<K, V> childElem = elements.get((K)q[childPos]);

        long parElemShortestTimeVal = Math.min(computeLifeTimeEnd(parentElem.getCreationTime()),
                computeIdleTimeEnd(parentElem.getLastAccessTime()));
        long childElemShortestTimeVal = Math.min(computeLifeTimeEnd(childElem.getCreationTime()),
                computeIdleTimeEnd(childElem.getLastAccessTime()));

        return (childElemShortestTimeVal < parElemShortestTimeVal) ? childPos : parentPos;
    }

    private long computeLifeTimeEnd(long creationTime) {
        return (creationTime + lifeTimeMs);
    }

    private long computeIdleTimeEnd(long creationTime) {
        return (creationTime + idleTimeMs);
    }

    @Override
    public V get(K key) {
        CachedElem<K, V> elem = elements.get(key);
        if(elem != null) {
            elem.updateAccessTime();
            heapify(elem.getQPos());
            hitNum++;
            return elem.getVal();
        }

        missNum++;
        return null;
    }

    @Override
    public int getHitCount() {
        return hitNum;
    }

    @Override
    public int getMissCount() {
        return missNum;
    }

    @Override
    public void dispose() {
        elements.clear();
        q = new Object[maxElements];
        qTail = 0;
    }
}
