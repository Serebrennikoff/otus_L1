package ru.otus.servlets.cache_info;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.springframework.context.ApplicationContext;
import ru.otus.frontend.DBWorkSimulator;

public class CacheInfoWebSocketCreator implements WebSocketCreator {
    private final DBWorkSimulator workSimulator;

    public CacheInfoWebSocketCreator(DBWorkSimulator workSimulator) {
        this.workSimulator = workSimulator;
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest servletUpgradeRequest, ServletUpgradeResponse servletUpgradeResponse) {
        return new CacheInfoWebSocket(workSimulator);
    }
}
