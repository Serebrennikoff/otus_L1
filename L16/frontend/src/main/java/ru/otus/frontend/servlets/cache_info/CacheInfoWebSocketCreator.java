package ru.otus.frontend.servlets.cache_info;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import ru.otus.frontend.app.DBWorkSimulator;

import java.util.logging.Logger;

public class CacheInfoWebSocketCreator implements WebSocketCreator {
    private final DBWorkSimulator workSimulator;

    CacheInfoWebSocketCreator(DBWorkSimulator workSimulator) {
        this.workSimulator = workSimulator;
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest servletUpgradeRequest, ServletUpgradeResponse servletUpgradeResponse) {
        return new CacheInfoWebSocket(workSimulator);
    }
}
