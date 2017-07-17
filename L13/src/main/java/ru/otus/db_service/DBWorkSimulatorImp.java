package ru.otus.db_service;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import ru.otus.db_service.data_sets.AddressDataSet;
import ru.otus.db_service.data_sets.PhoneDataSet;
import ru.otus.db_service.data_sets.UserDataSet;
import ru.otus.servlets.seriaizable.CacheInfo;

import java.util.Timer;
import java.util.TimerTask;

public class DBWorkSimulatorImp implements DBWorkSimulator {
    private final static int DB_ENTRIES_NUM = 4;

    private static boolean wasPopulated = false;

    private final DBService dbService;
    private final Timer timer = new Timer();

    public DBWorkSimulatorImp(DBService dbService) {this.dbService = dbService;}

    @Override
    public void startWork(Session session) {
        synchronized (DBWorkSimulatorImp.class) {
            if (!wasPopulated) {
                populateDB();
                wasPopulated = true;
            }
        }

        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                long id = (long)((Math.random() * 10) % DB_ENTRIES_NUM) + 1;
                dbService.findById(id);
                try {
                    session.getRemote().sendString(serializeCacheStats());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        },0,1000 * 3);
    }

    @Override
    public void stopWork() {
        timer.cancel();
    }

    @Override
    public String getCacheStats() {
        return serializeCacheStats();
    }

    private void populateDB() {
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

    private String serializeCacheStats() {
        return new Gson().toJson(new CacheInfo(dbService.getStatistics()));
    }
}
