package ru.otus.message_system.messages.front_to_db;

import ru.otus.message_system.messages.Msg;
import ru.otus.message_system.messages.front_to_db.payload_objects.UserDSPayload;

public class MsgPopulateDB extends FrontToDBMsg {
    public MsgPopulateDB(int senderLocalPort,
                         UserDSPayload payloadObj) {
        super(MsgPopulateDB.class,
                senderLocalPort,
                Msg.stringifyPayloadObject(payloadObj),
                false);
    }
}
