package ru.otus.message_system.channels;

import ru.otus.message_system.messages.Msg;

import java.io.IOException;

public interface MsgChannel {
    void send(Msg msg);

    Msg pool();

    Msg take() throws InterruptedException;

    boolean wasClosed();

    void close() throws IOException;

    int getLocalPort();

    Msg retrieveOutcomingMsg();
}
