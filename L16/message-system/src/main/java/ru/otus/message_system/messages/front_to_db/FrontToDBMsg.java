package ru.otus.message_system.messages.front_to_db;

import ru.otus.message_system.messages.Msg;

import java.sql.Timestamp;

public abstract class FrontToDBMsg extends Msg {
    // Message header
    private final String msgId;
    public final boolean waitForReply;

    protected FrontToDBMsg(Class<?> klass,
                           int senderLocalPort,
                           String payload,
                           boolean waitForReply) {
        super(klass, payload);
        this.msgId = Integer.toString(senderLocalPort) + " - " + Long.toString(System.nanoTime());
        this.waitForReply = waitForReply;
    }

    public String getMsgId() {return msgId;}
}
