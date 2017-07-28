package ru.otus.frontend;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.hibernate.stat.Statistics;
import ru.otus.db_service.MsgGetCacheStats;
import ru.otus.db_service.MsgGetUserByIdAndReturnCacheStats;
import ru.otus.db_service.MsgPersistUser;
import ru.otus.db_service.data_sets.AddressDataSet;
import ru.otus.db_service.data_sets.PhoneDataSet;
import ru.otus.db_service.data_sets.UserDataSet;
import ru.otus.message_system.Address;
import ru.otus.message_system.Addressee;
import ru.otus.message_system.Message;
import ru.otus.message_system.MessageSystemContext;
import ru.otus.servlets.seriaizable.CacheInfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class DBWorkSimulatorImp implements DBWorkSimulator, Addressee {
    private final static int DB_ENTRIES_NUM = 4;

    private final Address address;
    private final MessageSystemContext context;

    private final Timer timer;
    private final Map<Session, TimerTask> sessionTimerTaskMapping;
    private boolean isDBPopulated = false;

    public DBWorkSimulatorImp(MessageSystemContext context, Address address) {
        this.address = address;
        this.context = context;
        this.timer = new Timer();
        this.sessionTimerTaskMapping = new HashMap<>();

        register();
    }

    @Override
    public void startWorkForSession(Session session) {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                long id = (long)((Math.random() * 10) % DB_ENTRIES_NUM) + 1;
                Message message = new MsgGetUserByIdAndReturnCacheStats(context.getMessageSystem(),
                                                                        getAddress(),
                                                                        context.getDbAddress(),
                                                                        id,
                                                                        session);
                context.getMessageSystem().sendMessage(message);
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
        if(!isDBPopulated) {
            populateDB();
            isDBPopulated = true;
        }
        Message message = new MsgGetCacheStats(context.getMessageSystem(),
                                                getAddress(),
                                                context.getDbAddress(),
                                                session);
        context.getMessageSystem().sendMessage(message);
    }

    @Override
    public void publishCacheStats(Session session, Statistics stats) {
        try {
            session.getRemote().sendString(serializeCacheStats(new CacheInfo(stats)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Address getAddress() {
        return address;
    }

    private void register() {
        context.getMessageSystem().addAddressee(this);
    }

    private void populateDB() {
        UserDataSet[] users = {
                new UserDataSet("Peter",
                        new AddressDataSet("Baker str.", 142049),
                        new PhoneDataSet(234, "321-234"),
                        new PhoneDataSet(434, "188-990")),

                new UserDataSet("Chris",
                        new AddressDataSet("Fine str.", 143045),
                        new PhoneDataSet(234, "311-834")),

                new UserDataSet("Shone",
                        new AddressDataSet("Baker str.", 142049),
                        new PhoneDataSet(234, "241-234")),

                new UserDataSet("Bruce",
                        new AddressDataSet("Baum str.", 322049),
                        new PhoneDataSet(433, "184-933"),
                        new PhoneDataSet(433, "324-553"))
        };

        for (UserDataSet user : users) {
            Message message = new MsgPersistUser(getAddress(), context.getDbAddress(), user);
            context.getMessageSystem().sendMessage(message);
        }
    }

    private String serializeCacheStats(CacheInfo cacheInfo) {
        return new Gson().toJson(cacheInfo);
    }

}
