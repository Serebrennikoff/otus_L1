package ru.otus.message_system.message_server;

public interface MessageServerMBean {
    boolean getRunning();

    void setRunning(boolean running);
}
