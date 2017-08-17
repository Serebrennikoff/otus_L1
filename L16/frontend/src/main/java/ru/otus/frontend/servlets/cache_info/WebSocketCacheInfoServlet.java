package ru.otus.frontend.servlets.cache_info;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import ru.otus.frontend.app.DBWorkSimulatorImp;

import java.io.IOException;
import java.util.logging.LogManager;

public class WebSocketCacheInfoServlet extends WebSocketServlet {
    private final static int LOGOUT_TIME = 10 * 60 * 1000;

    @Override
    public void configure(WebSocketServletFactory factory) {
        try {
            LogManager.getLogManager().readConfiguration(getClass().getClassLoader()
                    .getResourceAsStream("/logging.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        factory.getPolicy().setIdleTimeout(LOGOUT_TIME);
        factory.setCreator(new CacheInfoWebSocketCreator(new DBWorkSimulatorImp()));
    }
}
