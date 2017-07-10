package ru.otus.servlets;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.h2.tools.Server;
import ru.otus.db_service.DBService;
import ru.otus.db_service.DBServiceImp;
import ru.otus.db_service.data_sets.AddressDataSet;
import ru.otus.db_service.data_sets.PhoneDataSet;
import ru.otus.db_service.data_sets.UserDataSet;
import ru.otus.servlets.seriaizable.CacheInfo;

import java.util.Timer;
import java.util.TimerTask;

@WebSocket
public class CacheInfoWebSocket {
    private final static String REQ_MESSAGE = "Request mock work";

    private Session session;

    private final DBService dbService;
    private final Timer timer;

    public CacheInfoWebSocket() {
        try {
            Server.createWebServer("-web","-webAllowOthers","-webPort","8082").start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        dbService = new DBServiceImp();
        timer = new Timer();
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        if(data.equals(REQ_MESSAGE)) {
            putInDB();
            mockSomeDBWork();
        }
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        System.out.println("Connection was established");
        setSession(session);
        try {
            session.getRemote().sendString(serializeCacheStats());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.println("Connection was closed");
    }

    private String serializeCacheStats() {
        return new Gson().toJson(new CacheInfo(dbService.getStatistics()));
    }

    private void setSession(Session session) {this.session = session;}

    private void mockSomeDBWork() {
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                long id = (long)((Math.random() * 10) % 4) + 1;
                dbService.findById(id);
                try {
                    session.getRemote().sendString(serializeCacheStats());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        },0,1000 * 3);
    }

    private void putInDB() {
        dbService.save(new UserDataSet("Peter",
                new AddressDataSet("Baker str.", 142049),
                new PhoneDataSet(234, "321-234"),
                new PhoneDataSet(434, "188-990")));

        dbService.save(new UserDataSet("Chris",
                new AddressDataSet("Fine str.", 143045),
                new PhoneDataSet(234, "311-834")));

        dbService.save(new UserDataSet("Shone",
                new AddressDataSet("Baker str.", 142049),
                new PhoneDataSet(234, "241-234")));

        dbService.save(new UserDataSet("Bruce",
                new AddressDataSet("Baum str.", 322049),
                new PhoneDataSet(433, "184-933"),
                new PhoneDataSet(433, "324-553")));
    }
}
