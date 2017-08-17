package ru.otus.frontend.app;

import org.eclipse.jetty.websocket.api.Session;

public interface DBWorkSimulator {

    void startWorkForSession(Session session);

    void stopWorkForSession(Session session);

    void getCacheStats(Session session);
}
