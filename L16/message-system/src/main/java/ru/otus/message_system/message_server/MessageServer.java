package ru.otus.message_system.message_server;

import com.google.gson.Gson;
import ru.otus.message_system.channels.MsgChannel;
import ru.otus.message_system.channels.SocketClientChannel;
import ru.otus.message_system.messages.Msg;
import ru.otus.message_system.messages.db_responses.DBResponseMsg;
import ru.otus.message_system.messages.front_to_db.FrontToDBMsg;
import ru.otus.message_system.messages.identification.MsgIdentifyService;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.*;

public class MessageServer implements MessageServerMBean {
    private static final Logger logger = Logger.getLogger(MessageServer.class.getName());

    public enum ServiceType {FRONTEND, DB}

    private static final int THREADS_NUM = 2;
    private static final int PORT = 8081;
    private static final int MONITOR_DELAY = 100;

    private final ExecutorService executor;
    private final List<MsgChannel> frontChannels;
    private final List<MsgChannel> dbChannels;
    private int dbChannelsCounter = 0;
    private final Map<String, MsgChannel> msgRegistry;

    public MessageServer() {
        executor = Executors.newFixedThreadPool(THREADS_NUM);
        frontChannels = new ArrayList<>();
        dbChannels = Collections.synchronizedList(new ArrayList<>());
        msgRegistry = new HashMap<>();
    }

    public void start() throws Exception {
        executor.submit(this::monitorFrontChannels);
        executor.submit(this::monitorDBChannels);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.info("Message server started on port: " + serverSocket.getLocalPort());
            while (!executor.isShutdown()) {
                Socket client = serverSocket.accept();
                logger.info("Connection accepted from remote port: " + client.getPort());
                SocketClientChannel channel = new SocketClientChannel(client);
                channel.init();
                channel.send(new MsgIdentifyService());
                Msg response = channel.take();
                typeDistributeChannel(
                        channel,
                        new Gson().fromJson(response.getPayload(), MessageServer.ServiceType.class)
                );
            }
        }
    }

    private void typeDistributeChannel(SocketClientChannel channel, ServiceType type) {
        switch (type) {
            case DB:
                dbChannels.add(channel);
                logger.info("New channel added to db service list. List size: " + dbChannels.size());
                break;
            case FRONTEND:
                frontChannels.add(channel);
                logger.info("New channel added to frontend list. List size: " + frontChannels.size());
                break;
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private Object monitorFrontChannels() throws InterruptedException {
        while (true) {
            Iterator<MsgChannel> iter = frontChannels.iterator();
            while (iter.hasNext()) {
                MsgChannel channel = iter.next();
                if (channel.wasClosed()) {
                    iter.remove();
                } else {
                    Msg msg = channel.pool();
                    if (msg != null) {
                        String msgId = ((FrontToDBMsg) msg).getMsgId();
                        if (((FrontToDBMsg) msg).waitForReply)
                            msgRegistry.put(msgId, channel);
                        synchronized (dbChannels) {
                            if (dbChannels.isEmpty())
                                logger.log(Level.SEVERE, "No db services available.");
                            dbChannelsCounter %= dbChannels.size();
                            dbChannels.get(dbChannelsCounter++).send(msg);
                        }
                    }
                }
            }
            Thread.sleep(MONITOR_DELAY);
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private Object monitorDBChannels() throws InterruptedException {
        while (true) {
            Iterator<MsgChannel> iter = dbChannels.iterator();
            while (iter.hasNext()){
                MsgChannel channel = iter.next();
                if (channel.wasClosed()) {
                    iter.remove();
                    reallocateDBMessages(channel);
                } else {
                    Msg msg = channel.pool();
                    if (msg != null) {
                        processResponseFromDB(msg);
                    }
                }
            }
            Thread.sleep(MONITOR_DELAY);
        }
    }

    private void reallocateDBMessages(MsgChannel channel) {
        Msg msg;
        int localDBChannelsCounter = 0;
        while ((msg = channel.pool()) != null) {
            processResponseFromDB(msg);
        }
        while ((msg = channel.retrieveOutcomingMsg()) != null) {
            dbChannels.get(localDBChannelsCounter%=dbChannels.size()).send(msg);
            localDBChannelsCounter++;
        }
    }

    private void processResponseFromDB(Msg msg) {
        String responseToMsgId = ((DBResponseMsg)msg).getReplyToMsgId();
        MsgChannel associatedChannel = msgRegistry.get(responseToMsgId);
        if (associatedChannel != null && !associatedChannel.wasClosed()) {
            associatedChannel.send(msg);
        }
        msgRegistry.remove(responseToMsgId);
    }

    @Override
    public boolean getRunning() {
        return true;
    }

    @Override
    public void setRunning(boolean running) {
        if (!running) {
            executor.shutdown();
            logger.info("Message server was stopped");
        }
    }
}
