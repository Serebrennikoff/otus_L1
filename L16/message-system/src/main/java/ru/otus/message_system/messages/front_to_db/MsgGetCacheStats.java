package ru.otus.message_system.messages.front_to_db;

public class MsgGetCacheStats extends FrontToDBMsg {
    public MsgGetCacheStats(int senderLocalPort) {
        super(MsgGetCacheStats.class, senderLocalPort, null, true);
    }
}
