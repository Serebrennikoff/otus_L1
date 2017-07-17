package ru.otus.db_service;

import org.eclipse.jetty.websocket.api.Session;

public interface DBWorkSimulator {

    void startWork(Session session);

    void stopWork();

    String getCacheStats();
}
