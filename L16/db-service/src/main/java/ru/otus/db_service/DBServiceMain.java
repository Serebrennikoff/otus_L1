package ru.otus.db_service;

import com.google.gson.Gson;
import org.hibernate.stat.Statistics;
import ru.otus.db_service.app.DBService;
import ru.otus.db_service.app.DBServiceImp;
import ru.otus.db_service.data_sets.AddressDataSet;
import ru.otus.db_service.data_sets.PhoneDataSet;
import ru.otus.db_service.data_sets.UserDataSet;
import ru.otus.message_system.channels.SocketClientChannel;
import ru.otus.message_system.message_server.MessageServer;
import ru.otus.message_system.messages.Msg;
import ru.otus.message_system.messages.db_responses.MsgCacheStats;
import ru.otus.message_system.messages.db_responses.payload_objects.CacheInfoPayload;
import ru.otus.message_system.messages.front_to_db.FrontToDBMsg;
import ru.otus.message_system.messages.front_to_db.payload_objects.UserDSPayload;
import ru.otus.message_system.messages.front_to_db.payload_objects.UserIdPayload;
import ru.otus.message_system.messages.identification.MsgIdentifyService;
import sun.java2d.loops.ProcessPath;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class DBServiceMain {
    // Messages
    private static final String CACHE_STATS =
            "ru.otus.message_system.messages.front_to_db.MsgGetCacheStats";
    private static final String USER_BY_ID_RET_STATS =
            "ru.otus.message_system.messages.front_to_db.MsgGetUserByIdAndReturnCacheStats";
    private static final String POPULATE_DB =
            "ru.otus.message_system.messages.front_to_db.MsgPopulateDB";
    private static final String IDENTIFICATION =
            "ru.otus.message_system.messages.identification.MsgIdentifyService";

    private static final String LOG_CONFIG_PATH = System.getProperty("user.dir") +
            "./db-service/logging.properties";
    private static final Logger logger = Logger.getLogger(DBServiceMain.class.getName());

    private static final String HOST = "localhost";
    private static final int PORT = 8081;

    private final DBService dbService;
    private SocketClientChannel client;

    /**
     * Launch class with "-f" flag if this dbService is required to create new DB server TCP connection.
     */
    public static void main(String[] args) throws Exception {
        LogManager.getLogManager().readConfiguration(new FileInputStream(LOG_CONFIG_PATH));
        String flag = "";
        if (args.length > 0) flag = args[0];
        new DBServiceMain(flag).start();
    }

    private DBServiceMain(String createDBServerFlag) {
        this.dbService = new DBServiceImp(createDBServerFlag);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void start() throws IOException {
        logger.info("DB Service was started: " + ManagementFactory.getRuntimeMXBean().getName());

        Socket socket = new Socket(HOST, PORT);
        client = new SocketClientChannel(socket);
        client.init();

        logger.info("Socket channel was established on local port: " + socket.getLocalPort());

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                while (true) {
                    Msg msg = client.take();
                    logger.info("Message " + msg.getClassName() +
                                " with payload " + msg.getPayload() +
                                " was received by db service");
                    processIncomingMsg(msg);
                }
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        });
    }

    private void processIncomingMsg(Msg msg) {
        Msg response;
        switch (msg.getClassName()) {
            case IDENTIFICATION:
                response = new MsgIdentifyService(MessageServer.ServiceType.DB);
                client.send(response);
                break;
            case CACHE_STATS:
                response = generateCacheInfoResponse(msg);
                client.send(response);
                break;
            case POPULATE_DB:
                UserDSPayload dsPayload = new Gson().fromJson(msg.getPayload(), UserDSPayload.class);
                UserDataSet userDataSet = generateUserDataSet(dsPayload);
                dbService.save(userDataSet);
                logger.info("New user was persisted: " + userDataSet.toString());
                break;
            case USER_BY_ID_RET_STATS:
                UserIdPayload incMsgPayload = new Gson().fromJson(msg.getPayload(), UserIdPayload.class);
                dbService.findById(incMsgPayload.getId());
                response = generateCacheInfoResponse(msg);
                client.send(response);
                break;
            default: throw new RuntimeException("DBService is not able to process the message.");
        }
    }

    private Msg generateCacheInfoResponse(Msg msg) {
        CacheInfoPayload payload = generateCacheInfoPayload(dbService.getStatistics());
        String incomingMsgId = ((FrontToDBMsg)msg).getMsgId();
        MsgCacheStats response = new MsgCacheStats(incomingMsgId, payload);
        return response;
    }

    private CacheInfoPayload generateCacheInfoPayload(Statistics stats) {
        return new CacheInfoPayload(stats.getSecondLevelCacheRegionNames(),
                stats.getSecondLevelCachePutCount(),
                stats.getSecondLevelCacheHitCount(),
                stats.getSecondLevelCacheMissCount());
    }

    private UserDataSet generateUserDataSet(UserDSPayload payload) {
        String userName = payload.getName();
        String[] address = payload.getAddress();
        AddressDataSet addressDataSet = new AddressDataSet(address[0], Integer.parseInt(address[1]));
        Set<String[]> phones = payload.getPhones();
        PhoneDataSet[] phoneDataSets = new PhoneDataSet[phones.size()];
        int counter = 0;
        for (String[] phone : phones) {
            phoneDataSets[counter++] = new PhoneDataSet(Integer.parseInt(phone[0]), phone[1]);
        }
        return new UserDataSet(userName, addressDataSet, phoneDataSets);
    }
}
