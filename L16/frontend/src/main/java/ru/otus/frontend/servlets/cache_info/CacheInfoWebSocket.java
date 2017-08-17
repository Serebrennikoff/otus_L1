package ru.otus.frontend.servlets.cache_info;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import ru.otus.frontend.app.DBWorkSimulator;

import java.util.logging.Logger;

@WebSocket
public class CacheInfoWebSocket {
    private static final Logger logger = Logger.getLogger(CacheInfoWebSocket.class.getName());

    private final static String REQ_MESSAGE = "Request mock work";

    private Session session;

    private DBWorkSimulator simulator;

    CacheInfoWebSocket(DBWorkSimulator simulator) {
        this.simulator = simulator;
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        if(data.equals(REQ_MESSAGE)) {
            simulator.startWorkForSession(session);
        }
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        setSession(session);
        logger.info("New web socket connection was established.");
        simulator.getCacheStats(session);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        simulator.stopWorkForSession(session);
        System.out.println("WebSocket connection was closed.");
    }

    private void setSession(Session session) {this.session = session;}
}