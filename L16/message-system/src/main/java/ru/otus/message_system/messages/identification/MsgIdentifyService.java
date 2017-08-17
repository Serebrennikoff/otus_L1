package ru.otus.message_system.messages.identification;

import ru.otus.message_system.message_server.MessageServer;
import ru.otus.message_system.messages.Msg;

public class MsgIdentifyService extends Msg {
    public MsgIdentifyService() {
        super(MsgIdentifyService.class, null);
    }

    public MsgIdentifyService(MessageServer.ServiceType payload) {
        super(MsgIdentifyService.class, Msg.stringifyPayloadObject(payload));
    }
}
