package ru.otus.cache;

/**
 * Wrapper for the element to be cached.
 */
class CachedElem<K, V> {
    private static final int TIME_THRESHOLD_MS = 1;

    private final K key;
    private final V val;

    private final long creationTime;
    private long lastAccessTime;

    private int qPos;

    protected CachedElem(K key, V val) {
        this.key = key;
        this.val = val;
        this.lastAccessTime = this.creationTime = System.currentTimeMillis();
    }

    protected K getKey() {return key;}
    protected V getVal() {return val;}

    protected long getCreationTime() {return creationTime;}
    protected long getLastAccessTime() {return lastAccessTime;}

    protected void updateAccessTime() {lastAccessTime = System.currentTimeMillis() + TIME_THRESHOLD_MS;}

    protected void setQPos(int pos) {qPos = pos;}
    protected int getQPos() {return qPos;}
}
