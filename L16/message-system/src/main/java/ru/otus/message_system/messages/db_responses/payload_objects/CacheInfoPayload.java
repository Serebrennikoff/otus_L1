package ru.otus.message_system.messages.db_responses.payload_objects;

public class CacheInfoPayload {
    private final String[] regNames;
    private final long putNum;
    private final long hitNum;
    private final long missNum;

    public CacheInfoPayload(String[] regNames, long putNum, long hitNum, long missNum) {
        this.regNames = regNames;
        this.putNum = putNum;
        this.hitNum = hitNum;
        this.missNum = missNum;
    }
}
