package ru.otus.servlets.cache_info;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.springframework.context.ApplicationContext;
import ru.otus.db_service.DBService;
import ru.otus.db_service.DBServiceImp;
import ru.otus.db_service.DBWorkSimulator;

public class CacheInfoWebSocketCreator implements WebSocketCreator {
    private final ApplicationContext context;

    public CacheInfoWebSocketCreator(ApplicationContext context) {
        this.context = context;
    }


    @Override
    public Object createWebSocket(ServletUpgradeRequest servletUpgradeRequest, ServletUpgradeResponse servletUpgradeResponse) {
        DBWorkSimulator simulator = (DBWorkSimulator)context.getBean("dbWorkSimulator");
        CacheInfoWebSocket socket = new CacheInfoWebSocket(simulator);
        return socket;
    }
}
