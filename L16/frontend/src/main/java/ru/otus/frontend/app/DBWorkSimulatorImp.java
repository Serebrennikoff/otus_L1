package ru.otus.frontend.app;

import org.eclipse.jetty.websocket.api.Session;
import ru.otus.message_system.channels.SocketClientChannel;
import ru.otus.message_system.message_server.MessageServer;
import ru.otus.message_system.messages.Msg;
import ru.otus.message_system.messages.db_responses.DBResponseMsg;
import ru.otus.message_system.messages.front_to_db.MsgGetCacheStats;
import ru.otus.message_system.messages.front_to_db.MsgGetUserByIdAndReturnCacheStats;
import ru.otus.message_system.messages.front_to_db.payload_objects.UserIdPayload;
import ru.otus.message_system.messages.identification.MsgIdentifyService;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBWorkSimulatorImp implements DBWorkSimulator {
    private static final Logger logger = Logger.getLogger(DBWorkSimulatorImp.class.getName());

    private static final String HOST = "localhost";
    private static final int PORT = 8081;
    private static final int DB_ENTRIES_NUM = 4;

    private static final String IDENTIFICATION_MSG =
            "ru.otus.message_system.messages.identification.MsgIdentifyService";

    private final Timer timer;
    private final Map<Session, TimerTask> sessionTimerTaskMapping;
    private final Map<String, Session> msgIdSessionMapping;
    private SocketClientChannel client;

    @SuppressWarnings("InfiniteLoopStatement")
    public DBWorkSimulatorImp() {
        timer = new Timer();
        sessionTimerTaskMapping = new HashMap<>();
        msgIdSessionMapping = new HashMap<>();

        try {
            client = new SocketClientChannel(new Socket(HOST, PORT));
            client.init();
            Msg msg = client.take();
            if (msg.getClassName().equals(IDENTIFICATION_MSG)) {
                Msg response = new MsgIdentifyService(MessageServer.ServiceType.FRONTEND);
                client.send(response);
            } else {
                logger.log(Level.SEVERE, "Failed to receive identification message from server.");
                throw new RuntimeException();
            }
        } catch (IOException | InterruptedException e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw new RuntimeException();
        }

        ExecutorService incMsgMonitor = Executors.newSingleThreadExecutor();
        incMsgMonitor.submit(() -> {
            try {
                while (true) {
                    Msg msg = client.take();
                    String msgId = ((DBResponseMsg)msg).getReplyToMsgId();
                    Session session = msgIdSessionMapping.get(msgId);
                    if (session != null && session.isOpen()) {
                        session.getRemote().sendString(msg.getPayload());
                    }
                    msgIdSessionMapping.remove(msgId);
                }
            } catch (InterruptedException | IOException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        });

        logger.info("Frontend app initialized.");
    }

    @Override
    public void startWorkForSession(Session session) {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                long id = (long)((Math.random() * 10) % DB_ENTRIES_NUM) + 1;
                MsgGetUserByIdAndReturnCacheStats msg =
                        new MsgGetUserByIdAndReturnCacheStats(
                                client.getLocalPort(),
                                new UserIdPayload(id)
                                );
                msgIdSessionMapping.put(msg.getMsgId(), session);
                client.send(msg);
            }
        };
        sessionTimerTaskMapping.putIfAbsent(session, timerTask);
        timer.scheduleAtFixedRate(timerTask,0,1000 * 3);
    }

    @Override
    public void stopWorkForSession(Session session) {
        TimerTask timerTask = sessionTimerTaskMapping.get(session);
        if(timerTask != null) timerTask.cancel();
        sessionTimerTaskMapping.remove(session);
    }

    @Override
    public void getCacheStats(Session session) {
        MsgGetCacheStats msg = new MsgGetCacheStats(client.getLocalPort());
        msgIdSessionMapping.put(msg.getMsgId(), session);
        client.send(msg);
    }
}
