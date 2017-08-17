package ru.otus.message_system.messages.db_responses;

import ru.otus.message_system.messages.Msg;

public abstract class DBResponseMsg extends Msg {
    private final String replyToMsgId;

    protected DBResponseMsg(Class<?> klass, String replyToMsgId, String payload) {
        super(klass, payload);
        this.replyToMsgId = replyToMsgId;
    }

    public String getReplyToMsgId() {return replyToMsgId;}
}
