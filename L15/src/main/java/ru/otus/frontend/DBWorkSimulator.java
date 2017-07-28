package ru.otus.frontend;

import org.eclipse.jetty.websocket.api.Session;
import org.hibernate.stat.Statistics;

public interface DBWorkSimulator {

    void startWorkForSession(Session session);

    void stopWorkForSession(Session session);

    void getCacheStats(Session session);

    void publishCacheStats(Session session, Statistics stats);
}
