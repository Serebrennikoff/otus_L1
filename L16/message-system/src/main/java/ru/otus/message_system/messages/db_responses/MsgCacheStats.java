package ru.otus.message_system.messages.db_responses;

import ru.otus.message_system.messages.Msg;
import ru.otus.message_system.messages.db_responses.payload_objects.CacheInfoPayload;

public class MsgCacheStats extends DBResponseMsg {
    public MsgCacheStats(String replyToMsgId, CacheInfoPayload payload) {
        super(MsgCacheStats.class, replyToMsgId, Msg.stringifyPayloadObject(payload));
    }
}
