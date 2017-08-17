package ru.otus.message_system.messages.front_to_db;

import ru.otus.message_system.messages.Msg;
import ru.otus.message_system.messages.front_to_db.payload_objects.UserIdPayload;

public class MsgGetUserByIdAndReturnCacheStats extends FrontToDBMsg {
    public MsgGetUserByIdAndReturnCacheStats(int senderLocalPort,
                                             UserIdPayload payloadObj) {
            super(MsgGetUserByIdAndReturnCacheStats.class,
                    senderLocalPort,
                    Msg.stringifyPayloadObject(payloadObj),
                    true);
    }
}
